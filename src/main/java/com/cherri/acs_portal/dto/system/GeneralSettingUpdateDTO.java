package com.cherri.acs_portal.dto.system;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Range;

@Data
@NoArgsConstructor
public class GeneralSettingUpdateDTO {

    @Range(min=60, max=1440, message = "{unaccepted.value}")
    private Integer preqResendInterval;

    @Range(min=1, max=30, message = "{unaccepted.value}")
    private Integer areqConnectionTimeout;

    @Range(min=1, max=30, message = "{unaccepted.value}")
    private Integer rreqConnectionTimeout;

    @Range(min=1, max=60, message = "{unaccepted.value}")
    private Integer areqReadTimeout;

    private String user;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }
}
