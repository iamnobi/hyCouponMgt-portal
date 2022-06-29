package com.cherri.acs_portal.dto.report;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;

/** 瀏覽器異常紀錄查詢條件參數 */
@Data
@NoArgsConstructor
public class BrowserErrorLogQueryDTO {

    @JsonProperty("issuerBankId")
    private Long issuerBankId;

    @JsonProperty("date")
    private String date;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }

    public boolean isQueryByDay() {
        // if query by day, the date value is yyyy-MM-dd, if by month, the date value is yyyy-MM
        return date.split("-").length == 3;
    }

}
