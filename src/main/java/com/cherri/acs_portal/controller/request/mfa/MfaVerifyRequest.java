package com.cherri.acs_portal.controller.request.mfa;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * MFA驗證請求
 *
 * @author Alan Chen
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MfaVerifyRequest {
  /**
   * TOTP-Code
   */
  @NotNull(message = "{column.notempty}")
  private String authenticationCode;
}
