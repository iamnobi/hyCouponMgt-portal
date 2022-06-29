package com.cherri.acs_portal.controller.request;

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

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class BlackListMerchantUrlOperationReqDTO extends AuditableDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "{column.notempty}")
    private String merchantUrl;

    @JsonIgnore
    private AuditStatus auditStatus;

    private String user;

    @JsonIgnore
    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.UNKNOWN;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }
}
