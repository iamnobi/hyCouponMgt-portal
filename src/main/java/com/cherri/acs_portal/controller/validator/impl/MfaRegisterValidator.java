package com.cherri.acs_portal.controller.validator.impl;

import com.cherri.acs_portal.controller.request.mfa.MfaCommonRequest;
import com.cherri.acs_portal.controller.validator.Validator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * MFA Register validator implement 多元素驗證功能-註冊請求參數檢驗
 *
 * @author Alan Chen
 */
@Slf4j
@Service
public class MfaRegisterValidator implements Validator<MfaCommonRequest> {

  /**
   * Valid value
   *
   * @param data will check data
   * @return Valid Is pass
   */
  @Override
  public boolean isValid(MfaCommonRequest data) {
    boolean isValid = data.getIssuerBankId() != null && StringUtils.isNotBlank(data.getAccount());
    log.debug("[MfaRegisterValidator] [isValid] isValid: {}", isValid);
    return isValid;
  }
}
