package com.cherri.acs_portal.dto.transactionLog;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.dto.PageQueryDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;

/** 查詢交易紀錄Request參數物件 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TxLogHeaderQueryDTO extends PageQueryDTO {

  /** 銀行ID */
  private Long issuerBankId;
  /** 查詢時間範圍 起始時間 */
  private Long startMillis;
  /** 查詢時間範圍 結束時間 */
  private Long endMillis;
  /** 卡號 */
  private String pan;
  /** 持卡人身份ID */
  private String identityNumber;
  /** 3DS Operator ID 00003 add Criteria 3ds operID in transaction inquiry */
  private String threeDSOperatorId;
  /** 卡別*/
  @JsonProperty("cardBrand")
  private String cardType;

  // 進階篩選條件
  /** ARes */
  private String ares;
  /** RRes */
  private String rres;
  /** 3DS Server 交易ID*/
  private String threeDSServerTransID;
  /** ACS 產生的交易ID */
  private String acsTransID;
  /** DS 產生的交易ID*/
  private String dsTransID;
  /** 3DS SDK 產生的交易ID */
  private String sdkTransID;

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
  }
}
