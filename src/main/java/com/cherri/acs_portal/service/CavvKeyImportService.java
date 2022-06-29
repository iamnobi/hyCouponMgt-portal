package com.cherri.acs_portal.service;

import com.cherri.acs_kernel.plugin.dto.hsm.result.HSMImportKeyResultDTO;
import com.cherri.acs_kernel.plugin.enumerator.HSMImportMechanism;
import com.cherri.acs_portal.component.HsmPlugin;
import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.cherri.acs_portal.controller.request.KeyImportReqDTO;
import com.cherri.acs_portal.controller.request.KeyImportVerifyReqDTO;
import com.cherri.acs_portal.controller.response.KeyImportVerifyResDTO;
import com.cherri.acs_portal.dto.system.CardBrandDTO;
import com.cherri.acs_portal.util.DesUtils;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.CavvKeyAlgType;
import ocean.acs.commons.enumerator.KeyStoreKeyName;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.models.dao.KeyStoreDAO;
import ocean.acs.models.data_object.entity.KeyStoreDO;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class CavvKeyImportService {

    private final HsmPlugin hsmPlugin;
    private final KeyStoreDAO keyStoreDAO;
    private final CardBrandConfigurationHelper cardBrandConfigurationHelper;

    public CavvKeyImportService(HsmPlugin hsmPlugin,
        KeyStoreDAO keyStoreDAO,
        CardBrandConfigurationHelper cardBrandConfigurationHelper) {
        this.hsmPlugin = hsmPlugin;
        this.keyStoreDAO = keyStoreDAO;
        this.cardBrandConfigurationHelper = cardBrandConfigurationHelper;
    }


    /**
     * importKeyConfirm
     *
     * @param reqDto
     * @return keyId
     */
    public String importKeyConfirm(KeyImportReqDTO reqDto) {
        // 參數驗證
        if (reqDto.getKeyComponents().size() != 2 && reqDto.getKeyComponents().size() != 3) {
            throw new OceanException(
              ResultStatus.ILLEGAL_ARGUMENT,
              "Wrong keyComponentsKcv size=" + reqDto.getKeyComponents().size());
        }

        // decrypt key components
        List<String> keyComponents =
            reqDto.getKeyComponents().stream()
                .map(
                    keyComponent ->
                        hsmPlugin.decryptCavvKeyComponent(
                            EnvironmentConstants.cavvEncryptPrivateKey, keyComponent))
                .collect(Collectors.toList());

        // combine key
        String combinedKey;
        try {
            combinedKey =
              DesUtils.combineKeyComponents(
                keyComponents.get(0),
                keyComponents.get(1),
                keyComponents.size() == 3 ? keyComponents.get(2) : null);
        } catch (Exception e) {
            log.warn("[importKeyConfirm] failed to combine key", e);
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT, "failed to combine key");
        }

        // Find CavvKeyAlgType for request card brand type
        CavvKeyAlgType cavvKeyAlgType =
                cardBrandConfigurationHelper
                        .findCardBrand(reqDto.getCardBrand())
                        .map(CardBrandDTO::getCavvKeyAlgType)
                        .orElseThrow(
                                () ->
                                        new OceanException(
                                                ResultStatus.SERVER_ERROR, "Unknown card brand"));

        HSMImportMechanism hsmImportMechanism;
        if (CavvKeyAlgType.DES.equals(cavvKeyAlgType)) {
            hsmImportMechanism = HSMImportMechanism.CKM_DES;
        } else if (CavvKeyAlgType.DES3.equals(cavvKeyAlgType)) {
            hsmImportMechanism = HSMImportMechanism.CKM_DES3;
        } else if (CavvKeyAlgType.HMAC.equals(cavvKeyAlgType)) {
            hsmImportMechanism = HSMImportMechanism.CKM_HMAC;
        } else {
            throw new OceanException(ResultStatus.SERVER_ERROR, "Unknown");
        }

        // Import to HSM
        HSMImportKeyResultDTO hsmImportKeyResultDTO = hsmPlugin.importKey(
          reqDto.getIssuerBankId(),
          hsmPlugin.cavvKyLabelFormat(
            reqDto.getIssuerBankId(), reqDto.getCardBrand(), reqDto.getKeyType()),
          combinedKey,
          hsmImportMechanism);
        return hsmImportKeyResultDTO.getKeyId();
    }
}
