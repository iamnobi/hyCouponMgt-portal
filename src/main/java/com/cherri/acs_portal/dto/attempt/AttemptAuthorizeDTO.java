package com.cherri.acs_portal.dto.attempt;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class AttemptAuthorizeDTO extends AuditableDTO {

    private Long maxAmount;
    private AuditStatus auditStatus;

    @JsonIgnore
    @Override
    public Long getId() {
        return null;
    }

    @JsonIgnore
    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.ATTEMPT;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }
}
