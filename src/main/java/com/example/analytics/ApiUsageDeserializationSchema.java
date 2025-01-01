package com.example.analytics;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.PojoTypeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;

import static com.example.analytics.NonSerializableBeansProvider.MAPPER;

/** Class responsible for ApiUsage message deserialization. */
public class ApiUsageDeserializationSchema implements DeserializationSchema<ApiUsage> {

    private static final long serialVersionUID = 92_056_892_778L;
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiUsageDeserializationSchema.class);

    /**
     * Deserialized Api Usage message.
     *
     * @param message serialized byte array of Api Usage event.
     * @return ApiUsage object deserialized from byte array
     */
    @Override
    public ApiUsage deserialize(final byte[] message) throws IOException {
        LOGGER.debug("Deserializing API Usage message: {}", new String(message, StandardCharsets.UTF_8));

        try {
            final var apiU = MAPPER.readValue(message, ApiUsage.class);
            LOGGER.info("{}-UUID Deserialized API Usage message: {}", apiU.getProviderId() + apiU.getUserId(), apiU);
            return apiU;
        } catch (JsonParseException jsonExc) {
            // Specific handling for JSON parsing errors
            LOGGER.error("JSON parsing error: Invalid API Usage message: {}",
                    new String(message, StandardCharsets.UTF_8),
                    jsonExc);
            return null; // Return null or handle it differently (e.g., collect in an error stream)
        } catch (IOException ioExc) {
            // Generic handling for other IO exceptions
            LOGGER.error("I/O error deserializing API Usage message: {}", new String(message, StandardCharsets.UTF_8),
                    ioExc);
            throw ioExc; // Rethrow or handle it as needed
        }
    }

    /**
     * Checks if stream has ended. In our case the assumption is that it's never
     * ending.
     *
     * @param nextElement next ApiUsage event
     * @return always false
     */
    @Override
    public boolean isEndOfStream(final ApiUsage nextElement) {
        return false;
    }

    /**
     * Returns information about deserialized type.
     *
     * @return ApiUsage.class type information
     */
    @Override
    public TypeInformation<ApiUsage> getProducedType() {
        return PojoTypeInfo.of(ApiUsage.class);
    }
}
