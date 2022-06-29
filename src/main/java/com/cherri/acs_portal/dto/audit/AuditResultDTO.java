package com.cherri.acs_portal.dto.audit;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotBlank;

@Data
public class AuditResultDTO {
  private Long auditId;
  @NotBlank(message = "{column.notempty}")
  private String auditStatus;

  @NotBlank(message = "{column.notempty}")
  private String functionType;

  private Long issuerBankId;

  private String comment;

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
  }
}
