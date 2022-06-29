package com.cherri.acs_portal.dto.report;

import org.apache.commons.lang3.builder.ToStringBuilder;
import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.dto.PageQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AbnormalTransactionQueryDTO extends PageQueryDTO {

    private Long issuerBankId;
    private Integer year;
    private Integer month;
    private String merchantId;
    private String merchantName;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }
}
