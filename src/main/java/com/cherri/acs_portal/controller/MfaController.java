package com.cherri.acs_portal.controller;

import com.cherri.acs_portal.controller.request.mfa.MfaCommonRequest;
import com.cherri.acs_portal.controller.request.mfa.MfaVerifyRequest;
import com.cherri.acs_portal.dto.ApiResponse;
import com.cherri.acs_portal.dto.mfa.MfaVerifyRequestDto;
import com.cherri.acs_portal.facade.MfaFacade;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alan Chen
 */
@Slf4j
@RestController
@RequestMapping("/auth/mfa")
public class MfaController extends ContextProvider {

  private final MfaFacade mfaFacade;

  @Autowired
  public MfaController(MfaFacade mfaFacade) {
    this.mfaFacade = mfaFacade;
  }

  /** MFA註冊 */
  //  @PostMapping("/register")
  //  public ApiResponse<Byte[]> register(@RequestBody MfaCommonRequest mfaCommonRequest) {
  //    return mfaFacade.register(mfaCommonRequest);
  //  }

  /** MFA 驗證 */
  @PostMapping("/verification")
  public ApiResponse<Boolean> verification(@Valid @RequestBody MfaVerifyRequest mfaVerifyRequest) {
    MfaVerifyRequestDto mfaVerifyRequestDto = new MfaVerifyRequestDto(
        super.getUserAccount(),
        super.getIssuerBankId(),
        mfaVerifyRequest.getAuthenticationCode());
    return mfaFacade.verify(mfaVerifyRequestDto);
  }

  /**
   * MFA Send simple otp mail
   */
  @PostMapping("/send")
  public ApiResponse<Boolean> sendSimpleOtp() {
    MfaCommonRequest mfaCommonRequest = new MfaCommonRequest();
    mfaCommonRequest.setAccount(super.getUserAccount());
    mfaCommonRequest.setIssuerBankId(super.getIssuerBankId());
    return mfaFacade.send(mfaCommonRequest);
  }

}
