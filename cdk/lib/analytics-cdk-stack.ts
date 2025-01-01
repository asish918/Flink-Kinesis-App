import * as cdk from 'aws-cdk-lib';
import { Construct } from 'constructs';
import { aws_kinesis as kinesis, aws_kinesisanalyticsv2 as kinesisanalytics, aws_iam as iam, aws_s3 as s3 } from 'aws-cdk-lib';

export class AnalyticsCdkStack extends cdk.Stack {
  constructor(scope: Construct, id: string, props?: cdk.StackProps) {
    super(scope, id, props);

    // Create Kinesis Data Stream
    const sinkStream = new kinesis.Stream(this, 'SinkStreamTest', {
      streamName: 'sink-stream-TEST',
      shardCount: 1,
      retentionPeriod: cdk.Duration.hours(24)
    });

    // Create IAM role for Kinesis Analytics
    const kinesisAnalyticsRole = new iam.Role(this, 'KinesisAnalyticsRole', {
      assumedBy: new iam.ServicePrincipal('kinesisanalytics.amazonaws.com'),
    });
    kinesisAnalyticsRole.addManagedPolicy(
      iam.ManagedPolicy.fromAwsManagedPolicyName('CloudWatchLogsFullAccess')
    );
    cdk.Tags.of(kinesisAnalyticsRole).add('Environment', 'FLINK_TEST_DO_NOT_DELETE');

    sinkStream.grantWrite(kinesisAnalyticsRole);

    // Create S3 bucket for Flink application code
    const flinkCodeBucket = new s3.Bucket(this, 'FlinkCodeBucket', {
      bucketName: `flink-code-bucket-${this.account}-${this.region}`,
      removalPolicy: cdk.RemovalPolicy.DESTROY, // For testing - change for production
      autoDeleteObjects: true, // For testing - change for production
      versioned: true,
    });
    cdk.Tags.of(flinkCodeBucket).add('Environment', 'FLINK_TEST_DO_NOT_DELETE');

    // Grant Kinesis Analytics role access to the S3 bucket
    flinkCodeBucket.grantRead(kinesisAnalyticsRole);

    const applicationSnapshotConfigurationProperty: kinesisanalytics.CfnApplication.ApplicationSnapshotConfigurationProperty = {
      snapshotsEnabled: true,
    };

    // Create Kinesis Analytics Flink application
    const flinkApp = new kinesisanalytics.CfnApplication(this, 'FlinkApplication', {
      applicationName: 'stream-processor-TEST',
      applicationDescription: 'Flink application processing data between Kinesis streams',
      runtimeEnvironment: 'FLINK-1_20',
      serviceExecutionRole: kinesisAnalyticsRole.roleArn,

      applicationConfiguration: {
        applicationSnapshotConfiguration: applicationSnapshotConfigurationProperty,
        flinkApplicationConfiguration: {
          parallelismConfiguration: {
            configurationType: 'CUSTOM',
            parallelism: 1,
            parallelismPerKpu: 1,
          },
          checkpointConfiguration: {
            configurationType: 'CUSTOM',
            checkpointingEnabled: true,
            checkpointInterval: 3600000,
            minPauseBetweenCheckpoints: 300000,
          },
        },

        environmentProperties: {
          propertyGroups: [
            {
              propertyGroupId: 'FlinkApplicationProperties',
              propertyMap: {
                'state.backend': 'rocksdb',
                'state.backend.incremental': 'true',
              },
            },
          ],
        },
      },
      runConfiguration: {
        applicationRestoreConfiguration: {
          applicationRestoreType: 'RESTORE_FROM_LATEST_SNAPSHOT',
        },
        flinkRunConfiguration: {
          allowNonRestoredState: true,
        },
      },
    });
    cdk.Tags.of(flinkApp).add('Environment', 'FLINK_TEST_DO_NOT_DELETE');

    new cdk.CfnOutput(this, 'FlinkArn', {
      value: flinkApp.ref,
      description: 'ARN of the Flink',
    });

    // Add S3 bucket ARN to outputs
    new cdk.CfnOutput(this, 'FlinkCodeBucketName', {
      value: flinkCodeBucket.bucketName,
      description: 'Name of the S3 bucket storing Flink application code',
    });
  }
}
