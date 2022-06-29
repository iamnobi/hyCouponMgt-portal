package com.cherri.acs_portal.dto.system;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.models.data_object.entity.SystemSettingDO;
import org.apache.commons.lang3.builder.ToStringBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class TimeoutSettingUpdateDTO extends AuditableDTO {

    @NotBlank(message = "{column.notempty}")
    @Size(min = 1, max = 3, message = "{unaccepted.value}")
    private String value;

    private AuditStatus auditStatus;
    private String user;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }

    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.SYS_TIMEOUT;
    }

    public static TimeoutSettingUpdateDTO valueOf(SystemSettingDO entity) {
        TimeoutSettingUpdateDTO dto = new TimeoutSettingUpdateDTO();
        dto.id = entity.getId();
        dto.value = entity.getValue();
        dto.auditStatus = AuditStatus.getStatusBySymbol(entity.getAuditStatus());
        return dto;
    }
}
