package com.cherri.acs_portal.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class QueryTimeRange {
  private ZonedDateTime now;
  private ZonedDateTime startZonedDateTime;
  private ZonedDateTime endZonedDateTime;

  public long getNowMillis() {
    return now.toInstant().toEpochMilli();
  }

  public long getStartMillis() {
    return startZonedDateTime.toInstant().toEpochMilli();
  }

  public long getEndMillis() {
    return endZonedDateTime.toInstant().toEpochMilli();
  }
}
