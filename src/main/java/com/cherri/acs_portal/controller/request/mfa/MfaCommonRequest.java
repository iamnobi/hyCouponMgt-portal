package com.cherri.acs_portal.controller.request.mfa;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * MFA Register Request 多元素驗證註冊請求物件
 *
 * @author Alan Chen
 */
@Getter
@Setter
@NoArgsConstructor
public class MfaCommonRequest {

  /**
   * Bank Code 銀行代碼
   */
  private Long issuerBankId;
  /**
   * Account id 使用者帳號
   */
  private String account;
}
