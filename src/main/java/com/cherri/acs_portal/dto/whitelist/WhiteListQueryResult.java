package com.cherri.acs_portal.dto.whitelist;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.models.data_object.portal.WhiteListQueryResultDO;


@Data
@NoArgsConstructor
public class WhiteListQueryResult {

    @JsonIgnore
    private Long panId;
    private Long id;
    private String cardBrand;
    private String cardNumber;
    @JsonIgnore
    private String cardNumberHash;
    @JsonIgnore
    private String cardNumberEn;
    private Long createMillis;
    private AuditStatus auditStatus;

    public static WhiteListQueryResult valueOf(WhiteListQueryResultDO whiteListQueryResultDO) {

        WhiteListQueryResult whiteListQueryResult = new WhiteListQueryResult();
        whiteListQueryResult.setId(whiteListQueryResultDO.getId());
        whiteListQueryResult.setPanId(whiteListQueryResultDO.getPanId());
        whiteListQueryResult.setCardBrand(whiteListQueryResultDO.getCardBrand());
        whiteListQueryResult.setCardNumber(whiteListQueryResultDO.getCardNumber());
        whiteListQueryResult.setCardNumberHash(whiteListQueryResultDO.getCardNumberHash());
        whiteListQueryResult.setCardNumberEn(whiteListQueryResultDO.getCardNumberEn());
        whiteListQueryResult.setCreateMillis(whiteListQueryResultDO.getCreateMillis());
        whiteListQueryResult.setAuditStatus(whiteListQueryResultDO.getAuditStatus());
        return whiteListQueryResult;
    }
}
