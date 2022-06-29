package com.cherri.acs_portal.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.AuditStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankGroupDto {

    @JsonProperty("groupId")
    private Long groupId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("members")
    private Long members;

    @JsonProperty("auditStatus")
    private AuditStatus auditStatus;

}
