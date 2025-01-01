import { KinesisClient, PutRecordsCommand } from '@aws-sdk/client-kinesis';

async function putSampleData(streamName: string) {
    if (!streamName) {
        throw new Error('Stream name is required');
    }

    const kinesisClient = new KinesisClient({ region: 'us-east-1' });

    // Create sample records
    const records = Array.from({ length: 10 }, (_, i) => ({
        Data: Buffer.from(JSON.stringify({
            id: i + 1,
            timestamp: new Date().toISOString(),
            value: Math.random() * 100,
            message: `Sample message ${i + 1}`
        })),
        PartitionKey: `partition-${i + 1}`
    }));

    const command = new PutRecordsCommand({
        StreamName: streamName,
        Records: records
    });

    try {
        const response = await kinesisClient.send(command);
        console.log('PutRecords response:', response);
    } catch (error) {
        console.error('Error putting records:', error);
    }
}

const streamName = process.argv[2];
putSampleData(streamName);