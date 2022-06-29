package com.cherri.acs_portal.dto.report;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import org.apache.commons.lang3.builder.ToStringBuilder;
import com.cherri.acs_portal.aspect.AuditLogAspect;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportQueryDTO {

    private Long issuerBankId;
    private Long id;

    @Positive
    @NotNull(message = "{column.notempty}")
    private Integer year;

    @Positive
    @NotNull(message = "{column.notempty}")
    private Integer month;

    @Positive
    private Integer day;

    public boolean isMonthlyReport() {
        return null == day;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }
}
