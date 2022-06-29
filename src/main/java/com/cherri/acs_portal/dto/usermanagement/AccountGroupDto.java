package com.cherri.acs_portal.dto.usermanagement;

import com.cherri.acs_portal.dto.audit.AuditableDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.models.data_object.entity.AccountGroupDO;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AccountGroupDto extends AuditableDTO {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long accountId;
    private String accountName;
    private Long groupId;
    private String groupName;
    private AuditStatus auditStatus;
    private String creator;
    private Long createMillis;

    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.ACCOUNT_GROUP;
    }

    public static AccountGroupDto valueOf(AccountGroupDO ag) {
        AccountGroupDto dto = new AccountGroupDto();
        dto.setId(ag.getId());
        dto.setAccountId(ag.getAccountId());
        dto.setGroupId(ag.getGroupId());
        dto.setAuditStatus(ag.getAuditStatus());
        dto.setCreator(ag.getCreator());
        dto.setCreateMillis(ag.getCreateMillis());
        return dto;
    }

}
