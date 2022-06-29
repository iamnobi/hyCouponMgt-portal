package com.cherri.acs_portal.service;

import com.cherri.acs_portal.constant.EnvironmentConstants;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.models.dao.CurrencyDAO;
import ocean.acs.models.data_object.entity.CurrencyDO;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class CurrencyService {

    private final CurrencyDAO currencyDao;
    // cached systemCurrency
    private CurrencyDO systemCurrency;

    public CurrencyService(CurrencyDAO currencyDao, EnvironmentConstants environmentConstants) {
        this.currencyDao = currencyDao;

        // find systemCurrency
        systemCurrency =
                currencyDao
                        .findByCode(String.valueOf(EnvironmentConstants.SYSTEM_CURRENCY_CODE))
                        .orElse(null);
        if (systemCurrency == null) {
            log.error(
                    "[getSystemCurrency] system currency '{}' is invalid",
                    EnvironmentConstants.SYSTEM_CURRENCY_CODE);
            throw new OceanException(ResultStatus.SERVER_ERROR, "system currency is invalid");
        }
    }

    public CurrencyDO getSystemCurrency() {
        return systemCurrency;
    }
}
