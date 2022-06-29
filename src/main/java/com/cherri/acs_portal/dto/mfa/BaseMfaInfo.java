package com.cherri.acs_portal.dto.mfa;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseMfaInfo {

  /**
   * Bank Code 銀行代碼
   */
  protected Long issuerBankId;
  /**
   * Account Id 使用者帳戶
   */
  protected String account;
}
