package com.cherri.acs_portal.dto;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
public class TimePageQueryDTO extends PageQueryDTO {

    private Long startTime;
    private Long endTime;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }
}
