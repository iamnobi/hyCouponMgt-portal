package com.cherri.acs_portal.dto.acs_integrator;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;

/** 查詢3ds 1.0交易紀錄Request參數物件 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TransactionRecordV1ReqDTO extends PageQueryDto {

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

  private TransactionRecordV1ReqDTO(Builder builder) {
    super(builder.page, builder.pageSize);
    this.bankCode = builder.bankCode;
    if (null != builder.startMillis) {
      this.startMillis = String.valueOf(builder.startMillis);
    }
    if (null != builder.endMillis) {
      this.endMillis = String.valueOf(builder.endMillis);
    }
    this.pan = builder.pan;
    this.identityNumber = builder.identityNumber;
    this.cardType = builder.cardType;
    this.veresTransStatus = builder.veresTransStatus;
    this.paresTransStatus = builder.paresTransStatus;
  }

  public static Builder builder() {
    return new Builder();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
  }

  public static class Builder {
    private Integer page;
    private Integer pageSize;
    private String bankCode;
    private Long startMillis;
    private Long endMillis;
    private String pan;
    private String identityNumber;
    private String cardType;
    private String veresTransStatus;
    private String paresTransStatus;

    public Builder page(Integer page) {
      this.page = page;
      return this;
    }

    public Builder pageSize(Integer pageSize) {
      this.pageSize = pageSize;
      return this;
    }

    public Builder bankCode(String bankCode) {
      this.bankCode = bankCode;
      return this;
    }

    public Builder startMillis(Long startMillis) {
      this.startMillis = startMillis;
      return this;
    }

    public Builder endMillis(Long endMillis) {
      this.endMillis = endMillis;
      return this;
    }

    public Builder pan(String pan) {
      this.pan = pan;
      return this;
    }

    public Builder identityNumber(String identityNumber) {
      this.identityNumber = identityNumber;
      return this;
    }

    public Builder cardType(String cardType) {
      this.cardType = cardType;
      return this;
    }

    public Builder veresTransStatus(String veresTransStatus) {
      this.veresTransStatus = veresTransStatus;
      return this;
    }

    public Builder paresTransStatus(String paresTransStatus) {
      this.paresTransStatus = paresTransStatus;
      return this;
    }

    public TransactionRecordV1ReqDTO build() {
      return new TransactionRecordV1ReqDTO(this);
    }
  }
}
