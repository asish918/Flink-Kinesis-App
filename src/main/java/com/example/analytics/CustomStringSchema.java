package com.example.analytics;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomStringSchema implements DeserializationSchema<String> {

    private static final long serialVersionUID = 1L;
    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public String deserialize(byte[] message) throws IOException {
        if (counter.incrementAndGet() == 5) {
            // throw new RuntimeException("Exception at the 5th entry");
        }
        return new String(message);
    }

    @Override
    public boolean isEndOfStream(String nextElement) {
        return false;
    }

    @Override
    public TypeInformation<String> getProducedType() {
        return Types.STRING;
    }
}