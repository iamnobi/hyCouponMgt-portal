package com.cherri.acs_portal.dto.system;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.enumerator.RenewStatus;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
@NoArgsConstructor
public class SslClientCertificateDto {

    private Long id;
    private String certificateInformation = "";
    private Long applyMillis;
    private Long expireMillis;
    private String issuer;
    private Boolean isExpire;
    private Boolean hasTempKey = false;
    private RenewStatus renewStatus = RenewStatus.NONE;
    private AuditStatus auditStatus;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }
}
