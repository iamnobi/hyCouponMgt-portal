package com.cherri.acs_portal.controller.request;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.dto.PageQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BankIdPageQueryDTO extends PageQueryDTO {

    private Long issuerBankId;

    public void setIssuerBankId(Long reqIssuerBankId, Long sessionIssuerBankId) {
        this.issuerBankId = reqIssuerBankId == null ? sessionIssuerBankId : reqIssuerBankId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }
}
