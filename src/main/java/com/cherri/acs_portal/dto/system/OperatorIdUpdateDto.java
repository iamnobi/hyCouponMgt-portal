package com.cherri.acs_portal.dto.system;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import org.apache.commons.lang3.builder.ToStringBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class OperatorIdUpdateDto extends AuditableDTO {

    private List<SystemSettingDTO> valueList;
    private AuditStatus auditStatus;
    private String user;

    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.SYS_ACS_OPERATOR_ID;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }
}
