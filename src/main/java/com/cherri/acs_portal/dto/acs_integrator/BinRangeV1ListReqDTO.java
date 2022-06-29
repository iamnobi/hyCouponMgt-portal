package com.cherri.acs_portal.dto.acs_integrator;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;

/** [查詢] 3ds 1.0 BinRange Request參數物件 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BinRangeV1ListReqDTO extends PageQueryDto {

  /** 銀行代碼 */
  private String bankCode;

  private BinRangeV1ListReqDTO(Builder builder) {
    super(builder.page, builder.pageSize);
    this.bankCode = builder.bankCode;
  }

  public static Builder builder() {
    return new Builder();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
  }

  public static class Builder {
    private String bankCode;
    private Integer page;
    private Integer pageSize;

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

    public BinRangeV1ListReqDTO build() {
      return new BinRangeV1ListReqDTO(this);
    }
  }
}
