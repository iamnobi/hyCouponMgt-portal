package com.cherri.acs_portal.component;

import com.cherri.acs_kernel.plugin.HSMInterface;
import com.cherri.acs_kernel.plugin.dto.HSMLoginInfo;
import com.cherri.acs_kernel.plugin.dto.IssuerPropertyDefinition;
import com.cherri.acs_kernel.plugin.dto.hsm.invoke.HSMCalculateCvvInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.invoke.HSMDecryptInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.invoke.HSMEncryptInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.invoke.HSMGenerateKeyInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.invoke.HSMImportKeyInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.invoke.HSMInitializeInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.invoke.HSMSignInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.result.HSMCalculateCvvResultDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.result.HSMDecryptResultDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.result.HSMEncryptResultDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.result.HSMGenerateKeyResultDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.result.HSMImportKeyResultDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.result.HSMInitializeResultDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.result.HSMSignResultDTO;
import com.cherri.acs_kernel.plugin.enumerator.HSMEncryptDecryptMechanism;
import com.cherri.acs_kernel.plugin.enumerator.HSMGenKeyMechanism;
import com.cherri.acs_kernel.plugin.enumerator.HSMImportMechanism;
import com.cherri.acs_kernel.plugin.enumerator.HSMSignMechanism;
import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.cherri.acs_portal.dto.acs_integrator.CalculateCvvDTO;
import com.cherri.acs_portal.dto.hsm.DecryptResultDTO;
import com.cherri.acs_portal.dto.hsm.EncryptResultDTO;
import com.cherri.acs_portal.model.enumerator.CavvKeyType;
import com.cherri.acs_portal.util.CalculateCvvUtil;
import com.cherri.acs_portal.util.CertificateUtils;
import com.cherri.acs_portal.util.DateUtil;
import com.google.common.io.BaseEncoding;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.PluginType;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.NoSuchDataException;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.models.dao.IssuerBankDAO;
import ocean.acs.models.dao.IssuerRuntimePropertyDAO;
import ocean.acs.models.data_object.entity.IssuerBankDO;
import ocean.acs.models.data_object.entity.IssuerRuntimePropDO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("PortalHsmPlugin")
@Log4j2
public class HsmPlugin implements CalculateCvvUtil.DesProvider {

    @Getter
    private HSMInterface pluginImpl;

    private final IssuerBankDAO issuerBankDAO;
    private final Map<Integer, IssuerPropertyDefinition> propertyDefinitionMap;
    private final IssuerRuntimePropertyDAO runtimePropertyDAO;

    @Autowired
    public HsmPlugin(
      HSMInterface pluginImpl,
      IssuerBankDAO issuerBankDAO,
      IssuerRuntimePropertyDAO runtimePropertyDAO) {

        this.pluginImpl = pluginImpl;
        this.issuerBankDAO = issuerBankDAO;
        List<IssuerPropertyDefinition> propertyDefinitionList =
          pluginImpl.getIssuerPropertyDefinitionList();
        this.runtimePropertyDAO = runtimePropertyDAO;

        this.propertyDefinitionMap = new HashMap<>();
        if (propertyDefinitionList != null && !propertyDefinitionList.isEmpty()) {
            for (IssuerPropertyDefinition propDef : propertyDefinitionList) {
                propertyDefinitionMap.put(propDef.getPropertyId(), propDef);
            }
        }

        this.pluginImpl.initialize(
          HSMInitializeInvokeDTO.builder().systemPropertiesMap(new HashMap<>()).build());
    }

    protected Map<IssuerPropertyDefinition, String> getIssuerRuntimeProperty(Long issuerBankId) {
        Map<IssuerPropertyDefinition, String> propMap = new HashMap<>();
        List<IssuerRuntimePropDO> propValueDOList =
          runtimePropertyDAO.getPropertyListOfIssuer(issuerBankId, getPluginType());

        if (propValueDOList == null || propValueDOList.isEmpty())
            return propMap;

        for (IssuerRuntimePropDO propValue : propValueDOList) {
            IssuerPropertyDefinition propDef = findPropertyDefinitionById(
              propValue.getPropertyId());
            if (propDef == null) {
                log.warn(
                  "Plugin Value miss matched. There might be some property has been removed or deprecated in plugin setting.");
                continue;
            }

            propMap.put(propDef, propValue.getValue());
        }
        return propMap;
    }

    public String getName() {
        return pluginImpl.getPluginName();
    }

    public PluginType getPluginType() {
        return PluginType.HSM;
    }

    public List<IssuerPropertyDefinition> getPropertyDefinitionList() {
        return pluginImpl.getIssuerPropertyDefinitionList();
    }

    public IssuerPropertyDefinition findPropertyDefinitionById(int propertyId) {
        return propertyDefinitionMap.get(propertyId);
    }

    // DesProvider
    @Override
    public byte[] desEncrypt(String keyId, byte[] data) throws Exception {
        HSMEncryptInvokeDTO hsmEncryptInvokeDTO =
          HSMEncryptInvokeDTO.builder()
            .hsmMechanism(HSMEncryptDecryptMechanism.CKM_DES_ECB)
            .keyId(keyId)
            .plainText(data)
            // TODO: set the maps
            .issuerPropertiesMap(new HashMap<>())
            .systemPropertiesMap(new HashMap<>())
            .build();
        HSMEncryptResultDTO hsmEncryptResultDTO = pluginImpl.encrypt(hsmEncryptInvokeDTO);
        ensureInvokeSuccessOrThrow(hsmEncryptResultDTO);
        return hsmEncryptResultDTO.getCiphertext();
    }

    @Override
    public byte[] desDecrypt(String keyId, byte[] data) throws Exception {
        HSMDecryptInvokeDTO hsmDecryptInvokeDTO =
          HSMDecryptInvokeDTO.builder()
            .hsmMechanism(HSMEncryptDecryptMechanism.CKM_DES_ECB)
            .keyId(keyId)
            .cipherText(data)
            // TODO: set the maps
            .issuerPropertiesMap(new HashMap<>())
            .systemPropertiesMap(new HashMap<>())
            .build();
        HSMDecryptResultDTO hsmDecryptResultDTO = pluginImpl.decrypt(hsmDecryptInvokeDTO);
        ensureInvokeSuccessOrThrow(hsmDecryptResultDTO);
        return hsmDecryptResultDTO.getPlainText();
    }
    // END: DesProvider

    public enum RSA_PKCS_SIGNATURE_HASH_FUNCTION {
        SHA1,
        SHA256
    }

    public EncryptResultDTO encryptWithIssuerBankId(String plaintextString, Long issuerBankId) {
        String keyId = findKeyIdByIssuerBankId(issuerBankId);
        return encryptWithKeyId(plaintextString.getBytes(StandardCharsets.UTF_8), keyId,
          issuerBankId);
    }

    public EncryptResultDTO encryptWithIssuerBankId(byte[] plaintext, Long issuerBankId) {
        String keyId = findKeyIdByIssuerBankId(issuerBankId);

        return encryptWithKeyId(plaintext, keyId, issuerBankId);
    }

    public HSMInitializeResultDTO initialize(List<HSMLoginInfo> hsmLoginInfoList) {

        HSMInitializeInvokeDTO invokeDTO =
          HSMInitializeInvokeDTO.builder()
            .hsmLoginInfoList(hsmLoginInfoList)
            .systemPropertiesMap(new HashMap<>())
            .build();
        HSMInitializeResultDTO resultDTO = pluginImpl.initialize(invokeDTO);

        ensureInvokeSuccessOrThrow(resultDTO);

        return resultDTO;
    }

    public HSMImportKeyResultDTO importKey(Long issuerBankId, String keyLabel, String desKeyHex, HSMImportMechanism importMechanism) {
        Map<IssuerPropertyDefinition, String> issuerRuntimeProp =
//        getIssuerRuntimeProperty(issuerBankId);
          getHsmIssuerRuntimeProperty(issuerBankId);

        HSMImportKeyInvokeDTO invokeDTO =
          HSMImportKeyInvokeDTO.builder()
            .keyLabel(keyLabel)
            .desKeyHex(desKeyHex)
            .hsmMechanism(importMechanism)
            .wrapKey((RSAPublicKey) CertificateUtils.readPublicKey(EnvironmentConstants.cavvEncryptPublicKey))
            .unwrapKey(EnvironmentConstants.cavvEncryptPrivateKey)
            .issuerPropertiesMap(issuerRuntimeProp)
            .systemPropertiesMap(new HashMap<>())
            .build();
        HSMImportKeyResultDTO resultDTO = pluginImpl.importKey(invokeDTO);

        ensureInvokeSuccessOrThrow(resultDTO);

        return resultDTO;
    }

    public EncryptResultDTO encryptWithKeyId(byte[] plaintext, String keyId, Long issuerBankId) {
        Map<IssuerPropertyDefinition, String> issuerRuntimeProp =
//        getIssuerRuntimeProperty(issuerBankId);
          getHsmIssuerRuntimeProperty(issuerBankId);
        HSMEncryptInvokeDTO hsmEncryptInvokeDTO =
          HSMEncryptInvokeDTO.builder()
            .hsmMechanism(EnvironmentConstants.HSM_ENCRYPT_DECRYPT_MECHANISM)
            .keyId(keyId)
            .plainText(plaintext)
            .issuerPropertiesMap(issuerRuntimeProp)
            .systemPropertiesMap(new HashMap<>())
            .build();
        HSMEncryptResultDTO hsmEncryptResultDTO = pluginImpl.encrypt(hsmEncryptInvokeDTO);
        ensureInvokeSuccessOrThrow(hsmEncryptResultDTO);
        return new EncryptResultDTO(hsmEncryptResultDTO.getCiphertext());
    }

    public DecryptResultDTO decryptSensitiveDataWithIssuerBankId(String base64String, Long issuerBankId) {
        String keyId = findSensitiveKeyIdByIssuerBankId(issuerBankId);

        return decryptWithKeyId(base64String, keyId, issuerBankId);
    }

    public DecryptResultDTO decryptWithIssuerBankId(String base64String, Long issuerBankId) {
        String keyId = findKeyIdByIssuerBankId(issuerBankId);

        return decryptWithKeyId(base64String, keyId, issuerBankId);
    }

    public DecryptResultDTO decryptWithKeyId(String base64String, String keyId, Long issuerBankId) {
        if (StringUtils.isEmpty(base64String)) {
            return new DecryptResultDTO(new byte[0]);
        }

        // Decrypt 不支援 decrypt issuerRuntimeProperty 的值，不然會進入無窮迴圈。
        Map<IssuerPropertyDefinition, String> issuerRuntimeProp =
          getIssuerRuntimeProperty(issuerBankId);

        HSMDecryptInvokeDTO hsmDecryptInvokeDTO =
          HSMDecryptInvokeDTO.builder()
            .hsmMechanism(EnvironmentConstants.HSM_ENCRYPT_DECRYPT_MECHANISM)
            .keyId(keyId)
            .cipherText(BaseEncoding.base64().decode(base64String))
            .issuerPropertiesMap(issuerRuntimeProp)
            .systemPropertiesMap(new HashMap<>())
            .build();
        HSMDecryptResultDTO hsmDecryptResultDTO = pluginImpl.decrypt(hsmDecryptInvokeDTO);
        ensureInvokeSuccessOrThrow(hsmDecryptResultDTO);
        return new DecryptResultDTO(hsmDecryptResultDTO.getPlainText());
    }

    public HSMGenerateKeyResultDTO generateRsaKeyPair(String keyLabel, Long issuerBankId) {
        Map<IssuerPropertyDefinition, String> issuerRuntimeProp =
//        getIssuerRuntimeProperty(issuerBankId);
          getHsmIssuerRuntimeProperty(issuerBankId);
        HSMGenerateKeyInvokeDTO hsmGenerateKeyInvokeDTO =
          HSMGenerateKeyInvokeDTO.builder()
            .keyLabel(keyLabel)
            .hsmMechanism(HSMGenKeyMechanism.CKM_RSA_PKCS_KEY_PAIR_GEN)
            .issuerPropertiesMap(issuerRuntimeProp)
            .systemPropertiesMap(new HashMap<>())
            .build();
        HSMGenerateKeyResultDTO hsmGenerateKeyResultDTO =
          pluginImpl.generateKey(hsmGenerateKeyInvokeDTO);
        ensureInvokeSuccessOrThrow(hsmGenerateKeyResultDTO);
        return hsmGenerateKeyResultDTO;
    }

    public String generateEncryptKey(String keyLabel, Long issuerBankId) {
        Map<IssuerPropertyDefinition, String> issuerRuntimeProp =
//        getIssuerRuntimeProperty(issuerBankId);
          getHsmIssuerRuntimeProperty(issuerBankId);

        HSMGenerateKeyInvokeDTO hsmGenerateKeyInvokeDTO =
          HSMGenerateKeyInvokeDTO.builder()
            .keyLabel(keyLabel)
            .hsmMechanism(HSMGenKeyMechanism.CKM_AES_KEY_GEN)
            .issuerPropertiesMap(issuerRuntimeProp)
            .systemPropertiesMap(new HashMap<>())
            .build();
        HSMGenerateKeyResultDTO hsmGenerateKeyResultDTO =
          pluginImpl.generateKey(hsmGenerateKeyInvokeDTO);
        ensureInvokeSuccessOrThrow(hsmGenerateKeyResultDTO);
        return hsmGenerateKeyResultDTO.getKeyId();
    }

    public byte[] rsaSign(
      HSMSignMechanism hsmSignMechanism, String keyId, byte[] message, Long issuerBankId) {
        Map<IssuerPropertyDefinition, String> issuerRuntimeProp =
//        getIssuerRuntimeProperty(issuerBankId);
          getHsmIssuerRuntimeProperty(issuerBankId);

        HSMSignInvokeDTO hsmSignInvokeDTO =
          HSMSignInvokeDTO.builder()
            .hsmMechanism(hsmSignMechanism)
            .keyId(keyId)
            .message(message)
            .issuerPropertiesMap(issuerRuntimeProp)
            .systemPropertiesMap(new HashMap<>())
            .build();
        HSMSignResultDTO hsmSignResultDTO = pluginImpl.sign(hsmSignInvokeDTO);
        ensureInvokeSuccessOrThrow(hsmSignResultDTO);
        return hsmSignResultDTO.getSignature();
    }

    public String decryptCavvKeyComponent(String keyId, String base64String) {
        // no issuerRuntimeProp for this function
        Map<IssuerPropertyDefinition, String> issuerRuntimeProp = new HashMap<>();

        HSMDecryptInvokeDTO hsmDecryptInvokeDTO =
            HSMDecryptInvokeDTO.builder()
                .hsmMechanism(HSMEncryptDecryptMechanism.CKM_RSA_PKCS_OAEP_WITH_SHA256_AND_MGF1_PADDING)
                .keyId(keyId)
                .cipherText(BaseEncoding.base64().decode(base64String))
                .issuerPropertiesMap(issuerRuntimeProp)
                .systemPropertiesMap(new HashMap<>())
                .build();
        HSMDecryptResultDTO hsmDecryptResultDTO = pluginImpl.decrypt(hsmDecryptInvokeDTO);

        ensureInvokeSuccessOrThrow(hsmDecryptResultDTO);
        DecryptResultDTO decryptResultDTO = null;
        try {
            decryptResultDTO = new DecryptResultDTO(hsmDecryptResultDTO.getPlainText());
            return decryptResultDTO.getString();
        } finally {
            if (decryptResultDTO != null) {
                decryptResultDTO.clearPlainText();
            }
        }
    }

    public byte[] rsaPssSign(String keyId, byte[] message, Long issuerBankId) throws Exception {
        Map<IssuerPropertyDefinition, String> issuerRuntimeProp =
          getHsmIssuerRuntimeProperty(issuerBankId);

        HSMSignInvokeDTO hsmSignInvokeDTO =
          HSMSignInvokeDTO.builder()
            .hsmMechanism(HSMSignMechanism.CKM_SHA256_RSA_PKCS_PSS)
            .keyId(keyId)
            .message(message)
            .issuerPropertiesMap(issuerRuntimeProp)
            .systemPropertiesMap(new HashMap<>())
            .build();
        HSMSignResultDTO hsmSignResultDTO = pluginImpl.sign(hsmSignInvokeDTO);
        ensureInvokeSuccessOrThrow(hsmSignResultDTO);
        return hsmSignResultDTO.getSignature();
    }

    public String bankDataKyLabelFormat(long issuerBankId) {
        String bankCode = getBankCode(issuerBankId);
        return String.format(
          "BANK_DATA_KEY_%s_%s",
          bankCode, DateUtil.millisToUtcDateTimeStr(System.currentTimeMillis()));
    }

    public String sensitiveDataKyLabelFormat(long issuerBankId) {
        String bankCode = getBankCode(issuerBankId);
        return String.format(
            "SENSITIVE_DATA_KEY_%s_%s",
            bankCode, DateUtil.millisToUtcDateTimeStr(System.currentTimeMillis()));
    }

    public String acsSigningKyLabelFormat(String bankCode, String cardBrand) {
        return String.format(
          "ACS_SIGNING_%s_%s_%s",
          bankCode, cardBrand, DateUtil.millisToUtcDateTimeStr(System.currentTimeMillis()));
    }

    public String cavvKyLabelFormat(long issuerBankId, String cardBrand, CavvKeyType keyType) {
        String bankCode = getBankCode(issuerBankId);
        return String.format(
          "ACS_CAVV_%s_%s_%s_%s",
          bankCode,
          cardBrand,
          keyType.toString(),
          DateUtil.millisToUtcDateTimeStr(System.currentTimeMillis()));
    }

    /**
     * 依照設定決定使用 hsmPlugin.calculateCvv() 還是 hsmPlugin.encrypt(DES) + hsmPlugin.decrypt(DES)
     *
     * @param cardNumber           Card number
     * @param atnLastFour          VISA/JCB : ATN last four number, MasterCard : Expiry date(yy/mm)
     * @param threeDSecurityResult VISA/JCB : Three-D Security Result, MasterCard : A 3 length
     *                             random digit number
     * @param keyA                 Key A
     * @param keyB                 Key B
     * @return cvv
     */
    public String calculateCvv(CalculateCvvDTO calculateCvvDTO, Long issuerBankId)
      throws Exception {
        if (EnvironmentConstants.IS_HSM_SUPPORT_CALCULATE_CVV) {
            Map<IssuerPropertyDefinition, String> issuerRuntimeProp =
              getHsmIssuerRuntimeProperty(issuerBankId);

            HSMCalculateCvvInvokeDTO hsmCalculateCvvInvokeDTO =
              HSMCalculateCvvInvokeDTO.builder()
                .cardNumber(calculateCvvDTO.getCardNumber())
                .atnLastFour(calculateCvvDTO.getAtnLastFour())
                .threeDSecurityResult(calculateCvvDTO.getThreeDSecurityResult())
                .keyA(calculateCvvDTO.getKeyA())
                .keyB(calculateCvvDTO.getKeyB())
                .issuerPropertiesMap(issuerRuntimeProp)
                .systemPropertiesMap(new HashMap<>())
                .build();
            HSMCalculateCvvResultDTO hsmCalculateCvvResultDTO =
              pluginImpl.calculateCvv(hsmCalculateCvvInvokeDTO);
            ensureInvokeSuccessOrThrow(hsmCalculateCvvResultDTO);
            return hsmCalculateCvvResultDTO.getCvv();

        } else {
            return CalculateCvvUtil.calculateCvvWithVersion2(calculateCvvDTO, this);
        }
    }

    private Map<IssuerPropertyDefinition, String> getHsmIssuerRuntimeProperty(Long issuerBankId) {
        Map<IssuerPropertyDefinition, String> issuerRuntimeProp =
          getIssuerRuntimeProperty(issuerBankId);

        for (Map.Entry<IssuerPropertyDefinition, String> entry : issuerRuntimeProp.entrySet()) {
            if (!entry.getKey().isEncrypt())
                continue;

            DecryptResultDTO decryptResultDTO = null;
            try {
                decryptResultDTO = decryptWithIssuerBankId(entry.getValue(),
                  issuerBankId);
                entry.setValue(decryptResultDTO.getString());
            } finally {
                if (decryptResultDTO != null) {
                    decryptResultDTO.clearPlainText();
                }
            }
        }

        return issuerRuntimeProp;
    }

    private String findKeyIdByIssuerBankId(Long issuerBankId) {
        IssuerBankDO issuerBank = issuerBankDAO.findById(issuerBankId).orElse(null);
        if (issuerBank == null) {
            log.error(
              "[findKeyIdByIssuerBankId] Issuer Bank not found for issuerBankId={}", issuerBankId);
            throw new OceanException(
              ResultStatus.SERVER_ERROR,
              "[DB Data Error] Issuer Bank not found for issuerBankId=" + issuerBankId);
        }
        if (issuerBank.getSymmetricKeyId() == null) {
            log.error(
              "[findKeyIdByIssuerBankId] Issuer Bank SymmetricKeyId = null. issuerBankId={}",
              issuerBankId);
            throw new OceanException(
              ResultStatus.SERVER_ERROR,
              "[DB Data Error] Issuer Bank SymmetricKeyId = null. issuerBankId=" + issuerBankId);
        }
        return issuerBank.getSymmetricKeyId();
    }

    private String findSensitiveKeyIdByIssuerBankId(Long issuerBankId) {
        IssuerBankDO issuerBank = issuerBankDAO.findById(issuerBankId).orElse(null);
        if (issuerBank == null) {
            log.error(
                "[findSensitiveKeyIdByIssuerBankId] Issuer Bank not found for issuerBankId={}", issuerBankId);
            throw new OceanException(
                ResultStatus.SERVER_ERROR,
                "[DB Data Error] Issuer Bank not found for issuerBankId=" + issuerBankId);
        }
        if (issuerBank.getSensitiveDataKeyId() == null) {
            log.error(
                "[findSensitiveKeyIdByIssuerBankId] Issuer Bank SensitiveDataKeyId = null. issuerBankId={}",
                issuerBankId);
            throw new OceanException(
                ResultStatus.SERVER_ERROR,
                "[DB Data Error] Issuer Bank SensitiveDataKeyId = null. issuerBankId=" + issuerBankId);
        }
        return issuerBank.getSensitiveDataKeyId();
    }

    private String getBankCode(Long issuerBankId) throws NoSuchDataException {
        return issuerBankDAO
          .findById(issuerBankId)
          .map(IssuerBankDO::getCode)
          .orElseThrow(
            () -> new NoSuchDataException("The issuerBank id:" + issuerBankId + " not found."));
    }

    private void ensureInvokeSuccessOrThrow(
      com.cherri.acs_kernel.plugin.dto.ResultDTO pluginResultDTO) {
        if (pluginResultDTO.isSuccess()) {
            return;
        }

        log.error("[HsmPlugin] plugin error", pluginResultDTO.getException());
        throw new OceanException(
          ResultStatus.HSM_PLUGIN_EXCEPTION, ResultStatus.HSM_PLUGIN_EXCEPTION.toString());
    }
}
