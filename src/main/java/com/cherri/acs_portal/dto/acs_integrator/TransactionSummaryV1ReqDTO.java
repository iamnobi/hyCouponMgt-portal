package com.cherri.acs_portal.dto.acs_integrator;

import lombok.Builder;
import lombok.Data;

/** 交易紀錄查詢摘要Request參數 */
@Data
@Builder
public class TransactionSummaryV1ReqDTO {

  /** 銀行代碼 */
  private String bankCode;

  /** T_AUTHTRANSACTIONMESSAGE.traderecordId */
  private String traderecordId;
}
