package ocean.acs.models.data_object.portal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.commons.constant.PortalEnvironmentConstants;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotBlank;

/** 新增黑名單卡號輸入參數 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ManualPanCreateDO extends AuditableDO {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "{column.notempty}")
    private String realCardNumber;

    private AuditStatus auditStatus;
    private String user;

    @JsonIgnore
    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.BLACK_LIST_PAN;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, PortalEnvironmentConstants.AUDIT_LOG_STYLE);
    }
}
