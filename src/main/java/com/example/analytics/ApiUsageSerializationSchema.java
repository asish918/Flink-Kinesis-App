package com.example.analytics;

import org.apache.flink.api.common.serialization.SerializationSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.charset.StandardCharsets;

import static com.example.analytics.NonSerializableBeansProvider.MAPPER;

public class ApiUsageSerializationSchema implements SerializationSchema<ApiUsage> {

    private static final long serialVersionUID = 92_056_892_779L;
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiUsageSerializationSchema.class);

    @Override
    public byte[] serialize(ApiUsage element) {
        try {
            LOGGER.debug("Serializing API Usage message: {}", element);
            byte[] serialized = MAPPER.writeValueAsBytes(element);
            LOGGER.info("Serialized API Usage message: {}", new String(serialized, StandardCharsets.UTF_8));
            return serialized;
        } catch (Exception e) {
            LOGGER.error("Error serializing API Usage message", e);
            return new byte[0];
        }
    }
}
