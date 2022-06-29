package com.cherri.acs_portal.dto.acs_integrator;

import com.cherri.acs_portal.dto.attempt.GrantedTransactionLogDTO;
import com.neovisionaries.i18n.CurrencyCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class Get3ds1AttemptGrantedLogResDto extends BaseResDTO {

    /** 授權時間 */
    private Long authMillis;
    /** 授權次數 */
    private Long triesPermitted;
    /** 授權金額 */
    private Double maxMoney;
    /** 交易貨幣 Format:Numeric */
    private String currency = Integer.toString(CurrencyCode.TWD.getNumeric()); // ACS 1.0 use TWD only
    /** 核可人員 */
    private String approved;
    private List<GrantedTransactionLogDTO> transactionLogList;
}
