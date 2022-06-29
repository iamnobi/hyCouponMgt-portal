package com.cherri.acs_portal.controller.response;

import com.cherri.acs_portal.dto.usermanagement.UserAccountDTO;
import com.cherri.acs_portal.dto.usermanagement.UserGroupDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserAccountResponse {

    private String account;

    private String name;

    @JsonProperty("department")
    private String department;

    @JsonProperty("ext")
    private String ext;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("email")
    private String email;

    private List<BankGroupDto> groupList;

    public static UserAccountResponse valueOf(UserAccountDTO userAccountDto) {
        return new UserAccountResponse(
          userAccountDto.getAccount(),
          userAccountDto.getName(),
          userAccountDto.getDepartment(),
          userAccountDto.getExt(),
          userAccountDto.getPhone(),
          userAccountDto.getEmail(),
          userAccountDto.getUserGroupDOList() == null
              ? null
              : userAccountDto.getUserGroupDOList().stream()
                  .map(userGroupDO -> {
                      BankGroupDto bankGroupDto = new BankGroupDto();
                      bankGroupDto.setGroupId(userGroupDO.getId());
                      bankGroupDto.setName(userGroupDO.getName());
                      return bankGroupDto;
                  })
                  .collect(Collectors.toList()));
    }
}
