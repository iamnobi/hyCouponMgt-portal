package com.cherri.acs_portal.service;

import com.cherri.acs_kernel.plugin.dto.hsm.result.HSMGenerateKeyResultDTO;
import com.cherri.acs_portal.component.HsmPlugin;
import com.cherri.acs_portal.controller.response.BankDataKeyResDTO;
import com.google.common.io.BaseEncoding;
import java.security.interfaces.RSAPublicKey;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.models.dao.BankDataKeyDAO;
import ocean.acs.models.data_object.entity.BankDataKeyDO;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class BankDataKeyService {

    private final BankDataKeyDAO bankDataKeyDAO;
    private final HsmPlugin hsmPlugin;

    public BankDataKeyService(BankDataKeyDAO bankDataKeyDAO, HsmPlugin hsmPlugin) {
        this.bankDataKeyDAO = bankDataKeyDAO;
        this.hsmPlugin = hsmPlugin;
    }

    public BankDataKeyResDTO queryBankDataKey(Long issuerBankId) {
        try {
            Optional<BankDataKeyDO> bankDataKeyDOOptional = bankDataKeyDAO
              .findByIssuerBankId(issuerBankId);
            if (bankDataKeyDOOptional.isPresent()) {
                return BankDataKeyResDTO.valueOf(bankDataKeyDOOptional.get());
            }
            log.info(
              "[queryBankDataKey] bankDataKey not exists for issuerBankId={}",
              issuerBankId);
            return null;
        } catch (DatabaseException e) {
            log.error(e.getMessage(), e);
            throw new OceanException(ResultStatus.DB_READ_ERROR);
        }
    }

    public BankDataKeyResDTO createBankDataKey(Long issuerBankId, String creator) {
        // 確認 key 是否已存在
        // ==============================
        BankDataKeyResDTO resDTO = queryBankDataKey(issuerBankId);
        if (resDTO != null) {
            log.warn(
              "[createBankDataKey] BankDataKey already exists. issuerBankId={}",
              issuerBankId);
            throw new OceanException(
              ResultStatus.DUPLICATE_DATA_ELEMENT, "BankDataKey already exists.");
        }

        // create RSA key pair in HSM
        // ==============================
        String kyLabel = hsmPlugin.bankDataKyLabelFormat(issuerBankId);
        log.info("[createBankDataKey] creating RSA key pair..");
        HSMGenerateKeyResultDTO genRsaKyPairResult = hsmPlugin
          .generateRsaKeyPair(kyLabel, issuerBankId);
        log.info("[createBankDataKey] create RSA key pair success.");

        String kyId = genRsaKyPairResult.getKeyId();
        RSAPublicKey rsaPubKy = genRsaKyPairResult.getRsaPublicKey();
        String rsaPubKyBase64 = BaseEncoding.base64().encode(rsaPubKy.getEncoded());
        log.debug(
          "[createBankDataKey] keyId={}, keySize={}, rsaPublicKey={}",
          kyId,
          rsaPubKy.getModulus().bitLength(),
          rsaPubKyBase64);

        // Save key info in BankDataKey
        // ==============================
        BankDataKeyDO bankDataKey = new BankDataKeyDO(issuerBankId, kyId, rsaPubKyBase64);
        try {
            bankDataKeyDAO.save(bankDataKey, creator);
        } catch (DatabaseException e) {
            log.error("[createBankDataKey] db error. bankDataKey={}", bankDataKey, e);
            throw new OceanException(ResultStatus.DB_SAVE_ERROR);
        }

        return BankDataKeyResDTO.valueOf(bankDataKey);
    }
}
