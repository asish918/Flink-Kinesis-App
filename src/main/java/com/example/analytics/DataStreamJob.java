package com.example.analytics;

import org.apache.commons.logging.LogFactory;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.connector.source.util.ratelimit.RateLimiterStrategy;
import org.apache.flink.configuration.CheckpointingOptions;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.ExternalizedCheckpointRetention;
import org.apache.flink.configuration.MemorySize;
import org.apache.flink.configuration.RestartStrategyOptions;
import org.apache.flink.connector.kinesis.sink.KinesisStreamsSink;
import org.apache.flink.connector.kinesis.sink.KinesisStreamsSinkBuilder;
import org.apache.flink.connector.kinesis.source.KinesisStreamsSource;
import org.apache.flink.connector.kinesis.source.config.KinesisSourceConfigOptions;
import org.apache.flink.connector.kinesis.source.enumerator.assigner.ShardAssignerFactory;
import org.apache.flink.core.execution.CheckpointingMode;
import org.apache.flink.core.fs.Path;
import org.apache.flink.orc.writer.OrcBulkWriterFactory;
import org.apache.flink.shaded.guava31.com.google.common.collect.Maps;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.LocalStreamEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.OnCheckpointRollingPolicy;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kinesis.FlinkKinesisConsumer;
import org.apache.flink.streaming.connectors.kinesis.config.AWSConfigConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.flink.connector.file.sink.FileSink;

import com.amazonaws.services.kinesisanalytics.runtime.KinesisAnalyticsRuntime;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Properties;

public class DataStreamJob {
        private static final Logger LOGGER = LoggerFactory.getLogger(DataStreamJob.class);

        // Name of the local JSON resource with the application properties in the same
        // format as they are received from the Amazon Managed Service for Apache Flink
        // runtime
        private static final String LOCAL_APPLICATION_PROPERTIES_RESOURCE = "flink-application-properties-dev.json";

        private static boolean isLocal(StreamExecutionEnvironment env) {
                return env instanceof LocalStreamEnvironment;
        }

        /**
         * Load application properties from Amazon Managed Service for Apache Flink
         * runtime or from a local resource, when the environment is local
         */
        private static Map<String, Properties> loadApplicationProperties(StreamExecutionEnvironment env)
                        throws IOException {
                if (isLocal(env)) {
                        LOGGER.info("Loading application properties from '{}'", LOCAL_APPLICATION_PROPERTIES_RESOURCE);
                        return KinesisAnalyticsRuntime.getApplicationProperties(DataStreamJob.class.getClassLoader()
                                        .getResource(LOCAL_APPLICATION_PROPERTIES_RESOURCE).getPath());
                } else {
                        LOGGER.info("Loading application properties from Amazon Managed Service for Apache Flink");
                        return KinesisAnalyticsRuntime.getApplicationProperties();
                }
        }

        private static KinesisStreamsSource<ApiUsage> createKinesisSource() {

                Properties inputProperties = new Properties();
                inputProperties.setProperty(AWSConfigConstants.AWS_CREDENTIALS_PROVIDER,
                "ASSUME_ROLE");
                inputProperties.setProperty(AWSConfigConstants.AWS_ROLE_ARN,
                "arn:aws:iam::266643203619:role/KD-read-access-kinesis-data-stream-role");
                inputProperties.setProperty(AWSConfigConstants.AWS_ROLE_SESSION_NAME,
                "flink-kinesis-session");
                inputProperties.setProperty(AWSConfigConstants.AWS_REGION, "us-east-1");

                inputProperties.setProperty(KinesisSourceConfigOptions.STREAM_INITIAL_POSITION.key(),
                                KinesisSourceConfigOptions.InitialPosition.AT_TIMESTAMP.name());

                // Change the date according to your preference of pulling data from the stream
                inputProperties.setProperty(KinesisSourceConfigOptions.STREAM_INITIAL_TIMESTAMP.key(),
                                "2024-12-06T00:00:00.480-00:00");
                // inputProperties.setProperty(KinesisSourceConfigOptions.STREAM_INITIAL_POSITION.key(),
                // KinesisSourceConfigOptions.InitialPosition.TRIM_HORIZON.name());

                return KinesisStreamsSource.<ApiUsage>builder()
                                .setStreamArn("arn:aws:kinesis:us-east-1:266643203619:stream/api-usage-stats-stream-produseast1")
                                .setSourceConfig(Configuration.fromMap(Maps.fromProperties(inputProperties)))

                                .setDeserializationSchema(new ApiUsageDeserializationSchema())
                                // .setDeserializationSchema(new CustomStringSchema())
                                .build();
        }

        private static KinesisStreamsSink<ApiUsage> createKinesisSink() {
                Properties outputProperties = new Properties();
                outputProperties.setProperty(AWSConfigConstants.AWS_REGION, "us-east-1");

                // Change the arn of the stream according to your environment
                return KinesisStreamsSink.<ApiUsage>builder()
                                .setStreamArn("arn:aws:kinesis:us-east-1:366471124629:stream/api-usage-stats-stream-fltestuseast1")
                                .setKinesisClientProperties(outputProperties)
                                .setSerializationSchema(new ApiUsageSerializationSchema())
                                .setPartitionKeyGenerator(element -> String.valueOf(element.hashCode()))
                                .build();
        }

        public static void main(final String[] args) throws Exception {
                // set up the streaming execution environment
                Configuration config = new Configuration();
                config.set(RestartStrategyOptions.RESTART_STRATEGY, "fixed-delay");
                config.set(RestartStrategyOptions.RESTART_STRATEGY_FIXED_DELAY_ATTEMPTS, 3);
                // // number of restart
                // // attempts
                config.set(RestartStrategyOptions.RESTART_STRATEGY_FIXED_DELAY_DELAY,
                                Duration.ofSeconds(10)); // delay
                config.set(CheckpointingOptions.ENABLE_CHECKPOINTS_AFTER_TASKS_FINISH, true);
                config.set(CheckpointingOptions.CHECKPOINT_STORAGE, "filesystem");
                config.set(CheckpointingOptions.CHECKPOINTS_DIRECTORY,
                                "file:///Users/asishmahapatra/Downloads/analytics");
                // "file:///Users/asishmahapatra/Downloads/flink-1.20.0");
                final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(config);
                env.enableCheckpointing(10000);
                env.getCheckpointConfig().setCheckpointingConsistencyMode(CheckpointingMode.EXACTLY_ONCE);
                env.getCheckpointConfig().setCheckpointTimeout(60000);
                env.getCheckpointConfig().setMinPauseBetweenCheckpoints(500);
                env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
                env.getCheckpointConfig()
                                .setExternalizedCheckpointRetention(
                                                ExternalizedCheckpointRetention.RETAIN_ON_CANCELLATION);

                final var watermarkStrategy = WatermarkStrategy
                                .<ApiUsage>forBoundedOutOfOrderness(Duration.ofSeconds(60))
                                .withTimestampAssigner(
                                                (apiUsage, timestamp) -> apiUsage.getRequestTime().toEpochMilli());

                final var windowAssigner = TumblingEventTimeWindows.of(Duration.ofSeconds(60));

                KinesisStreamsSource<ApiUsage> source = createKinesisSource();
                DataStream<ApiUsage> input = env.fromSource(source,
                                watermarkStrategy,
                                "Kinesis source",
                                TypeInformation.of(ApiUsage.class))
                                .uid("source-id")
                                .name("Kinesis Source");

                input.keyBy(ApiUsageKey::of).window(windowAssigner).reduce((prev, next) -> next);

                final OrcBulkWriterFactory<ApiUsage> writerFactory = new OrcBulkWriterFactory<>(
                                new ApiUsageVectorizer());

                // // Set up the rolling policy
                final var rollingPolicy = new CustomRollingPolicy(200 * 1024 * 1024);

                // Set Sink as local File System
                final FileSink<ApiUsage> sink = FileSink
                                .forBulkFormat(new Path("./data"), writerFactory)
                                // .forRowFormat(new Path("./data"), new SimpleStringEncoder<String>())
                                // .withRollingPolicy(rollingPolicy)
                                .withBucketAssigner(new S3BucketAssigner())
                                .build();

                // Set Sink as Kinesis Stream 
                // final KinesisStreamsSink<String> sink = createKinesisSink();
                input.sinkTo(sink);

                env.execute("Flink Kinesis from Prod to Dev");
        }
}
