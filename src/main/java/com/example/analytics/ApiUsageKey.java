package com.example.analytics;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class ApiUsageKey implements Serializable {

    private static final long serialVersionUID = -2551074071952916330L;

    private final String providerId;
    @EqualsAndHashCode.Exclude
    private final ApiTypeDb apiType;

    /**
     * Creates unique key based on api usage.
     *
     * @param apiUsage information about API usage to create a key.
     * @return unique key based on api usage.
     */
    public static ApiUsageKey of(final ApiUsage apiUsage) {
        return new ApiUsageKey(apiUsage.getProviderId(), apiUsage.getApiTypeDb());
    }

    @EqualsAndHashCode.Include
    public String getApiTypeValue() {
        return apiType.getValue();
    }
}
