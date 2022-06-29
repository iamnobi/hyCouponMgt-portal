package com.cherri.acs_portal.controller.response;

import com.cherri.acs_portal.dto.usermanagement.UserAccountDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ocean.acs.commons.enumerator.AuditStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserAccountGroupResponse {

    private Long id;
    private String account;
    private String username;
    private String group;
    private AuditStatus auditStatus;

    public static UserAccountGroupResponse valueOf(UserAccountDTO userAccountDto) {
        return new UserAccountGroupResponse(userAccountDto.getId(), userAccountDto.getAccount(),
          userAccountDto.getName(),
          userAccountDto.getGroup(), userAccountDto.getAuditStatus());
    }

}
