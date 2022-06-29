package com.cherri.acs_portal.controller.request;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
@NoArgsConstructor
public class GenCsrRequestDto {

    private int version;
    private String cardBrand;
    private String country = "";
    private String organization = "";
    private String organizationUnit = "";
    private String commonName = "";
    private String locality = "";
    private String stateOrProvince = "";
    private Long issuerBankId;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }
}
