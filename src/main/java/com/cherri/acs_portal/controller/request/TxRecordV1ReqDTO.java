package com.cherri.acs_portal.controller.request;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.dto.PageQueryDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 查詢3ds 1.0交易紀錄Request參數物件
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TxRecordV1ReqDTO extends PageQueryDTO {

    /**
     * 銀行ID
     */
    @NotNull(message = "{column.notempty}")
    private Long issuerBankId;
    /**
     * 查詢時間範圍 起始時間
     */
    private Long startMillis;
    /**
     * 查詢時間範圍 結束時間
     */
    private Long endMillis;
    /**
     * 卡號
     */
    private String pan;
    /**
     * 持卡人身份ID
     */
    private String identityNumber;
    /**
     * 卡別
     */
    @JsonProperty("cardBrand")
    private String cardType;

    /**
     * PARes Transaction Status
     */
    private String paresTransStatus;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }
}
