package com.cherri.acs_portal.controller.request;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
public class UserProfileUpdateRequest {

    @NotEmpty(message = "{column.notempty}")
    private String name;

    private String department;
    private String phone;
    private String ext;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }
}
