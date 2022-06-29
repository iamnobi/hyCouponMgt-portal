package com.cherri.acs_portal.controller.validator.impl;

import com.cherri.acs_portal.controller.request.MimaPolicyCreateRequest;
import com.cherri.acs_portal.controller.validator.Validator;
import org.springframework.stereotype.Service;

@Service
public class MimaPolicyCreateValidator implements Validator<MimaPolicyCreateRequest> {

    @Override
    public boolean isValid(MimaPolicyCreateRequest data) {
        return data.getIssuerBankId() != null
          && data.getMimaFreshInterval() != null
          && data.getAccountMaxIdleDay() != null
          && data.getMimaMaxLength() != null
          && data.getMimaMinLength() != null
          && data.getMimaAlphabetCount() != null
          && data.getMimaMinLowerCase() != null
          && data.getMimaMinUpperCase() != null
          && data.getMimaMinNumeric() != null
          && data.getMimaMinSpecialChar() != null
          && data.getMimaHistoryDupCount() != null;
    }
}
