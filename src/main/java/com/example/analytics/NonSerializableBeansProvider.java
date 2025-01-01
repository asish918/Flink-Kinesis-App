package com.example.analytics;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.core.json.JsonReadFeature;

public class NonSerializableBeansProvider {

        public static final ObjectMapper MAPPER = JsonMapper.builder()
                        .addModule(new JavaTimeModule())
                        .serializationInclusion(JsonInclude.Include.NON_NULL)
                        .configure(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION, true)
                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                        .configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true)
                        .configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS, true)
                        .build();
}
