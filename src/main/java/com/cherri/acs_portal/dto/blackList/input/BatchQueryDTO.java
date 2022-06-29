package com.cherri.acs_portal.dto.blackList.input;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.dto.PageQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ocean.acs.commons.enumerator.TransStatus;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
public class BatchQueryDTO extends PageQueryDTO {
  private Long id;
  private String batchName;
  private Long startTime;
  private Long endTime;
  private String pan;
  private String authStatus;
  private TransStatus transStatus;
  private Long issuerBankId;

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
  }
}
