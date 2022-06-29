package com.cherri.acs_portal.dto.transactionLog;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.dto.PageQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
public class PReqRecordRequestDTO extends PageQueryDTO {

    private static final long serialVersionUID = 1194276610496464423L;

    private String threeDSOperatorId;
    private Long startMillis;
    private Long endMillis;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }
}
