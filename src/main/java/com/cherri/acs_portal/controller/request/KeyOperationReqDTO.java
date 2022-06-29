package com.cherri.acs_portal.controller.request;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.validator.validation.HexKeyValidation;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
@NoArgsConstructor
public class KeyOperationReqDTO {

    private Long id;
    private Long issuerBankId;
    private String cardBrand;
    @HexKeyValidation
    private String keyA;
    @HexKeyValidation
    private String keyB;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }
}
