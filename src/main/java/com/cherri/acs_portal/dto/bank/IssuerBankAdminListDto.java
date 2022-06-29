package com.cherri.acs_portal.dto.bank;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.models.data_object.portal.UserAccountDO;
import ocean.acs.models.oracle.entity.UserAccount;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
public class IssuerBankAdminListDto extends IssuerBankAdminDto {

    public static IssuerBankAdminListDto valueOf(UserAccount userAccount) {
        IssuerBankAdminListDto dto = new IssuerBankAdminListDto();
        dto.setId(userAccount.getId());
        dto.setAccount(userAccount.getAccount());
        dto.setBankId(userAccount.getIssuerBankId());
        dto.setUserName(userAccount.getUsername());
        dto.setDepartment(userAccount.getDepartment());
        dto.setEmail(userAccount.getEmail());
        dto.setPhone(userAccount.getPhone());
        dto.setExt(userAccount.getExt());
        dto.setAuditStatus(AuditStatus.getStatusBySymbol(userAccount.getAuditStatus()));
        return dto;
    }

    public static IssuerBankAdminListDto valueOf(UserAccountDO userAccount) {
        IssuerBankAdminListDto dto = new IssuerBankAdminListDto();
        dto.setId(userAccount.getId());
        dto.setAccount(userAccount.getAccount());
        dto.setBankId(userAccount.getIssuerBankId());
        dto.setUserName(userAccount.getUsername());
        dto.setDepartment(userAccount.getDepartment());
        dto.setEmail(userAccount.getEmail());
        dto.setPhone(userAccount.getPhone());
        dto.setExt(userAccount.getExt());
        dto.setAuditStatus(AuditStatus.getStatusBySymbol(userAccount.getAuditStatus()));
        return dto;
    }


}
