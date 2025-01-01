package com.example.analytics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** API Type. */
public enum ApiTypeEnum {
    FOUR_G_GY("4g_gy"),
    FOUR_G_SY("4g_sy"),
    FIVE_G_N40("5g_n40"),
    FIVE_G_N28("5g_n28"),
    GRAPHQL("graphql");

    private final String value;

    ApiTypeEnum(final String value) {
        this.value = value;
    }

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
     * @return {@link ApiTypeEnum}.
     */
    @JsonCreator
    public static ApiTypeEnum fromValue(final String value) {
        for (final ApiTypeEnum reason : ApiTypeEnum.values()) {
            if (reason.value.equals(value)) {
                return reason;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}
