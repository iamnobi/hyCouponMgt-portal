package com.cherri.acs_portal.dto.blackList.input;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 新增黑名單卡號輸入參數
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ManualPanCreateDTO extends AuditableDTO implements Serializable {

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
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }
}
