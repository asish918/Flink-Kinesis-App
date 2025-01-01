package com.example.analytics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/** API type for usage limits. */
@RequiredArgsConstructor
@Getter
public enum ApiTypeDb {
    /** Diameter Adapter (4G) Gy interface. */
    DA_GY("DA_GY", ApiTypeEnum.FOUR_G_GY),
    /** Diameter Adapter (4G) Sy interface. */
    DA_SY("DA_SY", ApiTypeEnum.FOUR_G_SY),
    /** Engine (5G) n40 interface. */
    ENGINE_N40("ENGINE_N40", ApiTypeEnum.FIVE_G_N40),
    /** Engine (5G) n28 interface. */
    ENGINE_N28("ENGINE_N28", ApiTypeEnum.FIVE_G_N28),
    /** AppSync (GraphQL) API. */
    APPSYNC("APPSYNC", ApiTypeEnum.GRAPHQL);

    private final String value;
    private final ApiTypeEnum apiTypeEnumMapping;

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    /**
     * Create from value.
     *
     * @param value the string value.
     * @return {@link ApiTypeDb}.
     */
    @JsonCreator
    public static ApiTypeDb fromValue(final String value) {
        for (final ApiTypeDb actual : ApiTypeDb.values()) {
            if (actual.value.equals(value)) {
                return actual;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    /**
     * Create from ApiTypeEnum.
     *
     * @param value the ApiTypeEnum.
     * @return {@link ApiTypeDb}.
     */
    public static ApiTypeDb fromApiTypeEnum(final ApiTypeEnum value) {
        for (final ApiTypeDb actual : ApiTypeDb.values()) {
            if (actual.apiTypeEnumMapping.equals(value)) {
                return actual;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}
