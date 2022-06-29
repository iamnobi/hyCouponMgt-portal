package com.cherri.acs_portal.service;

import com.cherri.acs_kernel.plugin.dto.hsm.result.HSMGenerateKeyResultDTO;
import com.cherri.acs_portal.component.HsmPlugin;
import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.cherri.acs_portal.constant.MessageConstants;
import com.cherri.acs_portal.controller.request.CreateOrRenewSslCertificateP12ReqDto;
import com.cherri.acs_portal.controller.request.CreateOrRenewSslCertificateReqDto;
import com.cherri.acs_portal.controller.request.CreateSigningCertificateReqDto;
import com.cherri.acs_portal.controller.request.GenCsrRequestDto;
import com.cherri.acs_portal.controller.request.SigningCertificateQueryDto;
import com.cherri.acs_portal.controller.response.CertificateInfo;
import com.cherri.acs_portal.controller.response.CertificateRootCaSubCaInfoDto;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.hsm.DecryptResultDTO;
import com.cherri.acs_portal.dto.system.CaCertificateDto;
import com.cherri.acs_portal.dto.system.GenKeyRequest;
import com.cherri.acs_portal.dto.system.SigningCertificateDTO;
import com.cherri.acs_portal.dto.system.SigningCertificateDetailDTO;
import com.cherri.acs_portal.dto.system.SslClientCertificateDto;
import com.cherri.acs_portal.manager.AcsIntegratorManager;
import com.cherri.acs_portal.model.enumerator.CsrSignatureAlgorithm;
import com.cherri.acs_portal.util.CertificateUtils;
import com.google.common.io.BaseEncoding;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.Activity;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.enumerator.KeyStatus;
import ocean.acs.commons.enumerator.RenewStatus;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.NoSuchDataException;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.models.dao.CaCertificateDAO;
import ocean.acs.models.dao.CertificateDAO;
import ocean.acs.models.dao.CsrDAO;
import ocean.acs.models.dao.IssuerBankDAO;
import ocean.acs.models.dao.RootCaCertificateDAO;
import ocean.acs.models.dao.SigningCertificateDAO;
import ocean.acs.models.dao.SigningRsaKeyDAO;
import ocean.acs.models.dao.SslCertificateDAO;
import ocean.acs.models.dao.SubCaCertificateDAO;
import ocean.acs.models.data_object.entity.CaCertificateDO;
import ocean.acs.models.data_object.entity.CertificateDO;
import ocean.acs.models.data_object.entity.CsrDO;
import ocean.acs.models.data_object.entity.CsrDO.CertType;
import ocean.acs.models.data_object.entity.IssuerBankDO;
import ocean.acs.models.data_object.entity.RootCaCertificateDO;
import ocean.acs.models.data_object.entity.SigningCertificateDO;
import ocean.acs.models.data_object.entity.SigningRsaKeyDO;
import ocean.acs.models.data_object.entity.SslClientCertificateDO;
import ocean.acs.models.data_object.entity.SubCaCertificateDO;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.x500.X500Name;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
public class CertificateManagementService {

    private final IssuerBankDAO issuerBankDao;
    private final CaCertificateDAO caCertDao;
    private final SslCertificateDAO sslCertDao;
    private final AcsIntegratorManager acsIntegratorManager;

    private final SigningCertificateDAO signingCertificateDao;
    private final CertificateDAO certificateDao;
    private final RootCaCertificateDAO rootCaCertificateDao;
    private final SubCaCertificateDAO subCaCertificateDao;
    private final SigningRsaKeyDAO signingRsaKeyDao;
    private final CsrDAO csrDAO;
    private final CardBrandConfigurationHelper cardBrandConfigurationHelper;
    private final HsmPlugin hsmPlugin;

    public CertificateManagementService(
      IssuerBankDAO issuerBankDao,
      CaCertificateDAO caCertDao,
      SslCertificateDAO sslCertDao,
      AcsIntegratorManager acsIntegratorManager,
      SigningCertificateDAO signingCertificateDao,
      CertificateDAO certificateDao,
      RootCaCertificateDAO rootCaCertificateDao,
      SubCaCertificateDAO subCaCertificateDao,
      SigningRsaKeyDAO signingRsaKeyDao,
      CsrDAO csrDAO,
      CardBrandConfigurationHelper cardBrandConfigurationHelper,
      HsmPlugin hsmPlugin) {
        this.issuerBankDao = issuerBankDao;

        this.caCertDao = caCertDao;
        this.sslCertDao = sslCertDao;
        this.acsIntegratorManager = acsIntegratorManager;
        this.signingCertificateDao = signingCertificateDao;
        this.certificateDao = certificateDao;
        this.rootCaCertificateDao = rootCaCertificateDao;
        this.subCaCertificateDao = subCaCertificateDao;
        this.signingRsaKeyDao = signingRsaKeyDao;
        this.csrDAO = csrDAO;
        this.cardBrandConfigurationHelper = cardBrandConfigurationHelper;
        this.hsmPlugin = hsmPlugin;
    }

    public CaCertificateDto uploadCaCertificate(CaCertificateDto caCertificateDto) {

        String cardBrand = caCertificateDto.getCardBrand();
        String updater = caCertificateDto.getUser();
        byte[] certByteAry = caCertificateDto.getFileContent();

        // 1. read certificate
        X509Certificate cert = CertificateUtils.readCert(new ByteArrayInputStream(certByteAry));
        if (cert == null) {
            throw new OceanException(ResultStatus.INVALID_FORMAT, MessageConstants.get(MessageConstants.CERT_INVALID));
        }

        // 2. check certificate limit: max 10 certificate each card brand
        int count = caCertDao.countByCardBrand(cardBrand);
        if (count >= 10) {
            throw new OceanException(ResultStatus.EXCEEDS_MAX_CA_CERTIFICATE_LIMIT);
        }

        // 3. insert ca certificate
        long now = System.currentTimeMillis();
        CaCertificateDO caCertificate = new CaCertificateDO();
        caCertificate.setCreator(updater);
        caCertificate.setCreateMillis(now);

        caCertificate.setCertificate(certByteAry);
        caCertificate.setCardBrand(cardBrand);
        caCertificate.setCertificateInformation(cert.getSubjectDN().getName());
        caCertificate.setIssuer(cert.getIssuerDN().getName());
        caCertificate.setHashAlgorithm(cert.getSigAlgName());
        caCertificate.setApplyMillis(cert.getNotBefore().getTime());
        caCertificate.setExpireMillis(cert.getNotAfter().getTime());
        caCertificate.setAuditStatus(caCertificateDto.getAuditStatus().getSymbol());
        caCertificate = caCertDao.save(caCertificate);
        return CaCertificateDto.valueOf(caCertificate);
    }

    public DeleteDataDTO delete(DeleteDataDTO deleteDataDTO) {
        Optional<CaCertificateDO> caCertificateOpt = caCertDao.findById(deleteDataDTO.getId());
        if (!caCertificateOpt.isPresent()) {
            throw new OceanException("Failed in delete CA certificate.");
        }
        CaCertificateDO caCertificate = caCertificateOpt.get();
        caCertificate.setAuditStatus(deleteDataDTO.getAuditStatus().getSymbol());
        log.debug("[delete] delete id={}, deleter={}", deleteDataDTO.getId(),
          deleteDataDTO.getAuditStatus());
        caCertificate.setDeleteMillis(System.currentTimeMillis());
        caCertificate.setDeleter(deleteDataDTO.getUser());
        caCertificate.setDeleteFlag(true);
        caCertDao.delete(caCertificate);
        return deleteDataDTO;
    }

    public List<CaCertificateDto> getCaCertificateByCardBrand(String cardBrand) {
        return caCertDao.findByCardBrand(cardBrand).stream().map(CaCertificateDto::valueOf).collect(
            Collectors.toList());
    }

    public Boolean isCaCertificateExists(String cardBrand) {
        Boolean result = caCertDao.existsByCardBrandAndDeleteFlagFalse(cardBrand);
        return result == null ? false : result;
    }

    public CreateOrRenewSslCertificateP12ReqDto uploadSslCertificateP12(
      CreateOrRenewSslCertificateP12ReqDto uploadFileDto) {

        String cardBrandName = uploadFileDto.getCardBrand();
        String pwd = uploadFileDto.getPassword();

        byte[] p12Bytes = uploadFileDto.getFileContent();
        X509Certificate cert;
        try (ByteArrayInputStream in = new ByteArrayInputStream(p12Bytes)) {
            cert = CertificateUtils.readP12(in, pwd);
        } catch (IOException e) {
            log.error("[uploadSslCertificateP12] read P12 errro", e);
            throw new OceanException(MessageConstants.get(MessageConstants.IO_CLOSE_ERROR));
        }
        Long now = System.currentTimeMillis();

        Optional<SslClientCertificateDO> activitySslCertOpt =
          sslCertDao.findUnDeleteByActivityAndCardBrand(cardBrandName);
        if (activitySslCertOpt.isPresent()) {
            SslClientCertificateDO activitySslCert = activitySslCertOpt.get();
            activitySslCert.setDeleteFlag(true);
            activitySslCert.setDeleteMillis(now);
            activitySslCert.setDeleter(uploadFileDto.getUser());
            sslCertDao.save(activitySslCert);
        }

        SslClientCertificateDO sslCert = new SslClientCertificateDO();
        sslCert.setCardBrand(cardBrandName);
        sslCert.setKeyStatus(KeyStatus.COMPLETED);
        sslCert.setActivity(Activity.ENABLED);
        sslCert.setCertificate(p12Bytes);
        sslCert.setHasCert(true);
        sslCert.setKeyPassword(pwd.trim());
        sslCert.setManualPassword(true);
        sslCert.setExpireMillis(cert.getNotAfter().getTime());
        sslCert.setApplyMillis(cert.getNotBefore().getTime());
        sslCert.setCertificateInformation(cert.getSubjectDN().getName());
        sslCert.setIssuer(cert.getIssuerDN().getName());
        sslCert.setHashAlgorithm(cert.getSigAlgName());
        sslCert.setAuditStatus(uploadFileDto.getAuditStatus().getSymbol());
        sslCert.setCreateMillis(now);
        sslCert.setCreator(uploadFileDto.getUser());
        SslClientCertificateDO sslClientCertificate = sslCertDao.save(sslCert);
        return CreateOrRenewSslCertificateP12ReqDto.valueOf(sslClientCertificate);
    }

    public String generateSslCertificateKeyAndCSR(GenKeyRequest request) {
        String cardBrandName = request.getCardBrand();
        sslCertDao.deleteByCardBrand(cardBrandName);
        return saveSslCertificateAndGenerateTempCsr(
          cardBrandName, request.toDistinguishedName(), request.getUser());
    }

    public Optional<SslClientCertificateDO> findUnDeleteByActivityAndCardBrandAndKeyStatus(
      String cardBrand, KeyStatus keyStatus) {
        return sslCertDao.findUnDeleteByActivityAndCardBrandAndKeyStatus(cardBrand, keyStatus);
    }

    public Optional<SslClientCertificateDO> findUnDeleteByActivityAndCardBrand(String cardBrand) {
        return sslCertDao.findUnDeleteByActivityAndCardBrand(cardBrand);
    }

    public Optional<CreateOrRenewSslCertificateP12ReqDto> findSslP12CertById(Long id) {
        Optional<SslClientCertificateDO> sslClientCertificateOpt = sslCertDao.findById(id);
        return sslClientCertificateOpt.map(CreateOrRenewSslCertificateP12ReqDto::valueOf);
    }

    public CreateOrRenewSslCertificateReqDto saveSslCertificate(
      CreateOrRenewSslCertificateReqDto uploadFileDto) {

        String cardBrandName = uploadFileDto.getCardBrand();

        X509Certificate x509Cert =
          CertificateUtils.readCert(new ByteArrayInputStream(uploadFileDto.getFileContent()));
        Optional<SslClientCertificateDO> activitySslCertOpt =
          sslCertDao.findUnDeleteByActivityAndCardBrandAndKeyStatus(
            cardBrandName, KeyStatus.PROCESSING);

        if (activitySslCertOpt.isPresent()) {
            CsrDO csrDO = this.findCsrForSslClientCertOrThrow(x509Cert, cardBrandName);

            final String password = CertificateUtils.genRandomPassword();
            byte[] p12 =
              CertificateUtils.mergeToP12(
                x509Cert, decryptPrivateKeyString(csrDO.getEncPrivateKey()), password);

            SslClientCertificateDO sslCert = activitySslCertOpt.get();
            sslCert.setKeyPassword(encryptKeyPassword(password));
            sslCert.setManualPassword(false);
            sslCert.setHasCert(true);
            sslCert.setCertificate(p12);
            sslCert.setKeyStatus(KeyStatus.COMPLETED);
            sslCert.setActivity(Activity.ENABLED);
            sslCert.setAuditStatus(AuditStatus.AUDITING.getSymbol()); // 上傳憑證後需要先審核
            sslCert.setExpireMillis(x509Cert.getNotAfter().getTime());
            sslCert.setApplyMillis(x509Cert.getNotBefore().getTime());
            sslCert.setCertificateInformation(x509Cert.getSubjectDN().getName());
            sslCert.setIssuer(x509Cert.getIssuerDN().getName());
            sslCert.setHashAlgorithm(x509Cert.getSigAlgName());
            sslCert.setAuditStatus(uploadFileDto.getAuditStatus().getSymbol());
            sslCert.setUpdater(uploadFileDto.getUser());
            sslCert.setUpdateMillis(System.currentTimeMillis());
            SslClientCertificateDO sslClientCertificate = sslCertDao.save(sslCert);

            // update CSR table
            csrDO.setCertUploadMillis(System.currentTimeMillis());
            csrDO.setCertExpireMillis(x509Cert.getNotAfter().getTime());
            csrDO.setCertificate(uploadFileDto.getFileContent());
            csrDO.setUpdater(uploadFileDto.getUser());
            csrDO.setUpdateMillis(System.currentTimeMillis());
            try {
                csrDAO.save(csrDO);
            } catch (DatabaseException e) {
                throw new OceanException(ResultStatus.DB_SAVE_ERROR, e.getMessage(), e);
            }

            return CreateOrRenewSslCertificateReqDto.valueOf(sslClientCertificate);
        }
        return null;
    }

    public boolean deleteSslCertificateTmpKey(String cardBrandName) {
        return sslCertDao.deleteByActivityAndCardBrandAndKeyStatus(cardBrandName);
    }

    public SslClientCertificateDto findSslCertificateByCardBrand(String cardBrandName) {
        List<SslClientCertificateDO> sslCerts = sslCertDao.findUnDeleteByCardBrand(cardBrandName);

        SslClientCertificateDO activityCert = null;
        SslClientCertificateDO inactivityCert = null;
        for (SslClientCertificateDO sslCert : sslCerts) {
            if (Activity.ENABLED.equals(sslCert.getActivity())) {
                activityCert = sslCert;
            } else {
                inactivityCert = sslCert;
            }
        }

        SslClientCertificateDto dto = new SslClientCertificateDto();
        if (activityCert != null) {
            BeanUtils.copyProperties(activityCert, dto);
            if (inactivityCert != null) {
                dto.setHasTempKey(true);
            }
        }

        if ((activityCert == null && inactivityCert == null)) {
            return null;
        } else if (activityCert == null) {
            // Apply for the first certificate
            dto.setHasTempKey(true);
            dto.setAuditStatus(AuditStatus.getStatusBySymbol(inactivityCert.getAuditStatus()));
        } else if (activityCert.isExpired()) {
            // Expired certificate, must be recreated
            dto.setRenewStatus(RenewStatus.NONE);
            dto.setIsExpire(true);
            dto.setAuditStatus(AuditStatus.getStatusBySymbol(activityCert.getAuditStatus()));
        } else if (inactivityCert == null) {
            // Activity certificate
            if (activityCert.getKeyStatus() == KeyStatus.COMPLETED) {
                dto.setRenewStatus(RenewStatus.INIT);
            } else {
                dto.setRenewStatus(RenewStatus.NONE);
            }
            dto.setAuditStatus(AuditStatus.getStatusBySymbol(activityCert.getAuditStatus()));
        } else {
            // Renew certificate
            switch (inactivityCert.getKeyStatus()) {
                case INIT:
                case PROCESSING:
                    dto.setRenewStatus(RenewStatus.PROCESSING);
                    break;
                case COMPLETED:
                    dto.setRenewStatus(RenewStatus.INIT);
                    break;
                default:
                    dto.setRenewStatus(RenewStatus.NONE);
            }
            dto.setAuditStatus(AuditStatus.getStatusBySymbol(inactivityCert.getAuditStatus()));
        }
        return dto;
    }

    public void downloadSslCertificateP12(
      HttpServletResponse response, String cardBrandName, String password) {
        Optional<SslClientCertificateDO> sslCertOpt =
          sslCertDao.findUnDeleteByActivityAndCardBrand(cardBrandName);
        if (sslCertOpt.isPresent()) {
            SslClientCertificateDO sslCert = sslCertOpt.get();
            if (sslCert.getManualPassword() && !sslCert.getKeyPassword().equals(password)) {
                throw new OceanException(
                  ResultStatus.MESSAGE_RECEIVED_INVALID, MessageConstants.get(MessageConstants.MIMA_INCORRECT));
            }

            String fileName = String.format("%s.p12", cardBrandName);
            response.setContentType("text/p12;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            try (InputStream input = new ByteArrayInputStream(sslCert.getCertificate())) {
                IOUtils.copy(input, response.getOutputStream());
                response.flushBuffer();
            } catch (IOException e) {
                log.error(
                  "[downloadSslCertificateP12] Error writing P12 to ServletOutputStream. cardBrand={}",
                  cardBrandName, e);
                throw new OceanException("IO " + MessageConstants.get(MessageConstants.IO_WRITE_ERROR));
            }
        }
    }

    public String renewSslCertificateByGenKey(GenKeyRequest request) {
        String cardBrandName = request.getCardBrand().toUpperCase();
        sslCertDao.deleteByActivityAndCardBrandAndKeyStatus(cardBrandName);
        return saveSslCertificateAndGenerateTempCsr(
          cardBrandName, request.toDistinguishedName(), request.getUser());
    }

    private String saveSslCertificateAndGenerateTempCsr(
      String cardBrandName, String DistinguishedName, String creator) {
        KeyPair keyPair = CertificateUtils.genRSAKey();
        String csr = CertificateUtils.genCsr(keyPair.getPrivate(), keyPair.getPublic(), DistinguishedName);
        String encPrivateKey = encryptPrivateKeyString(keyPair.getPrivate());
        String publicKey = CertificateUtils.writeToString(keyPair.getPublic(), "Public Key");

        SslClientCertificateDO sslCert = new SslClientCertificateDO();
        sslCert.setCardBrand(cardBrandName);
        sslCert.setPrivateKey(encPrivateKey);
        sslCert.setPublicKey(publicKey);
        sslCert.setKeyStatus(KeyStatus.PROCESSING);
        sslCert.setActivity(Activity.DISABLED);
        sslCert.setAuditStatus(AuditStatus.PUBLISHED.getSymbol());
        sslCert.setCreator(creator);
        sslCert.setCreateMillis(System.currentTimeMillis());

        sslCert.setHasCert(false);
        sslCert.setManualPassword(false);
        sslCert.setDeleteFlag(false);

        sslCertDao.save(sslCert);

        // save to CSR table
        CsrDO csrDO = new CsrDO(
            null,
            0,
            EnvironmentConstants.ORG_ISSUER_BANK_ID,
            CertType.SSL_CLIENT,
            cardBrandName,
            csr,
            null,
            null,
            null,
            null,
            encPrivateKey,
            publicKey,
            CertificateUtils.calculateRsaModulusSha256Hex((RSAPublicKey) keyPair.getPublic())
        );
        csrDO.setCreator(creator);
        csrDO.setCreateMillis(System.currentTimeMillis());
        try {
            csrDO = csrDAO.save(csrDO);
        } catch (DatabaseException e) {
            throw new OceanException(ResultStatus.DB_SAVE_ERROR, e.getMessage(), e);
        }

        return csr;
    }

    private String encryptPrivateKeyString(PrivateKey privateKey) throws OceanException {
        String privateStr = CertificateUtils.writeToString(privateKey, "Private Key");
        return hsmPlugin
          .encryptWithIssuerBankId(privateStr, EnvironmentConstants.ORG_ISSUER_BANK_ID)
          .getBase64();
    }

    private String decryptPrivateKeyString(String privateKeyString) {
        DecryptResultDTO decryptResultDTO = null;
        try {
            decryptResultDTO = hsmPlugin
                .decryptWithIssuerBankId(privateKeyString, EnvironmentConstants.ORG_ISSUER_BANK_ID);
            return decryptResultDTO.getString();
        } catch (Exception e) {
            log.error("[decryptPrivateKeyString] decrypt AReq device info failed", e);
            throw new OceanException(ResultStatus.DATA_DECRYPTION_FAILURE, "Decrypt private key failure.");
        } finally {
            if (decryptResultDTO != null) {
                decryptResultDTO.clearPlainText();
            }
        }
    }

    public CreateOrRenewSslCertificateReqDto renewSslCertificate(
      CreateOrRenewSslCertificateReqDto uploadFileDto) {

        String cardBrandName = uploadFileDto.getCardBrand();

        X509Certificate x509Cert = readX509Cert(uploadFileDto.getFileContent());

        Optional<SslClientCertificateDO> waitingUpdateSslCertOpt =
          sslCertDao.findUnDeleteByActivityAndCardBrandAndKeyStatus(
            cardBrandName, KeyStatus.PROCESSING);
        Optional<SslClientCertificateDO> currentEnableSslCertOpt =
          sslCertDao.findUnDeleteByActivityAndCardBrand(cardBrandName);
        CsrDO csrDO = this.findCsrForSslClientCertOrThrow(x509Cert, cardBrandName);

        // 使用"產生CSR"
        if (waitingUpdateSslCertOpt.isPresent() && currentEnableSslCertOpt.isPresent()) {
            SslClientCertificateDO currentEnableSslCert = currentEnableSslCertOpt.get();
            SslClientCertificateDO waitingUpdateSslCert = waitingUpdateSslCertOpt.get();

            final String password = CertificateUtils.genRandomPassword();
            byte[] p12 =
              CertificateUtils.mergeToP12(
                x509Cert, decryptPrivateKeyString(csrDO.getEncPrivateKey()), password);

            currentEnableSslCert.setDeleteFlag(true);
            currentEnableSslCert.setActivity(Activity.DISABLED);
            currentEnableSslCert.setDeleteMillis(System.currentTimeMillis());
            currentEnableSslCert.setDeleter(uploadFileDto.getUser());
            sslCertDao.save(currentEnableSslCert);

            waitingUpdateSslCert.setPrivateKey(csrDO.getEncPrivateKey());
            waitingUpdateSslCert.setPublicKey(csrDO.getPublicKey());
            waitingUpdateSslCert.setManualPassword(false);
            waitingUpdateSslCert.setKeyPassword(encryptKeyPassword(password));
            waitingUpdateSslCert.setHasCert(true);
            waitingUpdateSslCert.setCertificate(p12);
            waitingUpdateSslCert.setKeyStatus(KeyStatus.COMPLETED);
            waitingUpdateSslCert.setActivity(Activity.ENABLED);
            waitingUpdateSslCert.setAuditStatus(uploadFileDto.getAuditStatus().getSymbol());
            waitingUpdateSslCert.setExpireMillis(x509Cert.getNotAfter().getTime());
            waitingUpdateSslCert.setApplyMillis(x509Cert.getNotBefore().getTime());
            waitingUpdateSslCert.setCertificateInformation(x509Cert.getSubjectDN().getName());
            waitingUpdateSslCert.setIssuer(x509Cert.getIssuerDN().getName());
            waitingUpdateSslCert.setHashAlgorithm(x509Cert.getSigAlgName());
            SslClientCertificateDO sslClientCertificate = sslCertDao.save(waitingUpdateSslCert);

            // update CSR table
            csrDO.setCertUploadMillis(System.currentTimeMillis());
            csrDO.setCertExpireMillis(x509Cert.getNotAfter().getTime());
            csrDO.setCertificate(uploadFileDto.getFileContent());
            csrDO.setUpdater(uploadFileDto.getUser());
            csrDO.setUpdateMillis(System.currentTimeMillis());
            try {
                csrDAO.save(csrDO);
            } catch (DatabaseException e) {
                throw new OceanException(ResultStatus.DB_SAVE_ERROR, e.getMessage(), e);
            }

            return CreateOrRenewSslCertificateReqDto.valueOf(sslClientCertificate);
        }

        // 使用"上傳憑證"
        if (currentEnableSslCertOpt.isPresent()) {
            SslClientCertificateDO currentEnableSslCert = currentEnableSslCertOpt.get();

            final String password = CertificateUtils.genRandomPassword();
            byte[] p12 =
              CertificateUtils.mergeToP12(
                x509Cert, decryptPrivateKeyString(csrDO.getEncPrivateKey()), password);

            currentEnableSslCert.setPublicKey(csrDO.getPublicKey());
            currentEnableSslCert.setPrivateKey(csrDO.getEncPrivateKey());
            currentEnableSslCert.setKeyPassword(encryptKeyPassword(password));
            currentEnableSslCert.setManualPassword(false);
            currentEnableSslCert.setHasCert(true);
            currentEnableSslCert.setCertificate(p12);
            currentEnableSslCert.setKeyStatus(KeyStatus.COMPLETED);
            currentEnableSslCert.setActivity(Activity.ENABLED);
            currentEnableSslCert.setAuditStatus(uploadFileDto.getAuditStatus().getSymbol());
            currentEnableSslCert.setExpireMillis(x509Cert.getNotAfter().getTime());
            currentEnableSslCert.setApplyMillis(x509Cert.getNotBefore().getTime());
            currentEnableSslCert.setCertificateInformation(x509Cert.getSubjectDN().getName());
            currentEnableSslCert.setIssuer(x509Cert.getIssuerDN().getName());
            currentEnableSslCert.setHashAlgorithm(x509Cert.getSigAlgName());
            SslClientCertificateDO sslClientCertificate = sslCertDao.save(currentEnableSslCert);

            // update CSR table
            csrDO.setCertUploadMillis(System.currentTimeMillis());
            csrDO.setCertExpireMillis(x509Cert.getNotAfter().getTime());
            csrDO.setCertificate(uploadFileDto.getFileContent());
            csrDO.setUpdater(uploadFileDto.getUser());
            csrDO.setUpdateMillis(System.currentTimeMillis());
            try {
                csrDAO.save(csrDO);
            } catch (DatabaseException e) {
                throw new OceanException(ResultStatus.DB_SAVE_ERROR, e.getMessage(), e);
            }

            return CreateOrRenewSslCertificateReqDto.valueOf(sslClientCertificate);
        }

        // Unknown
        return null;
    }

    @Nonnull
    private CsrDO findCsrForSslClientCertOrThrow(X509Certificate x509Cert, String cardBrand) {
        String modulusSha256Hex = CertificateUtils
            .calculateRsaModulusSha256Hex((RSAPublicKey) x509Cert.getPublicKey());
        CsrDO csrDO;
        try {
            csrDO = csrDAO
                .findByModulusSha256Hex(
                    CertType.SSL_CLIENT,
                    0,
                    EnvironmentConstants.ORG_ISSUER_BANK_ID,
                    cardBrand,
                    modulusSha256Hex)
                .orElse(null);
        } catch (DatabaseException e) {
            throw new OceanException(ResultStatus.DB_READ_ERROR, e.getMessage(), e);
        }
        if (csrDO == null) {
            log.warn("[findCsrForSslClientCertOrThrow] INVALID_CERTIFICATE, cannot find CSR by the upload certificate. cert={}", x509Cert);
            throw new OceanException(ResultStatus.INVALID_CERTIFICATE);
        }
        return csrDO;
    }

    @Nonnull
    private CsrDO findCsrForSigningCertOrThrow(X509Certificate x509Cert, String cardBrand, int threeDSVersion, Long issuerBankId) {
        String modulusSha256Hex = CertificateUtils
            .calculateRsaModulusSha256Hex((RSAPublicKey) x509Cert.getPublicKey());
        CsrDO csrDO;
        try {
            csrDO = csrDAO
                .findByModulusSha256Hex(
                    CertType.SIGNING,
                    threeDSVersion,
                    issuerBankId,
                    cardBrand,
                    modulusSha256Hex)
                .orElse(null);
        } catch (DatabaseException e) {
            throw new OceanException(ResultStatus.DB_READ_ERROR, e.getMessage(), e);
        }
        if (csrDO == null) {
            log.warn("[findCsrForSigningCertOrThrow] INVALID_CERTIFICATE, cannot find CSR by the upload certificate. cert={}", x509Cert);
            throw new OceanException(ResultStatus.INVALID_CERTIFICATE);
        }
        return csrDO;
    }

    private X509Certificate readX509Cert(byte[] certBytes) throws OceanException {
        try (InputStream inputStream = new ByteArrayInputStream(certBytes)) {
            return CertificateUtils.readCert(inputStream);
        } catch (IOException e) {
            log.error("[readX509Cert] read certBytes error", e);
            throw new OceanException(ResultStatus.IO_EXCEPTION, e.getMessage());
        }
    }

    private String encryptKeyPassword(String password) {
        return hsmPlugin
          .encryptWithIssuerBankId(password, EnvironmentConstants.ORG_ISSUER_BANK_ID).getBase64();
    }

    public CreateOrRenewSslCertificateP12ReqDto renewSslCertificateByP12(
      CreateOrRenewSslCertificateP12ReqDto uploadFileDto) {

        String cardBrand = uploadFileDto.getCardBrand();
        String password = uploadFileDto.getPassword();

        Optional<SslClientCertificateDO> activitySslCertOpt =
          sslCertDao.findUnDeleteByActivityAndCardBrand(cardBrand);
        byte[] p12Bytes = uploadFileDto.getFileContent();

        X509Certificate cert;
        try (ByteArrayInputStream in = new ByteArrayInputStream(p12Bytes)) {
            cert = CertificateUtils.readP12(in, password);
        } catch (IOException e) {
            log.error("[renewSslCertificateByP12] read P12 error", e);
            throw new OceanException(MessageConstants.get(MessageConstants.IO_CLOSE_ERROR));
        }

        if (activitySslCertOpt.isPresent()) {
            SslClientCertificateDO sslCert = activitySslCertOpt.get();
            sslCert.setKeyStatus(KeyStatus.COMPLETED);
            sslCert.setActivity(Activity.ENABLED);
            sslCert.setCertificate(p12Bytes);
            sslCert.setHasCert(true);
            sslCert.setKeyPassword(password);
            sslCert.setManualPassword(true);
            sslCert.setExpireMillis(cert.getNotAfter().getTime());
            sslCert.setApplyMillis(cert.getNotBefore().getTime());
            sslCert.setCertificateInformation(cert.getSubjectDN().getName());
            sslCert.setIssuer(cert.getIssuerDN().getName());
            sslCert.setHashAlgorithm(cert.getSigAlgName());
            sslCert.setAuditStatus(uploadFileDto.getAuditStatus().getSymbol());
            SslClientCertificateDO sslClientCertificate = sslCertDao.save(sslCert);
            return CreateOrRenewSslCertificateP12ReqDto.valueOf(sslClientCertificate);
        }
        return null;
    }

    public boolean existsP7bCertificate(int version, String cardBrand, Long issuerBankId) {
        return signingCertificateDao.existsByCardBrand(version, cardBrand, issuerBankId);
    }

    @Transactional(rollbackFor = Exception.class)
    public CreateSigningCertificateReqDto uploadSigningCertificate(
      CreateSigningCertificateReqDto uploadFileDto) {
        Long issuerBankId = uploadFileDto.getIssuerBankId();

        // 將p7b 檔案拆成三個.pem檔案
        byte[] fileByte = uploadFileDto.getFileContent();
        List<X509Certificate> certificateList = CertificateUtils
          .readPkcs7CertificateChain(new ByteArrayInputStream(fileByte));
        // 憑證鏈層數有可能是 3 (正常 case), 1 (UL 測試)
        if (certificateList.size() == 0) {
            log.warn(
              "[uploadSigningCertificate] Invalid certificate chain. certificate list size={}",
              certificateList.size());
            throw new OceanException("Invalid certificate chain.");
        }
        X509Certificate acsSigningCertificate = certificateList.get(0);
        X509Certificate subCaCertificate = null;
        X509Certificate rootCaCertificate = null;
        if (certificateList.size() == 3) {
            subCaCertificate = certificateList.get(1);
            rootCaCertificate = certificateList.get(2);
        } else if (certificateList.size() == 2) {
            rootCaCertificate = certificateList.get(1);
        }

        String cardBrand = uploadFileDto.getCardBrand();
        String issuerBankCode = uploadFileDto.getIssuerBankCode();
        AuditStatus auditStatus = uploadFileDto.getAuditStatus();
        log.debug(
            "[uploadSigningCertificate] upload signing certificate, cardBrand={}, issuerBankId={}",
            StringUtils.normalizeSpace(cardBrand),
            issuerBankId
        );

        // find CSR
        CsrDO csrDO = this
            .findCsrForSigningCertOrThrow(acsSigningCertificate, cardBrand,
                uploadFileDto.getVersion(), issuerBankId);
        RSAPublicKey rsaPublicKey = (RSAPublicKey) CertificateUtils.readPublicKey(csrDO.getPublicKey());

        // update CSR table
        csrDO.setCertUploadMillis(System.currentTimeMillis());
        csrDO.setCertExpireMillis(acsSigningCertificate.getNotAfter().getTime());
        csrDO.setCertificate(uploadFileDto.getFileContent());
        csrDO.setUpdater(uploadFileDto.getUser());
        csrDO.setUpdateMillis(System.currentTimeMillis());
        try {
            csrDAO.save(csrDO);
        } catch (DatabaseException e) {
            throw new OceanException(ResultStatus.DB_SAVE_ERROR, e.getMessage(), e);
        }

        // 2. Save RSA key info to SigningRsaKey
        log.info("[uploadSigningCertificate] Save RSA key info to SigningRsaKey");
        SigningRsaKeyDO brandNewSingingRsaKey = signingRsaKeyDao.insert(SigningRsaKeyDO.getBuilder()
          .keyId(csrDO.getKeyId())
          .rsaModulus(rsaPublicKey.getModulus().toString(16))
          .rsaPublicExponent(rsaPublicKey.getPublicExponent().toString(16))
          .build());

        // 3. Update SigningCertificate's CurrentRsaKeyId
        log.info("[uploadSigningCertificate] Update SigningCertificate's CurrentRsaKeyId");
        SigningCertificateDO existingSigningCertificate =
          getExistingSigningCertificate(uploadFileDto.getVersion(), cardBrand, issuerBankCode, issuerBankId);
        existingSigningCertificate.setCurrentRsaKeyId(brandNewSingingRsaKey.getId());
        existingSigningCertificate.setUpdateMillis(System.currentTimeMillis());
        signingCertificateDao.update(existingSigningCertificate);

        //儲存憑證鍊
        try {
            boolean uploadResult;
            uploadResult =
              uploadCertificate(
                  uploadFileDto.getVersion(),cardBrand, issuerBankCode, issuerBankId, acsSigningCertificate, auditStatus);
            log.debug("[uploadSigningCertificate] upload certificate result={}", uploadResult);

            if (null == rootCaCertificate) {
                log.warn("[uploadSigningCertificate] certificate chain does not included root-ca");
                uploadResult = signingCertificateDao.cleanRootCertificateId(uploadFileDto.getVersion(), issuerBankId);
                log.debug("[uploadSigningCertificate] clean root-ca certificate id result={}",
                    uploadResult);
            } else {
                uploadResult =
                    uploadRootCaCertificate(uploadFileDto.getVersion(), cardBrand, issuerBankCode, issuerBankId, rootCaCertificate);
                log.debug("[uploadSigningCertificate] upload root-ca certificate result={}",
                    uploadResult);
            }

            if (null == subCaCertificate) {
                log.warn("[uploadSigningCertificate] certificate chain does not included sub-ca");
                uploadResult = signingCertificateDao.cleanSubCertificateId(uploadFileDto.getVersion(), issuerBankId);
                log.debug("[uploadSigningCertificate] clean sub-ca certificate id result={}",
                  uploadResult);
            } else {
                uploadResult = uploadSubCaCertificate(uploadFileDto.getVersion(), cardBrand, issuerBankCode, issuerBankId,
                  subCaCertificate);
                log.debug("[uploadSigningCertificate] upload sub-ca certificate result={}",
                  uploadResult);
            }

        } catch (Exception e) {
            log.error("[uploadSigningCertificate] unknown exception", e);
            throw new OceanException(ResultStatus.SERVER_ERROR, e.getMessage());
        }

        return uploadFileDto;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean uploadCertificate(
      int version,
      String cardBrand,
      String issuerBankCode,
      Long issuerBankId,
      X509Certificate x509Cert,
      AuditStatus auditStatus) {

        CertificateDO certificate;
        try {
            certificate =
              CertificateDO.builder()
                .certificate(x509Cert.getEncoded())
                .information(x509Cert.getSubjectDN().getName())
                .issuer(x509Cert.getIssuerDN().getName())
                .expiryDate(x509Cert.getNotAfter().getTime())
                .applyDate(x509Cert.getNotBefore().getTime())
                .build();

        } catch (CertificateEncodingException e) {
            log.error("[uploadCertificate] error message={}", e.getMessage(), e);
            return false;
        }

        SigningCertificateDO signingCertificate =
          getExistingSigningCertificate(version, cardBrand, issuerBankCode, issuerBankId);

        if (signingCertificate != null) {
            certificate = certificateDao.create(certificate);
            signingCertificate.setAuditStatus(auditStatus.getSymbol());
            signingCertificate.setCurrentCertificateId(certificate.getId());
            signingCertificate = signingCertificateDao.update(signingCertificate);
        }

        return signingCertificate != null;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean uploadRootCaCertificate(
      int version, String cardBrand, String issuerBankCode, Long issuerBankId, X509Certificate x509Cert) {
        RootCaCertificateDO rootCaCertificate;
        try {
            rootCaCertificate =
              RootCaCertificateDO.builder()
                .certificate(x509Cert.getEncoded())
                .information(x509Cert.getSubjectDN().getName())
                .issuer(x509Cert.getIssuerDN().getName())
                .expiryDate(x509Cert.getNotAfter().getTime())
                .applyDate(x509Cert.getNotBefore().getTime())
                .build();
        } catch (CertificateEncodingException e) {
            log.error("[uploadRootCaCertificate] error message={}", e.getMessage(), e);
            throw new OceanException("failed in upload root ca file.");
        }

        SigningCertificateDO signingCertificate =
          getExistingSigningCertificate(version, cardBrand, issuerBankCode, issuerBankId);

        if (signingCertificate != null) {
            rootCaCertificate = rootCaCertificateDao.create(rootCaCertificate);
            signingCertificate.setCurrentRootCaCertificateId(rootCaCertificate.getId());
            signingCertificate = signingCertificateDao.update(signingCertificate);
        }

        return signingCertificate != null;
    }

    private boolean uploadSubCaCertificate(
      int version, String cardBrand, String issuerBankCode, Long issuerBankId, X509Certificate x509Cert) {
        SubCaCertificateDO subCaCertificate;
        try {
            subCaCertificate =
              SubCaCertificateDO.builder()
                .certificate(x509Cert.getEncoded())
                .information(x509Cert.getSubjectDN().getName())
                .issuer(x509Cert.getIssuerDN().getName())
                .expiryDate(x509Cert.getNotAfter().getTime())
                .applyDate(x509Cert.getNotBefore().getTime())
                .build();
        } catch (CertificateEncodingException e) {
            log.error("[uploadSubCaCertificate] error message={}", e.getMessage(), e);
            return false;
        }

        // find existing signingCertifcate
        SigningCertificateDO signingCertificate =
          getExistingSigningCertificate(version, cardBrand, issuerBankCode, issuerBankId);

        if (signingCertificate != null) {
            subCaCertificate = subCaCertificateDao.create(subCaCertificate);
            signingCertificate.setCurrentSubCaCertificateId(subCaCertificate.getId());
            signingCertificate = signingCertificateDao.update(signingCertificate);
        }

        return signingCertificate != null;
    }

    public Optional<CreateSigningCertificateReqDto> findSigningCertificateByCardBrandAndBankCode(
      int version, String cardBrand, Long issuerBankId) {
        return signingCertificateDao
          .findByCardBrandAndIssuerBankId(version, cardBrand, issuerBankId)
          .map(CreateSigningCertificateReqDto::valueOf);
    }

    /**
     * @param cardBrand
     * @param issuerBankCode 非共用憑證的 CardBrand (如 JCB), 必填。共用則免填
     * @param issuerBankId
     * @return
     * @throws NoSuchDataException
     */
    private SigningCertificateDO getExistingSigningCertificate(
      int version, String cardBrand, String issuerBankCode, Long issuerBankId) throws NoSuchDataException {
        // update or create
//        boolean isCardBrandUseGlobalSigningCertificate = cardBrandConfigurationHelper
//          .isCardBrandUseGlobalSigningCertificate(version, cardBrand);
        boolean isCardBrandUseGlobalSigningCertificate = true;

        if (isCardBrandUseGlobalSigningCertificate) {  //useGlobalSigningCertificate == true
            if (EnvironmentConstants.IS_MULTI_ISSUER) {  // multiple issuer.
                return signingCertificateDao.findByCardBrandAndBankId(version, cardBrand,
                        EnvironmentConstants.ORG_ISSUER_BANK_ID).orElseGet(() -> {
                    return signingCertificateDao.create(
                                SigningCertificateDO.builder()
                                    .threeDSVersion(version)
                                    .cardBrand(cardBrand)
                                    .issuerBankId(EnvironmentConstants.ORG_ISSUER_BANK_ID)
                                    .build());
                });
            } else {  // single issuer.
                return signingCertificateDao.findByCardBrandAndBankId(version, cardBrand, issuerBankId).orElseGet(() -> {
                    return signingCertificateDao.create(
                                SigningCertificateDO.builder()
                                    .threeDSVersion(version)
                                    .cardBrand(cardBrand)
                                    .issuerBankId(issuerBankId)
                                    .build());
                });
            }
        } else {
            // useGlobalSigningCertificate == false
            if (StringUtils.isEmpty(issuerBankCode)) {
                throw new OceanException(ResultStatus.MESSAGE_RECEIVED_INVALID,
                  "Missing issuerBankCode parameter");
            }
            return signingCertificateDao
              .findJCBSigningCertificate(version, cardBrand, issuerBankId)
              .orElseGet(
                () ->
                  signingCertificateDao.create(
                    SigningCertificateDO.builder()
                      .threeDSVersion(version)
                      .cardBrand(cardBrand)
                      .issuerBankId(issuerBankId)
                      .build()));
        }
    }

    public SigningCertificateDetailDTO getSigningCertificateByCardBrandAndIssuerBankId(
      SigningCertificateQueryDto signingCertificateReqDto) {
        final String cardBrand = signingCertificateReqDto.getCardBrand();
        final Long issuerBankId = signingCertificateReqDto.getIssuerBankId();
        Optional<SigningCertificateDO> signingCertificateOpt =
          signingCertificateDao.findByCardBrandAndIssuerBankId(signingCertificateReqDto.getVersion(), cardBrand, issuerBankId);
        if (!signingCertificateOpt.isPresent()) {
            log.info(
              "[getSigningCertificateByCardBrandAndIssuerBankId] get no certificate data of IssuerBankId={}.",
              issuerBankId);
            return new SigningCertificateDetailDTO();
        }

        SigningCertificateDO signingCertificate = signingCertificateOpt.get();
        SigningCertificateDetailDTO certificateDetailDTO = new SigningCertificateDetailDTO();
        SigningCertificateDTO certificateDTO = new SigningCertificateDTO();
        SigningCertificateDTO rootCaDTO = new SigningCertificateDTO();
        SigningCertificateDTO subCaDTO = new SigningCertificateDTO();
        certificateDetailDTO.setCertificate(certificateDTO);
        certificateDetailDTO.setRootCaCertificate(rootCaDTO);
        certificateDetailDTO.setSubCaCertificate(subCaDTO);
        certificateDetailDTO.setId(signingCertificate.getId());
        certificateDetailDTO.setAuditStatus(
          AuditStatus.getStatusBySymbol(signingCertificate.getAuditStatus()));

        CertificateRootCaSubCaInfoDto certificateRootCaSubCaInfoDto =
          signingCertificateDao
            .findByCardBrandAndIssuerBankId(signingCertificateReqDto.getVersion(), cardBrand, issuerBankId)
            .map(this::getCertificateRootCaSubCaInfoDto)
            .orElseThrow(
              () -> {
                  log.error(
                    "[getSigningCertificateByCardBrandAndIssuerBankId] get no certificate data of IssuerBankId={}.",
                    issuerBankId);
                  return new OceanException(ResultStatus.NO_SUCH_DATA, "get no certificate data.");
              });

        final long now = System.currentTimeMillis();
        boolean isExpired;
        try {
            Long applyDate = 0L;
            if (certificateRootCaSubCaInfoDto.getCertApplyDate() != null) {
                applyDate = certificateRootCaSubCaInfoDto.getCertApplyDate();
            }
            certificateDTO.setApplyDate(applyDate);
            certificateDTO
              .setCertificateInformation(certificateRootCaSubCaInfoDto.getCertInformation());

            Long expireMillis = 0L;
            if (certificateRootCaSubCaInfoDto.getCertExpiryDate() != null) {
                expireMillis = certificateRootCaSubCaInfoDto.getCertExpiryDate();
            }
            certificateDTO.setExpireDate(expireMillis);
            isExpired = expireMillis < now;
            certificateDTO.setIsExpire(isExpired);
            certificateDTO.setIssuer(certificateRootCaSubCaInfoDto.getCertIssuer());

            applyDate = 0L;
            if (certificateRootCaSubCaInfoDto.getRootCaCertApplyDate() != null) {
                applyDate = certificateRootCaSubCaInfoDto.getRootCaCertApplyDate();
            }
            rootCaDTO.setApplyDate(applyDate);
            rootCaDTO
              .setCertificateInformation(certificateRootCaSubCaInfoDto.getRootCaCertInformation());

            expireMillis = 0L;
            if (certificateRootCaSubCaInfoDto.getRootCaCertExpiryDate() != null) {
                expireMillis = certificateRootCaSubCaInfoDto.getRootCaCertExpiryDate();
            }
            rootCaDTO.setExpireDate(expireMillis);
            isExpired = expireMillis < now;
            rootCaDTO.setIsExpire(isExpired);
            rootCaDTO.setIssuer(certificateRootCaSubCaInfoDto.getRootCaCertIssuer());

            applyDate = 0L;
            if (certificateRootCaSubCaInfoDto.getSubCaCertApplyDate() != null) {
                applyDate = certificateRootCaSubCaInfoDto.getSubCaCertApplyDate();
            }
            subCaDTO.setApplyDate(applyDate);
            subCaDTO
              .setCertificateInformation(certificateRootCaSubCaInfoDto.getSubCaCertInformation());

            expireMillis = 0L;
            if (certificateRootCaSubCaInfoDto.getSubCaCertExpiryDate() != null) {
                expireMillis = certificateRootCaSubCaInfoDto.getSubCaCertExpiryDate();
            }
            subCaDTO.setExpireDate(expireMillis);
            isExpired = expireMillis < now;
            subCaDTO.setIsExpire(isExpired);
            subCaDTO.setIssuer(certificateRootCaSubCaInfoDto.getSubCaCertIssuer());

        } catch (Exception e) {
            log.error("[getSigningCertificateByCardBrandAndIssuerBankId] unknown exception.");
            throw new OceanException("Failed in get signing certificate.");
        }

        return certificateDetailDTO;
    }

    public String generateCsrKey(GenCsrRequestDto genCsrRequestDto, String creator) {
        CsrSignatureAlgorithm csrSignatureAlgorithm = CsrSignatureAlgorithm.SHA256;

        String cardBrand = genCsrRequestDto.getCardBrand();
        String bankCode = getBankCode(genCsrRequestDto.getIssuerBankId());

        // 1. gen RSA key pair
        log.info("[generateCsrKey] generate HSM RSA key pair");
        String kyLabel = hsmPlugin.acsSigningKyLabelFormat(bankCode, cardBrand);
        HSMGenerateKeyResultDTO hsmGenerateKeyResultDTO =
          hsmPlugin.generateRsaKeyPair(kyLabel, genCsrRequestDto.getIssuerBankId());

        String keyId = hsmGenerateKeyResultDTO.getKeyId();
        RSAPublicKey rsaPublicKey = hsmGenerateKeyResultDTO.getRsaPublicKey();
        log.debug(
          "[generateCsrKey] keyId={}, rsaPublicKey={}",
          keyId,
          BaseEncoding.base64().encode(rsaPublicKey.getEncoded()));

        // 4. Prepare CSR signature input
        String dn = Stream.of(
            "C=" + StringUtils.trimToEmpty(genCsrRequestDto.getCountry()),
            "ST=" + StringUtils.trimToEmpty(genCsrRequestDto.getStateOrProvince()),
            "L=" + StringUtils.trimToEmpty(genCsrRequestDto.getLocality()),
            "O=" + StringUtils.trimToEmpty(genCsrRequestDto.getOrganization()),
            "OU=" + StringUtils.trimToEmpty(genCsrRequestDto.getOrganizationUnit()),
            "CN=" + StringUtils.trimToEmpty(genCsrRequestDto.getCommonName()))
          .map(st -> st.replaceAll(",", "\\\\,"))
          .collect(Collectors.joining(","));
        log.debug("[generateCsrKey] DN = {}", StringUtils.normalizeSpace(dn));
        final X500Name x500Name = new X500Name(dn);
        byte[] certReqInfo = CertificateUtils
          .createCertificationRequestInfo(x500Name, rsaPublicKey);

        // 5. Create CSR signature
        log.info("[generateCsrKey] Create CSR signature");
        byte[] csrSignature =
          hsmPlugin.rsaSign(
            csrSignatureAlgorithm.getHsmSignMechanism(),
            keyId,
            certReqInfo,
            genCsrRequestDto.getIssuerBankId());

        // 6. encode CSR
        final String encodedCsr =
          CertificateUtils.encodeCsr(certReqInfo, csrSignature, csrSignatureAlgorithm);
        log.debug("[createCsr] encode csr = {}", encodedCsr);

        // 7. Save to CSR table
        CsrDO csrDO = new CsrDO(
            null,
            genCsrRequestDto.getVersion(),
            genCsrRequestDto.getIssuerBankId(),
            CertType.SIGNING,
            genCsrRequestDto.getCardBrand(),
            encodedCsr,
            null,
            null,
            null,
            keyId,
            null,
            CertificateUtils.writeToString(rsaPublicKey, "Public Key"),
            CertificateUtils.calculateRsaModulusSha256Hex(rsaPublicKey)
        );
        csrDO.setCreator(creator);
        csrDO.setCreateMillis(System.currentTimeMillis());
        try {
            csrDO = csrDAO.save(csrDO);
        } catch (DatabaseException e) {
            throw new OceanException(ResultStatus.DB_SAVE_ERROR, e.getMessage(), e);
        }

        return encodedCsr;
    }

    public List<CertificateInfo> getSigningCertList(int threeDSVersion, Long issuerBankId, String cardBrand) {
        // 1. find csrDOList
        List<CsrDO> csrDOList = null;
        try {
            csrDOList = csrDAO.findAvailableCertificateList(
                CertType.SIGNING, threeDSVersion, issuerBankId, cardBrand);
        } catch (DatabaseException e) {
            throw new OceanException(ResultStatus.DB_READ_ERROR, e.getMessage(), e);
        }
        if (csrDOList.isEmpty()) {
            log.warn("[getSigningCertList] no available certificate");
            return new ArrayList<>();
        }
        List<CertificateInfo> certificateInfoList =
            csrDOList.stream()
                .map(csrDO ->
                    new CertificateInfo(
                        csrDO.getId(),
                        false,
                        csrDO.getCreateMillis(),
                        csrDO.getCertUploadMillis(),
                        csrDO.getCertExpireMillis()))
                .collect(Collectors.toList());

        // 2. find current certificate
        SigningCertificateDO signingCertificate =
            signingCertificateDao.findByCardBrandAndIssuerBankId(threeDSVersion, cardBrand, issuerBankId)
            .orElse(null);
        if (signingCertificate == null) {
            log.warn("[getSigningCertList] cannot find current certificate. threeDSVersion={}, cardBrand={}, issuerBankId={}",
                threeDSVersion,
                StringUtils.normalizeSpace(cardBrand),
                issuerBankId);
        } else {
            Long currentRsaKeyId = signingCertificate.getCurrentRsaKeyId();
            SigningRsaKeyDO signingRsaKeyDO = signingRsaKeyDao.findById(currentRsaKeyId)
                .orElseThrow(() ->
                    new OceanException(ResultStatus.SERVER_ERROR, "[getSigningCertList] cannot find signingRsaKey. id=" + currentRsaKeyId));
            String keyId = signingRsaKeyDO.getKeyId();
            csrDOList.stream()
                .filter(csrDO -> csrDO.getKeyId().equals(keyId))
                .findFirst()
                .ifPresent(currentCsrDO -> certificateInfoList.forEach(certificateInfo -> {
                    if (certificateInfo.getId() == currentCsrDO.getId()) {
                        certificateInfo.setCurrent(true);
                    }
                }));
        }

        return certificateInfoList;
    }

    public List<CertificateInfo> getSslCertList(String cardBrand) {
        // 1. find csrDOList
        List<CsrDO> csrDOList = null;
        try {
            csrDOList = csrDAO.findAvailableCertificateList(
                CertType.SSL_CLIENT, 0, EnvironmentConstants.ORG_ISSUER_BANK_ID, cardBrand);
        } catch (DatabaseException e) {
            throw new OceanException(ResultStatus.DB_READ_ERROR, e.getMessage(), e);
        }
        if (csrDOList.isEmpty()) {
            log.warn("[getSigningCertList] no available certificate");
            return new ArrayList<>();
        }
        List<CertificateInfo> certificateInfoList =
            csrDOList.stream()
                .map(csrDO ->
                    new CertificateInfo(
                        csrDO.getId(),
                        false,
                        csrDO.getCreateMillis(),
                        csrDO.getCertUploadMillis(),
                        csrDO.getCertExpireMillis()))
                .collect(Collectors.toList());

        // 2. find current certificate
        SslClientCertificateDO sslClientCertificateDO = sslCertDao.findUnDeleteByActivityAndCardBrand(cardBrand).orElse(null);
        if (sslClientCertificateDO != null) {
            csrDOList.stream()
                .filter(csrDO -> csrDO.getPublicKey().equals(sslClientCertificateDO.getPublicKey()))
                .findFirst()
                .ifPresent(currentCsrDO -> certificateInfoList.forEach(certificateInfo -> {
                    if (certificateInfo.getId() == currentCsrDO.getId()) {
                        certificateInfo.setCurrent(true);
                    }
                }));
        }

        return certificateInfoList;
    }

    public Optional<CaCertificateDto> findCaCertById(Long id) {
        Optional<CaCertificateDO> result = caCertDao.findById(id);
        return result.map(CaCertificateDto::parseToCaCertificateDto);
    }

    public Optional<CaCertificateDO> findById(Long id) {
        return caCertDao.findById(id);
    }

    public CaCertificateDO updateCaCertAuditStatus(CaCertificateDto caCertificateDto) {
        Optional<CaCertificateDO> caCertificateOpt = caCertDao.findById(caCertificateDto.getId());
        if (!caCertificateOpt.isPresent()) {
            throw new OceanException(
              ResultStatus.NO_SUCH_DATA,
              "can not find Ca certificate by id " + caCertificateDto.getId());
        }
        CaCertificateDO caCertificate = caCertificateOpt.get();
        caCertificate.setUpdateMillis(System.currentTimeMillis());
        caCertificate.setUpdater(caCertificateDto.getUser());
        caCertificate.setAuditStatus(caCertificateDto.getAuditStatus().getSymbol());
        return caCertDao.save(caCertificate);
    }

    public CreateSigningCertificateReqDto updateSigningCertAuditStatus(
      CreateSigningCertificateReqDto uploadDto) {
        SigningCertificateDO signingCertificate =
          signingCertificateDao
            .findById(uploadDto.getId())
            .orElseThrow(
              () ->
                new OceanException(
                  ResultStatus.NO_SUCH_DATA,
                  "can not find signing certificate by id " + uploadDto.getId()));
        signingCertificate.setUpdateMillis(System.currentTimeMillis());
        signingCertificate.setAuditStatus(uploadDto.getAuditStatus().getSymbol());
        return CreateSigningCertificateReqDto
          .valueOf(signingCertificateDao.update(signingCertificate));
    }

    public CreateOrRenewSslCertificateP12ReqDto updateSslCertLockStatus(
      CreateOrRenewSslCertificateP12ReqDto uploadDto) {
        Optional<SslClientCertificateDO> sslClientCertificateDtoOpt =
          sslCertDao.findById(uploadDto.getId());
        if (!sslClientCertificateDtoOpt.isPresent()) {
            throw new OceanException(
              ResultStatus.NO_SUCH_DATA, "can not find ssl certificate by id " + uploadDto.getId());
        }
        SslClientCertificateDO sslClientCertificate = sslClientCertificateDtoOpt.get();
        sslClientCertificate.setUpdateMillis(System.currentTimeMillis());
        sslClientCertificate.setUpdater(uploadDto.getUser());
        sslClientCertificate = sslCertDao.save(sslClientCertificate);
        uploadDto.setId(sslClientCertificate.getId());
        return uploadDto;
    }

    public void updateSslCertLockStatus(CreateOrRenewSslCertificateReqDto updateDto) {
        SslClientCertificateDO sslClientCertificate =
          sslCertDao
            .findById(updateDto.getId())
            .orElseThrow(
              () ->
                new OceanException(
                  ResultStatus.NO_SUCH_DATA,
                  "can not find ssl certificate by id " + updateDto.getId()));
        sslClientCertificate.setAuditStatus(updateDto.getAuditStatus().getSymbol());
        sslClientCertificate.setUpdateMillis(System.currentTimeMillis());
        sslClientCertificate.setUpdater(updateDto.getUser());
        sslCertDao.save(sslClientCertificate);
    }

    private CertificateRootCaSubCaInfoDto getCertificateRootCaSubCaInfoDto(
      SigningCertificateDO signingCertificateOpt) {
        CertificateRootCaSubCaInfoDto.CertificateRootCaSubCaInfoDtoBuilder builder =
          CertificateRootCaSubCaInfoDto.builder();
        if (null == signingCertificateOpt.getCurrentCertificateId()) {
            return builder.build();
        }

        certificateDao
          .findById(signingCertificateOpt.getCurrentCertificateId())
          .ifPresent(
            e ->
              builder
                .certApplyDate(e.getApplyDate())
                .certExpiryDate(e.getExpiryDate())
                .certInformation(e.getInformation())
                .certIssuer(e.getIssuer()));

        if (null != signingCertificateOpt.getCurrentRootCaCertificateId()) {
            rootCaCertificateDao
                .findById(signingCertificateOpt.getCurrentRootCaCertificateId())
                .ifPresent(
                    e ->
                        builder
                            .rootCaCertApplyDate(e.getApplyDate())
                            .rootCaCertExpiryDate(e.getExpiryDate())
                            .rootCaCertInformation(e.getInformation())
                            .rootCaCertIssuer(e.getIssuer()));
        }

        if (null != signingCertificateOpt.getCurrentSubCaCertificateId()) {
            subCaCertificateDao
              .findById(signingCertificateOpt.getCurrentSubCaCertificateId())
              .ifPresent(
                e -> builder
                  .subCaCertApplyDate(e.getApplyDate())
                  .subCaCertExpiryDate(e.getExpiryDate())
                  .subCaCertInformation(e.getInformation())
                  .subCaCertIssuer(e.getIssuer()));
        }
        return builder.build();
    }

    private String getBankCode(Long issuerBankId) throws NoSuchDataException {
        return issuerBankDao
          .findById(issuerBankId)
          .map(IssuerBankDO::getCode)
          .orElseThrow(
            () -> new NoSuchDataException("The issuerBank id:" + issuerBankId + " not found."));
    }
}
