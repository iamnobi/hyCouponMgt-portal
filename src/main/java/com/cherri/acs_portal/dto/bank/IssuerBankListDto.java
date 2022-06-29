package com.cherri.acs_portal.dto.bank;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.models.data_object.entity.IssuerBankDO;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
public class IssuerBankListDto extends IssuerBankDto {

    private Boolean hasAnyBankAdmin;

    public static IssuerBankListDto valueOf(IssuerBankDO entity) {
        IssuerBankListDto dto = new IssuerBankListDto();

        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setAuditStatus(AuditStatus.getStatusBySymbol(entity.getAuditStatus()));
        dto.setId(entity.getId());
        dto.setIssuerBankId(entity.getId());

        return dto;
    }
}
