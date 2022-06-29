package com.cherri.acs_portal.facade;

import com.cherri.acs_portal.config.ClassicRbaProperties;
import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.cherri.acs_portal.controller.response.UiConfigDTO;
import com.cherri.acs_portal.service.CurrencyService;
import ocean.acs.models.data_object.entity.CurrencyDO;
import org.springframework.stereotype.Service;

@Service
public class UiConfigFacade {

    private final ClassicRbaProperties classicRbaProperties;
    private final CurrencyService currencyService;

    public UiConfigFacade(ClassicRbaProperties classicRbaProperties,
        CurrencyService currencyService) {
        this.classicRbaProperties = classicRbaProperties;
        this.currencyService = currencyService;
    }

    public UiConfigDTO getUiConfig() {
        CurrencyDO systemCurrency = currencyService.getSystemCurrency();
        return UiConfigDTO.builder()
          .isMultipleBank(EnvironmentConstants.IS_MULTI_ISSUER)
          .isLdapMode(EnvironmentConstants.IS_EXTERNAL_AUTHENTICATION_SERVICE)
          .isMfa(EnvironmentConstants.IS_MFA)
          .mfaSystem(EnvironmentConstants.MFA_SYSTEM)
          .rbaSettings(classicRbaProperties)
          .cavvImportMode(EnvironmentConstants.CAVV_IMPORT_MODE)
          .attemptSettingSelectCurrency(EnvironmentConstants.ATTEMPT_SETTING_SELECT_CURRENCY)
          .systemCurrency(systemCurrency.getAlpha())
          .systemCurrencyCode(Integer.parseInt(systemCurrency.getCode()))
          .cavvEncryptPublicKey(EnvironmentConstants.cavvEncryptPublicKey)
          .recaptchaSiteKey(EnvironmentConstants.RECAPTCHA_SITE_KEY)
          .build();
    }
}
