package com.cherri.acs_portal.controller.request;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 交易紀錄查詢摘要Request參數
 */
@Data
@NoArgsConstructor
public class TxSummaryReqDTO {

    /**
     * 銀行ID
     */
    @NotNull(message = "{column.notempty}")
    private Long issuerBankId;

    /**
     * ACS 1.0 transaction id
     */
    @NotBlank(message = "{column.notempty}")
    private String transactionLogId;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }
}
