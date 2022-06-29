package com.cherri.acs_portal.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.models.data_object.entity.BlackListMerchantUrlDO;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BlackListMerchantUrlResDTO {

    private Long id;
    private Long issuerBankId;
    private Long createMillis;
    private String merchantUrl;
    private AuditStatus auditStatus;

    public static BlackListMerchantUrlResDTO valueOf(BlackListMerchantUrlDO entity) {
        return new BlackListMerchantUrlResDTO(
          entity.getId(),
          entity.getIssuerBankId(),
          entity.getCreateMillis(),
          entity.getUrl(),
          AuditStatus.getStatusBySymbol(entity.getAuditStatus()));
    }
}
