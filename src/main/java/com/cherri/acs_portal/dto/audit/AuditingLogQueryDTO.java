package com.cherri.acs_portal.dto.audit;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.dto.PageQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = true)
public class AuditingLogQueryDTO extends PageQueryDTO {
  @NotBlank(message = "{column.notempty}")
  private String auditStatus;

  Long startTime = 0L;

  Long endTime = Long.MAX_VALUE;

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
  }
}
