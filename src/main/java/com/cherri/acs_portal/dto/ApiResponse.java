package com.cherri.acs_portal.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;
import ocean.acs.commons.enumerator.ResultStatus;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

  public static final ApiResponse SUCCESS_API_RESPONSE = new ApiResponse();

  @JsonInclude(JsonInclude.Include.USE_DEFAULTS)
  private T data;

  private Integer status;

  private String message;

  /** 只回傳status和message */
  public ApiResponse() {
    this.status = ResultStatus.SUCCESS.getCode();
    this.message = ResultStatus.SUCCESS.name().toLowerCase();
  }

  /**
   * 預設有回傳值的成功狀態
   *
   * @param data
   */
  public ApiResponse(T data) {
    this.data = data;
    this.status = ResultStatus.SUCCESS.getCode();
    this.message = ResultStatus.SUCCESS.name().toLowerCase();
  }

  /**
   * 自定義錯誤訊息
   *
   * @param status ResultStatus.code
   * @param message ResultStatus.name or custom message
   */
  public ApiResponse(ResultStatus status, String message) {
    this.status = status.getCode();
    this.message = message;
  }

  public ApiResponse(ResultStatus status) {
    this.status = status.getCode();
    this.message = status.name();
  }

  public static ApiResponse valueOf(OperationResult result) {
    return new ApiResponse(result.getStatus(), result.getMessage());
  }

  public static <T> ApiResponse valueOf(T data) {
    return new ApiResponse<>(data);
  }

}
