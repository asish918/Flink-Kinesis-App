package com.example.analytics;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.apache.flink.core.io.SimpleVersionedSerializer;
import org.apache.flink.streaming.api.functions.sink.filesystem.BucketAssigner;
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.SimpleVersionedStringSerializer;

/**
 * This class extends {@link BucketAssigner} to set teh bucket ID based on the data partitioning
 * strategy we use.
 */
public class S3BucketAssigner implements BucketAssigner<ApiUsage, String> {

  private static final long serialVersionUID = 1185485800082767642L;

  @Override
  public String getBucketId(final ApiUsage event, final Context context) {
    final ZonedDateTime zonedDateTime =
        ZonedDateTime.ofInstant(event.getRequestTime(), ZoneId.of("UTC"));
    return String.format(
        "provider=%s/apitype=%s/year=%d/month=%d/day=%d/",
        event.getProviderId(),
        event.getApiType(),
        zonedDateTime.getYear(),
        zonedDateTime.getMonthValue(),
        zonedDateTime.getDayOfMonth());
  }

  @Override
  public SimpleVersionedSerializer<String> getSerializer() {
    return SimpleVersionedStringSerializer.INSTANCE;
  }
}

