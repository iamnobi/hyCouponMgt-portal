package com.cherri.acs_portal.dto.acs_integrator;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotNull;

/** [新增] 3ds 1.0 BinRange Request參數物件 */
@Builder
@Data
public class BinRangeV1SaveReqDTO {

  @NotNull private String bankCode;

  @NotNull private String bankName;

  @NotNull private String cardBrand;

  @NotNull private String cardType;

  @NotNull private String startRange;

  @NotNull private String endRange;

  @NotNull private String operator;

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
  }
}
