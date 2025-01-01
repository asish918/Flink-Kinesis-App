package com.example.analytics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/** Represents APIUsage event item that will be sent to / received from Kinesis Data Stream. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiUsage implements Serializable {

    private String providerId;
    private String userId;
    private Instant requestTime;
    @EqualsAndHashCode.Exclude private ApiTypeEnum apiType;
    private RequestTypeEnum requestType;
    @Builder.Default private List<String> requestFields = new ArrayList<>();
    private boolean deprecated;
    private String resultCode;
    private boolean failedRequest;
    private Boolean oneTimeEvent;

    @JsonIgnore
    public ApiTypeDb getApiTypeDb() {
        return ApiTypeDb.fromApiTypeEnum(apiType);
    }

    @JsonIgnore
    @EqualsAndHashCode.Include
    public String getApiTypeValue() {
        return apiType.getValue();
    }
}
