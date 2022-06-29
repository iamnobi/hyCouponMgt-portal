package com.cherri.acs_portal.dto.usermanagement;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.dto.PageQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AuditLogListRequest extends PageQueryDTO {
  private Long issuerBankId;
  private Long startMillis;
  private Long endMillis;
  private String account;
  private String ip;
  private String methodName;
  private String action;
  private String errorCode;

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
  }
}
