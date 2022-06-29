package com.cherri.acs_portal.dto.usermanagement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.models.data_object.portal.UserAccountDO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String account;
    private AuditStatus auditStatus;

    public static UserDto valueOf(UserAccountDO userAccount) {
        return new UserDto(userAccount.getId(), userAccount.getAccount(),
          AuditStatus.getStatusBySymbol(userAccount.getAuditStatus()));
    }

    @Override
    public String toString() {
        return "id=" + id + "|account='" + account;
    }
}
