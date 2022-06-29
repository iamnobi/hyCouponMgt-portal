package com.cherri.acs_portal.dto.mfa;

import com.cherri.acs_portal.controller.request.mfa.MfaCommonRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * MFA Register Request Dto
 *
 * @author Alan Chen
 */
@Getter
@Setter
@NoArgsConstructor
public class MfaRegisterRequestDto extends BaseMfaInfo {

  /**
   * Parse request object to request data transfer object
   *
   * @param request request
   * @return data transfer object
   */
  public static MfaRegisterRequestDto valueOf(MfaCommonRequest request) {
    MfaRegisterRequestDto dto = new MfaRegisterRequestDto();
    dto.setIssuerBankId(request.getIssuerBankId());
    dto.setAccount(request.getAccount());
    return dto;
  }
}
