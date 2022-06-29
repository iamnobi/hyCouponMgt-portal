package com.cherri.acs_portal.controller.validator.impl;

import com.cherri.acs_portal.controller.request.ChangeMimaRequest;
import com.cherri.acs_portal.controller.validator.Validator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * ChangeMimaValidator
 *
 * @author Alan Chen
 */
@Service
public class ChangeMimaValidator implements Validator<ChangeMimaRequest> {

    @Override
    public boolean isValid(ChangeMimaRequest data) {
        return data.getIssuerBankId() != null
          && StringUtils.isNotBlank(data.getAccount())
          && data.getOldMima() != null
          && StringUtils.isNotBlank(data.getNewMima())
          && StringUtils.isNotBlank(data.getMimaConfirm())
          && data.getNewMima().equals(data.getMimaConfirm());
    }
}
