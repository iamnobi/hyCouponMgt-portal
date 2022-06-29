package com.cherri.acs_portal.controller.request;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
public class LoginForgetMimaRequest {

  @NotNull(message = "{column.notempty}")
  private String bankCode;

  @NotEmpty(message = "{column.notempty}")
  private String account;

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
  }
}
