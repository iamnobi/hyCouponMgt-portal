package ocean.acs.models.data_object.portal;

import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.commons.constant.PortalEnvironmentConstants;
import org.apache.commons.lang3.builder.ToStringBuilder;

/** 人工彈性授權交易使用紀錄 */
@Data
@NoArgsConstructor
public class GrantedTransactionLogDO {

    /** 交易執行時間 */
    private Long executeMillis;
    /** 交易金額 */
    private Double amount;
    /** 交易貨幣 Format:Numeric */
    private String currency;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, PortalEnvironmentConstants.AUDIT_LOG_STYLE);
    }
}
