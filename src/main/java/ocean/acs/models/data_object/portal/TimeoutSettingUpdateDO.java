package ocean.acs.models.data_object.portal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.commons.constant.PortalEnvironmentConstants;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.models.oracle.entity.SystemSetting;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class TimeoutSettingUpdateDO extends AuditableDO {

    @NotBlank(message = "{column.notempty}")
    @Size(min = 1, max = 3, message = "{unaccepted.value}")
    private String value;
    private AuditStatus auditStatus;
    private String user;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, PortalEnvironmentConstants.AUDIT_LOG_STYLE);
     }

    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.SYS_TIMEOUT;
    }

    public static TimeoutSettingUpdateDO valueOf(SystemSetting entity) {
        TimeoutSettingUpdateDO dto = new TimeoutSettingUpdateDO();
        dto.id = entity.getId();
        dto.value = entity.getValue();
        dto.auditStatus = AuditStatus.getStatusBySymbol(entity.getAuditStatus());
        return dto;
    }

    public static TimeoutSettingUpdateDO newInstance(Long id, String value, AuditStatus auditStatus) {
        TimeoutSettingUpdateDO dto = new TimeoutSettingUpdateDO();
        dto.id = id;
        dto.value =value;
        dto.auditStatus = auditStatus;
        return dto;
    }

}
