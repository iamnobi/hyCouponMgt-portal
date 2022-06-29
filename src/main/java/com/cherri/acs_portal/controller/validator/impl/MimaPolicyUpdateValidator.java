package com.cherri.acs_portal.controller.validator.impl;

import com.cherri.acs_portal.controller.request.MimaPolicyUpdateRequest;
import com.cherri.acs_portal.controller.validator.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Mima Policy Update Request Validator
 *
 * @author Alan Chen
 */
@Slf4j
@Service
public class MimaPolicyUpdateValidator implements Validator<MimaPolicyUpdateRequest> {

    @Override
    public boolean isValid(MimaPolicyUpdateRequest data) {
        boolean isValid = data.getId() != null
          && data.getAccountMaxIdleDay() != null && data.getAccountMaxIdleDay() >= 0
          && data.getLoginRetryCount() != null && data.getLoginRetryCount() >= 0
          && data.getMimaFreshInterval() != null && data.getMimaFreshInterval() >= 0
          && data.getMimaHistoryDupCount() != null && data.getMimaHistoryDupCount() >= 0

          && data.getMimaMaxLength() != null && data.getMimaMaxLength() >= 0
          && data.getMimaMinLength() != null && data.getMimaMinLength() >= 0

          && data.getMimaMinNumeric() != null && data.getMimaMinNumeric() >= 0
          && data.getMimaMinSpecialChar() != null && data.getMimaMinSpecialChar() >= 0
          && data.getMimaMinUpperCase() != null && data.getMimaMinUpperCase() >= 0
          && data.getMimaMinLowerCase() != null && data.getMimaMinLowerCase() >= 0
          && data.getMimaAlphabetCount() != null && data.getMimaAlphabetCount() >= 0;
        log.debug("[MimaPolicyUpdateValidator][isValid] isValid: {}", isValid);
        return isValid;
    }
}
