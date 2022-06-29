package com.cherri.acs_portal.dto.acs_integrator;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ocean.acs.commons.enumerator.AuditStatus;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * [更新] 3ds 1.0 BinRange Request參數物件
 */
@Builder
@Data
public class BinRangeV1UpdateAuditStatusReqDTO {

    @NotNull
    private Long id;

    @NotNull
    private AuditStatus auditStatus;

    @NotNull
    private String operator;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }
}
