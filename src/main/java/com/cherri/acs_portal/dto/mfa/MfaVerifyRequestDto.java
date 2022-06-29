package com.cherri.acs_portal.dto.mfa;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MfaVerifyRequestDto extends BaseMfaInfo {

  private String authenticationCode;

  public MfaVerifyRequestDto(String account, long issuerBankId, String authenticationCode) {
    this.account = account;
    this.issuerBankId = issuerBankId;
    this.authenticationCode = authenticationCode;
  }
}
