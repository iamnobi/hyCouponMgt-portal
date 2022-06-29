package com.cherri.acs_portal.controller.request;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserPageQueryDTO extends BankIdPageQueryDTO {

    private String account;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }
}
