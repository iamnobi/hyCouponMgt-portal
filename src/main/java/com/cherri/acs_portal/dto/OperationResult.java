package com.cherri.acs_portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ocean.acs.commons.enumerator.ResultStatus;

@Data
@AllArgsConstructor
public class OperationResult {

  private static final OperationResult SUCCESS =
      new OperationResult(ResultStatus.SUCCESS, "success");

  private ResultStatus status;
  private String message;

  public boolean isSuccess() {
    return ResultStatus.SUCCESS.equals(status);
  }

  public static OperationResult getSuccessInstance() {
    return OperationResult.SUCCESS;
  }
}
