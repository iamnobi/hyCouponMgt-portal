package com.cherri.acs_portal.dto.acs_integrator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/** 向ACS Integrator查詢卡號是否為3DS 1.0版本 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@ToString
@Data
public class TxLogHeaderV1ReqDto extends PageQueryDto {

  /** 銀行代碼 */
  private String bankCode;

  /** 查詢時間範圍 起始時間(ms) */
  private String startMillis;

  /** 查詢時間範圍 結束時間(ms) */
  private String endMillis;
  /** 卡號 */
  private String pan;
  /** 持卡人身份ID */
  private String identityNumber;
  /** 卡別 */
  private String cardType;

  // 進階篩選條件
  /** VERes */
  private String veresTransStatus;
  /** PARes */
  private String paresTransStatus;
}
