package com.cherri.acs_portal.controller.response;

import lombok.Data;

@Data
public class BatchResultDTO {
  private Long addedCount;
  private Long deletedCount;
  private Long modifiedCount;
}
