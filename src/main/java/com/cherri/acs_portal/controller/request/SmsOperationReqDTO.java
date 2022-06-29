package com.cherri.acs_portal.controller.request;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Range;

@Data
@NoArgsConstructor
public class SmsOperationReqDTO {

    private static final int ONE_MINUTE_MILLIS = 60 * 1000;
    private static final int NINE_MINUTE_MILLIS = 9 * ONE_MINUTE_MILLIS;

    @NotNull(message = "{column.notempty}")
    private Long id;

    @NotNull(message = "{column.notempty}")
    private Long issuerBankId;

    @NotNull(message = "{column.notempty}")
    @Range(min = ONE_MINUTE_MILLIS, max = NINE_MINUTE_MILLIS, message = "{unaccepted.value}")
    private Long expireMillis;

    @NotBlank(message = "{column.notempty}")
    private String verifyMessage;

    @NotBlank(message = "{column.notempty}")
    private String exceedLimitMessage;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }
}
