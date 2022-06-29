package com.cherri.acs_portal.dto.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.models.data_object.entity.SystemSettingDO;

@Data
@Log4j2
public class CardBrandTransactionRateKpiDTO {

    @Getter
    @AllArgsConstructor
    enum RateType {
        /**
         * Rate Type
         */
        N_RATE("n"),
        U_RATE("u");
        private final String systemSettingKeyValue;
    }

    private final Map<String, Map<RateType, Integer>> transactionRateKpiMap;

    public CardBrandTransactionRateKpiDTO(List<SystemSettingDO> transactionRateKpiSettingList) {
        transactionRateKpiMap = new HashMap<>();
        transactionRateKpiSettingList.forEach(
          systemSetting -> {
              String cardBrand = systemSetting.getCategory();
              RateType rateType = getRateTypeFromTransactionRateKpiSetting(systemSetting);
              Integer kpi = Integer.parseInt(systemSetting.getValue());

              Map<RateType, Integer> rateMap = transactionRateKpiMap
                .computeIfAbsent(cardBrand, k -> new HashMap<>());
              rateMap.put(rateType, kpi);
          });
    }

    public Integer getKPiByCardBrandAndRateType(String cardBrand, RateType rateType) {
        Map<RateType, Integer> rateMap = transactionRateKpiMap.get(cardBrand);
        if (rateMap == null) {
            return null;
        }
        return rateMap.get(rateType);
    }

    private RateType getRateTypeFromTransactionRateKpiSetting(SystemSettingDO setting) {
        for (RateType rateType : RateType.values()) {
            // VISA.n.rate.kpi
            String rateTypeString = setting.getKey().split("\\.")[1];
            if (rateType.getSystemSettingKeyValue().equals(rateTypeString)) {
                return rateType;
            }
        }
        log.error(
          "[getRateTypeFromTransactionRateKpiSetting] Invalid SYSTEM_SETTING KY = {}, VAL = {}",
          setting.getKey(),
          setting.getValue());
        throw new OceanException(ResultStatus.SERVER_ERROR,
          "Invalid SYSTEM_SETTING KY = " + setting.getKey());
    }
}
