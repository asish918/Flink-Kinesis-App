package com.example.analytics;

import java.io.IOException;

import org.apache.flink.streaming.api.functions.sink.filesystem.PartFileInfo;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.CheckpointRollingPolicy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomRollingPolicy extends CheckpointRollingPolicy<ApiUsage, String> {

    private static final long serialVersionUID = -198321233478123L;
    private final long rollingFileSize;
  
    /**
     * Constructor.
     *
     * @param rollingFileSize max rolling size
     */
    public CustomRollingPolicy(final long rollingFileSize) {
      super();
      this.rollingFileSize = rollingFileSize;
    }
  
    @Override
    public boolean shouldRollOnCheckpoint(final PartFileInfo<String> partFileState) {
      // should be always true because BulkPartWriter do not support persisting
      // https://t.ly/krAq
      return true;
    }
  
    @Override
    public boolean shouldRollOnEvent(
        final PartFileInfo<String> partFileState, final ApiUsage element) {
      try {
        return partFileState.getSize() > rollingFileSize;
      } catch (final IOException exc) {
        log.error("Error getting part file size", exc);
        return false;
      }
    }
  
    @Override
    public boolean shouldRollOnProcessingTime(
        final PartFileInfo<String> partFileState, final long currentTime) {
      return (currentTime - partFileState.getCreationTime()) > 7_200_000;
    }
  }
