package com.cherri.acs_portal.controller.request;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
@NoArgsConstructor
public class ChangeSigningCertDto {

    private int version;
    private String cardBrand;
    private Long issuerBankId;
    private Long certId;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }
}
