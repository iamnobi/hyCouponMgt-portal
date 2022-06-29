package com.cherri.acs_portal.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ocean.acs.commons.enumerator.ResultStatus;

@Data
@ToString
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
public class ApiPageResponse<T> extends PagingResultDTO<T> {
  private static final ApiPageResponse INSTANCE =
      new ApiPageResponse(ResultStatus.SUCCESS, ResultStatus.SUCCESS.name());

  private Integer status;

  private String message;

  /**
   * 預設有回傳值的成功狀態
   *
   * @param pagingResult
   */
  public ApiPageResponse(PagingResultDTO<T> pagingResult) {
    setData(pagingResult.getData());
    setCurrentPage(pagingResult.getCurrentPage());
    setTotal(pagingResult.getTotal());
    setTotalPages(pagingResult.getTotalPages());

    this.status = ResultStatus.SUCCESS.getCode();
    this.message = "success";
  }
  /**
   * 自定義錯誤訊息
   *
   * @param status
   * @param message
   */
  public ApiPageResponse(ResultStatus status, String message) {
    this.status = status.getCode();
    this.message = message;
  }

  public static ApiPageResponse empty() {
    return INSTANCE;
  }

  @SuppressWarnings("unchecked")
  public static ApiPageResponse valueOf(PagingResultDTO pagingResult) {
    ApiPageResponse response = new ApiPageResponse();
    if (pagingResult.getTotal() < 1) {
      response.setStatus(ResultStatus.NO_SUCH_DATA.getCode());
      response.setMessage(ResultStatus.NO_SUCH_DATA.name());
      return response;
    }
    response.setStatus(ResultStatus.SUCCESS.getCode());
    response.setMessage(ResultStatus.SUCCESS.name());
    response.setCurrentPage(pagingResult.getCurrentPage());
    response.setTotal(pagingResult.getTotal());
    response.setTotalPages(pagingResult.getTotalPages());
    response.setData(pagingResult.getData());
    return response;
  }
}
