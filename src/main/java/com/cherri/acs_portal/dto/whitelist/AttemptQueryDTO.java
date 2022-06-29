package com.cherri.acs_portal.dto.whitelist;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttemptQueryDTO {
  private Long issuerBankId;

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
  }
}
