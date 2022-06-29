package com.cherri.acs_portal.service;

import com.cherri.acs_portal.dto.certificate.CardBrandSigningCertificateConfigDTO;
import com.cherri.acs_portal.dto.report.CardBrandTransactionRateKpiDTO;
import com.cherri.acs_portal.dto.system.CardBrandDTO;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.CavvKeyAlgType;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.models.dao.CardBrandDAO;
import ocean.acs.models.dao.SystemSettingDAO;
import ocean.acs.models.data_object.entity.SystemSettingDO;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class CardBrandConfigurationHelper {

    private static final String USER_GLOBAL_SIGNING_CERTIFICATE_CONFIG =
      "%s.useGlobalSigningCertificate";
    private static final String USER_GLOBAL_SIGNING_CERTIFICATE_CONFIG_3DS1 =
      "%s.useGlobalSigningCertificate3ds1";
    private static final String TRANSACTION_RATE_KPI_SETTING = "transactionRateKPI";
    private static final String CAVV_KEY_NUM = "cavvKeyNum";
    private static final String CAVV_KEY_TYPE = "cavvKeyType";
    private static final String USER_GLOBAL_SIGNING_CERTIFICATE_CONFIG_TRUE = "1";
    private final long CARD_BRAND_LIST_CACHED_MILLIS = 10 * 1000;
    private long cachedCardBrandListCreateMillis;
    private List<CardBrandDTO> cachedCardBrandList = null;

    private final SystemSettingDAO systemSettingDAO;
    private final CardBrandDAO cardBrandDAO;

    public CardBrandConfigurationHelper(SystemSettingDAO systemSettingDAO,
      CardBrandDAO cardBrandDAO) {
        this.systemSettingDAO = systemSettingDAO;
        this.cardBrandDAO = cardBrandDAO;
        //findCardBrandList();
    }

    public Optional<CardBrandDTO> findCardBrand(String cardBrand) {
        return findCardBrandList().stream()
                .filter(dto -> dto.getName().equals(cardBrand))
                .findFirst();
    }

    public List<CardBrandDTO> findCardBrandList() {
        if (cachedCardBrandList == null || isCachedCardBrandListExpired()) {
            log.debug("[findCardBrandList] reload CardBrand");
            List<CardBrandDTO> cardBrandDTOList =
                    cardBrandDAO.findCardBrandList().stream()
                            .map(CardBrandDTO::valueOf)
                            .collect(Collectors.toList());

            // find and set cavvKeyNum
            List<SystemSettingDO> cavvKeyNumSettingList =
                    systemSettingDAO.findByClassName(CAVV_KEY_NUM);
            for (SystemSettingDO cavvKeyNumSetting : cavvKeyNumSettingList) {
                Optional<CardBrandDTO> cardBrandDTO =
                        cardBrandDTOList.stream()
                                .filter(dto -> dto.getName().equals(cavvKeyNumSetting.getCategory()))
                                .findFirst();
                cardBrandDTO.ifPresent(dto ->
                    dto.setCavvKeyNum(Integer.parseInt(cavvKeyNumSetting.getValue())));
            }

            // find and set cavvKeyType
            List<SystemSettingDO> cavvKeyTypeSettingList =
                systemSettingDAO.findByClassName(CAVV_KEY_TYPE);
            for (SystemSettingDO cavvKeyTypeSetting : cavvKeyTypeSettingList) {
                Optional<CardBrandDTO> cardBrandDTO =
                    cardBrandDTOList.stream()
                        .filter(dto -> dto.getName().equals(cavvKeyTypeSetting.getCategory()))
                        .findFirst();
                cardBrandDTO.ifPresent(dto ->
                    dto.setCavvKeyAlgType(CavvKeyAlgType.valueOf(cavvKeyTypeSetting.getValue())));
            }

            this.cachedCardBrandList = cardBrandDTOList;
            this.cachedCardBrandListCreateMillis = System.currentTimeMillis();
        }
        return cachedCardBrandList;
    }

    public boolean isCardBrandUseGlobalSigningCertificate(int version, String cardBrand) {
        String configName = version == 1
            ? USER_GLOBAL_SIGNING_CERTIFICATE_CONFIG_3DS1
            : USER_GLOBAL_SIGNING_CERTIFICATE_CONFIG;
        String systemSettingKey = String.format(configName, cardBrand);
        SystemSettingDO useGlobalSigningCertificateSetting =
          systemSettingDAO.findByKey(systemSettingKey).orElse(null);
        // setting not found
        if (useGlobalSigningCertificateSetting == null) {
            log.warn(
              "[isCardBrandUseGlobalSigningCertificate] SYSTEM_SETTING data error, missing key = {}",
              systemSettingKey);
            throw new OceanException(ResultStatus.NO_SUCH_DATA,
              "SYSTEM_SETTING data error, missing key = " + systemSettingKey);
        }

        return useGlobalSigningCertificateSetting
          .getValue()
          .equals(USER_GLOBAL_SIGNING_CERTIFICATE_CONFIG_TRUE);
    }

    public List<CardBrandSigningCertificateConfigDTO> findCardBrandSigningCertificateConfig() {
        return cardBrandDAO.findCardBrandSigningCertificateConfig().stream()
          .map(CardBrandSigningCertificateConfigDTO::valueOf).collect(
            Collectors.toList());
    }

    public CardBrandTransactionRateKpiDTO findCardBrandTransactionRateKpi() {
        List<SystemSettingDO> transactionRateKPISettingList =
          systemSettingDAO.findByClassName(TRANSACTION_RATE_KPI_SETTING);
        return new CardBrandTransactionRateKpiDTO(transactionRateKPISettingList);
    }

    private boolean isCachedCardBrandListExpired() {
        return System.currentTimeMillis() > (cachedCardBrandListCreateMillis
          + CARD_BRAND_LIST_CACHED_MILLIS);
    }
}
