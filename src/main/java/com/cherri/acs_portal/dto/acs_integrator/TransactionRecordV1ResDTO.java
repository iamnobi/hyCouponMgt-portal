package com.cherri.acs_portal.dto.acs_integrator;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TransactionRecordV1ResDTO extends BaseResDTO {
  private String traderecordId; // id

  private Long transTime; // 驗證時間
  private String pan; // 卡號
  private String amount; // 金額
  private String currency; // 幣別
  private String veres; // VERes, for 風險評估(RBA), 驗證註冊狀態 Y or N，決定於外部傳來的VReq，是否有設定binRange
  private String pares; // PARes, for 風險評估(RBA), 交易狀態 ，決定於外部傳來的PAReq (Y, N, U, A)
}
