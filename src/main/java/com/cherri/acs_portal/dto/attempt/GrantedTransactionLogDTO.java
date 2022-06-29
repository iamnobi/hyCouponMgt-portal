package com.cherri.acs_portal.dto.attempt;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.models.data_object.portal.GrantedTransactionLogDO;
import org.apache.commons.lang3.builder.ToStringBuilder;

/** 人工彈性授權交易使用紀錄 */
@Data
@NoArgsConstructor
public class GrantedTransactionLogDTO {

    /** 交易執行時間 */
    private Long executeMillis;
    /** 交易金額 */
    private Double amount;
    /** 交易貨幣 Format:Numeric */
    private String currency;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }

    public static GrantedTransactionLogDTO valueOf(
      GrantedTransactionLogDO grantedTransactionLogDO) {
        GrantedTransactionLogDTO grantedTransactionLogDTO = new GrantedTransactionLogDTO();
        grantedTransactionLogDTO.setExecuteMillis(grantedTransactionLogDO.getExecuteMillis());
        grantedTransactionLogDTO.setAmount(grantedTransactionLogDO.getAmount());
        grantedTransactionLogDTO.setCurrency(grantedTransactionLogDO.getCurrency());
        return grantedTransactionLogDTO;
    }
}
