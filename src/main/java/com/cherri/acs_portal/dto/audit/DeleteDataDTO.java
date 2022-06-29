package com.cherri.acs_portal.dto.audit;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class DeleteDataDTO extends AuditableDTO {
  private String user;

  @JsonIgnore
  private AuditStatus auditStatus;

  private Integer version;

  public DeleteDataDTO(Long id, Long issuerBankId, AuditStatus auditStatus, String user, Integer version) {
    this.id = id;
    this.issuerBankId = issuerBankId;
    this.auditStatus = auditStatus;
    this.user = user;
    this.version = version;
  }

  @Override
  public AuditFunctionType getFunctionType() {
    return AuditFunctionType.UNKNOWN;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
  }
}
