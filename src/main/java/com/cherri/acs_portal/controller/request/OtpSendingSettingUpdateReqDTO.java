package com.cherri.acs_portal.controller.request;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.validator.validation.HttpUrlValidation;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
@NoArgsConstructor
public class OtpSendingSettingUpdateReqDTO {

    @NotNull(message = "{column.notempty}")
    private Long issuerBankId;

    @NotNull(message = "{column.notempty}")
    private Boolean orgEnable;

    @NotNull(message = "{column.notempty}")
    private Boolean bankEnable;

    @HttpUrlValidation
    private String bankApiUrl;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }
}
