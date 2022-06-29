package com.cherri.acs_portal.controller.request;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
public class AcquirerBankCreateRequest {

  @NotNull(message = "{column.notempty}")
  private String name;

  @NotNull(message = "{column.notempty}")
  private String threeDSServerRefNumber;

  //00003 allow control asc/3ds oper id by user
  @NotNull(message = "{column.notempty}")
  private String threeDSOperatorId;

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
  }
}
