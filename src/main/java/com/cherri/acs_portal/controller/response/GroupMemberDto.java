package com.cherri.acs_portal.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.models.data_object.portal.UserAccountDO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupMemberDto {

    @JsonProperty("account")
    private String account;

    @JsonProperty("name")
    private String name;

    @JsonProperty("auditStatus")
    private AuditStatus auditStatus;

    @Override
    public String toString() {
        return String
          .format("account=%s|name=%s|auditStatus=%s", this.account, this.name, this.auditStatus);
    }

    public static GroupMemberDto valudOf(UserAccountDO ua) {
        return new GroupMemberDto(ua.getAccount(), ua.getUsername(),
          AuditStatus.getStatusBySymbol(ua.getAuditStatus()));
    }

}
