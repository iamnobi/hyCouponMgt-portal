package com.cherri.acs_portal.dto.blackList.output;

import com.cherri.acs_portal.controller.request.BlackListMerchantUrlOperationReqDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.models.data_object.entity.BlackListMerchantUrlDO;


@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BlackListMerchantUrlDTO extends AuditableDTO {

    private Long createMillis;
    private String merchantUrl;
    private AuditStatus auditStatus;

    private String creator;
    private String updater;

    public BlackListMerchantUrlDTO(BlackListMerchantUrlOperationReqDTO reqDto, String user) {
        this.id = reqDto.getId();
        this.issuerBankId = reqDto.getIssuerBankId();
        this.createMillis = System.currentTimeMillis();
        this.merchantUrl = reqDto.getMerchantUrl();
        this.creator = user;
    }

    public static BlackListMerchantUrlDTO valueOf(BlackListMerchantUrlDO entity) {
        BlackListMerchantUrlDTO merchantUrlDTO = new BlackListMerchantUrlDTO();
        merchantUrlDTO.setId(entity.getId());
        merchantUrlDTO.setIssuerBankId(entity.getIssuerBankId());
        merchantUrlDTO.setCreateMillis(entity.getCreateMillis());
        merchantUrlDTO.setCreator(entity.getCreator());
        merchantUrlDTO.setMerchantUrl(entity.getUrl());
        merchantUrlDTO.setAuditStatus(AuditStatus.getStatusBySymbol(entity.getAuditStatus()));
        merchantUrlDTO.setUpdater(entity.getUpdater());

        return merchantUrlDTO;
    }

    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.BLACK_LIST_MERCHANT_URL;
    }
}
