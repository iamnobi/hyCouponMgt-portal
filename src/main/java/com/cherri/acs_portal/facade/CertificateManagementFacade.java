package com.cherri.acs_portal.facade;

import com.cherri.acs_portal.controller.request.CreateOrRenewSslCertificateP12ReqDto;
import com.cherri.acs_portal.controller.request.CreateOrRenewSslCertificateReqDto;
import com.cherri.acs_portal.controller.request.CreateSigningCertificateReqDto;
import com.cherri.acs_portal.controller.request.GenCsrRequestDto;
import com.cherri.acs_portal.controller.request.SigningCertificateQueryDto;
import com.cherri.acs_portal.controller.response.CertificateInfo;
import com.cherri.acs_portal.controller.response.DataEditResultDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.certificate.CardBrandSigningCertificateConfigDTO;
import com.cherri.acs_portal.dto.system.CaCertificateDto;
import com.cherri.acs_portal.dto.system.GenKeyRequest;
import com.cherri.acs_portal.dto.system.SigningCertificateDetailDTO;
import com.cherri.acs_portal.dto.system.SslClientCertificateDto;
import com.cherri.acs_portal.service.AuditService;
import com.cherri.acs_portal.service.CardBrandConfigurationHelper;
import com.cherri.acs_portal.service.CertificateManagementService;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ocean.acs.commons.enumerator.AuditActionType;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.models.dao.CsrDAO;
import ocean.acs.models.data_object.entity.CsrDO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CertificateManagementFacade {

    private final CertificateManagementService certificateManagementService;
    private final HttpServletResponse response;

    private final AuditService auditService;
    private final CardBrandConfigurationHelper cardBrandConfigurationHelper;
    private final CsrDAO csrDAO;

    @Autowired
    public CertificateManagementFacade(
      CertificateManagementService certificateManagementService, HttpServletResponse response,
      AuditService auditService,
      CardBrandConfigurationHelper cardBrandConfigurationHelper, CsrDAO csrDAO) {
        this.response = response;
        this.certificateManagementService = certificateManagementService;
        this.auditService = auditService;
        this.cardBrandConfigurationHelper = cardBrandConfigurationHelper;
        this.csrDAO = csrDAO;
    }

    public DataEditResultDTO uploadCaCertificate(CaCertificateDto caCertificateDto) {
        try {
            AuditActionType auditFunctionType = AuditActionType.ADD;
            if (certificateManagementService
              .isCaCertificateExists(caCertificateDto.getCardBrand())) {
                auditFunctionType = AuditActionType.UPDATE;
            }

            boolean isDemandAuditing = auditService
              .isAuditingOnDemand(AuditFunctionType.SYS_CA_CERT);
            if (isDemandAuditing) {
                return auditService
                  .requestAudit(AuditFunctionType.SYS_CA_CERT, auditFunctionType, caCertificateDto);
            } else {
                caCertificateDto.setAuditStatus(AuditStatus.PUBLISHED);
                CaCertificateDto result = certificateManagementService
                  .uploadCaCertificate(caCertificateDto);
                return new DataEditResultDTO(result);
            }
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    public DataEditResultDTO deleteCaCertificate(DeleteDataDTO deleteDataDto) {
        try {
            boolean isDemandAuditing = auditService
              .isAuditingOnDemand(AuditFunctionType.SYS_CA_CERT);
            if (isDemandAuditing) {
                return auditService
                  .requestAudit(AuditFunctionType.SYS_CA_CERT, AuditActionType.DELETE,
                    deleteDataDto);
            } else {
                deleteDataDto.setAuditStatus(AuditStatus.PUBLISHED);
                DeleteDataDTO result = certificateManagementService.delete(deleteDataDto);
                return new DataEditResultDTO(result);
            }
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    public List<CaCertificateDto> getCaCertificateByCardBrand(String cardBrand) {
        return certificateManagementService.getCaCertificateByCardBrand(cardBrand);
    }

    public DataEditResultDTO uploadSslCertificateP12(
      CreateOrRenewSslCertificateP12ReqDto uploadFileDto) {
        try {
            boolean isDemandAuditing = auditService
              .isAuditingOnDemand(AuditFunctionType.SYS_SSL_P12_CERT);
            if (isDemandAuditing) {
                return auditService
                  .requestAudit(AuditFunctionType.SYS_SSL_P12_CERT, AuditActionType.ADD,
                    uploadFileDto);
            } else {
                uploadFileDto.setAuditStatus(AuditStatus.PUBLISHED);
                CreateOrRenewSslCertificateP12ReqDto result = certificateManagementService
                  .uploadSslCertificateP12(uploadFileDto);
                return new DataEditResultDTO(result);
            }
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    public String generateSslCertificateKeyAndCSR(GenKeyRequest request) {
        return certificateManagementService.generateSslCertificateKeyAndCSR(request);
    }

    public DataEditResultDTO saveSslCertificate(CreateOrRenewSslCertificateReqDto uploadFileDto) {
        try {
            boolean isDemandAuditing = auditService
              .isAuditingOnDemand(AuditFunctionType.SYS_SSL_CERT);
            if (isDemandAuditing) {
                return auditService
                  .requestAudit(AuditFunctionType.SYS_SSL_CERT, AuditActionType.UPDATE,
                    uploadFileDto);
            } else {
                uploadFileDto.setAuditStatus(AuditStatus.PUBLISHED);
                CreateOrRenewSslCertificateReqDto result = certificateManagementService
                  .saveSslCertificate(uploadFileDto);
                return new DataEditResultDTO(result);
            }
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    public boolean deleteSslCertificateTmpKey(String cardBrandName) {
        return certificateManagementService.deleteSslCertificateTmpKey(cardBrandName);
    }

    public SslClientCertificateDto findSslCertificateByCardBrand(String cardBrandName) {
        return certificateManagementService.findSslCertificateByCardBrand(cardBrandName);
    }

    public void downloadSslCertificateP12(String cardBrandName, String password) {
        certificateManagementService.downloadSslCertificateP12(response, cardBrandName, password);
    }

    public String renewSslCertificateByGenKey(GenKeyRequest request) {
        return certificateManagementService.renewSslCertificateByGenKey(request);
    }

    public DataEditResultDTO renewSslCertificate(CreateOrRenewSslCertificateReqDto uploadFileDto) {
        try {
            boolean isDemandAuditing = auditService
              .isAuditingOnDemand(AuditFunctionType.SYS_SSL_CERT);
            if (isDemandAuditing) {
                return auditService.requestAudit(
                  AuditFunctionType.SYS_SSL_CERT, AuditActionType.UPDATE, uploadFileDto);
            } else {
                uploadFileDto.setAuditStatus(AuditStatus.PUBLISHED);
                CreateOrRenewSslCertificateReqDto result =
                  certificateManagementService.renewSslCertificate(uploadFileDto);
                return new DataEditResultDTO(result);
            }
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    public DataEditResultDTO renewSslCertificateByP12(
      CreateOrRenewSslCertificateP12ReqDto uploadFileDto) {
        try {
            boolean isDemandAuditing = auditService
              .isAuditingOnDemand(AuditFunctionType.SYS_SSL_P12_CERT);
            if (isDemandAuditing) {
                return auditService.requestAudit(
                  AuditFunctionType.SYS_SSL_P12_CERT, AuditActionType.UPDATE, uploadFileDto);
            } else {
                uploadFileDto.setAuditStatus(AuditStatus.PUBLISHED);
                CreateOrRenewSslCertificateP12ReqDto result =
                  certificateManagementService.renewSslCertificateByP12(uploadFileDto);
                return new DataEditResultDTO(result);
            }
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    public DataEditResultDTO uploadSigningCertificate(
      CreateSigningCertificateReqDto uploadFileDto) {
        try {
            boolean isDemandAuditing = auditService
              .isAuditingOnDemand(AuditFunctionType.SYS_SIGNING_CERT);
            if (isDemandAuditing) {
                boolean existsP7bCert =
                  certificateManagementService.existsP7bCertificate(
                      uploadFileDto.getVersion(),
                      uploadFileDto.getCardBrand(),
                      uploadFileDto.getIssuerBankId());
                AuditActionType auditActionType;
                if (existsP7bCert) {
                    auditActionType = AuditActionType.UPDATE;
                } else {
                    auditActionType = AuditActionType.ADD;
                }
                return auditService
                  .requestAudit(AuditFunctionType.SYS_SIGNING_CERT, auditActionType, uploadFileDto);
            } else {
                uploadFileDto.setAuditStatus(AuditStatus.PUBLISHED);
                CreateSigningCertificateReqDto result = certificateManagementService
                  .uploadSigningCertificate(uploadFileDto);
                return new DataEditResultDTO(result);
            }
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    public SigningCertificateDetailDTO getCertificatesByCardBrandAndIssuerBankId(
      SigningCertificateQueryDto signingCertificateQueryDto) {
        return certificateManagementService
          .getSigningCertificateByCardBrandAndIssuerBankId(signingCertificateQueryDto);
    }

    public String generateCsrKey(GenCsrRequestDto genCsrRequestDto, String creator) {
        return certificateManagementService.generateCsrKey(genCsrRequestDto, creator);
    }

    public List<CardBrandSigningCertificateConfigDTO> findCardBrandSigningCertificateConfig() {
        return cardBrandConfigurationHelper.findCardBrandSigningCertificateConfig();
    }

    public List<CertificateInfo> getSigningCertList(int threeDSVersion, Long issuerBankId, String cardBrand) {
        return certificateManagementService.getSigningCertList(threeDSVersion, issuerBankId, cardBrand);
    }
    public List<CertificateInfo> getSslCertList(String cardBrand) {
        return certificateManagementService.getSslCertList(cardBrand);
    }
    public void changeSigningCert(Long certId, int threeDSVersion, Long issuerBankId, String cardBrand, String userAccount) {
        // 1. find certificate
        CsrDO csrDO = null;
        try {
            csrDO = csrDAO
                .findById(certId)
                .orElseThrow(() -> new OceanException(ResultStatus.NO_SUCH_DATA));
        } catch (DatabaseException e) {
            throw new OceanException(ResultStatus.DB_READ_ERROR, e.getMessage(), e);
        }
        if (csrDO.getThreeDSVersion() != threeDSVersion ||
            !csrDO.getIssuerBankId().equals(issuerBankId) ||
            !csrDO.getCardBrand().equals(cardBrand)) {
            log.error("[changeSigningCert] invalid request. certId={}, threeDSVersion={}, issuerBankId={}, cardBrand={}",
                certId, threeDSVersion, issuerBankId, StringUtils.normalizeSpace(cardBrand));
            throw new OceanException(ResultStatus.INVALID_FORMAT);
        }
        byte[] cert = csrDO.getCertificate();

        // 2. upload cert
        CreateSigningCertificateReqDto uploadFileDto = null;
        try {
            uploadFileDto = new CreateSigningCertificateReqDto(
                threeDSVersion, cardBrand, "000", cert, "cert", issuerBankId);
        } catch (IOException e) {
            throw new OceanException(ResultStatus.SERVER_ERROR, e.getMessage(), e);
        }
        uploadFileDto.setIssuerBankId(issuerBankId);
        uploadFileDto.setUser(userAccount);
        uploadFileDto.setAuditStatus(AuditStatus.PUBLISHED);
        certificateManagementService.uploadSigningCertificate(uploadFileDto);
    }
    public void changeSslCert(Long certId, String cardBrand, String userAccount) {
        // 1. find certificate
        CsrDO csrDO = null;
        try {
            csrDO = csrDAO
                .findById(certId)
                .orElseThrow(() -> new OceanException(ResultStatus.NO_SUCH_DATA));
        } catch (DatabaseException e) {
            throw new OceanException(ResultStatus.DB_READ_ERROR, e.getMessage(), e);
        }
        if (!csrDO.getCardBrand().equals(cardBrand)) {
            log.error("[changeSigningCert] invalid request. certId={}, cardBrand={}",
                certId, StringUtils.normalizeSpace(cardBrand));
            throw new OceanException(ResultStatus.INVALID_FORMAT);
        }
        byte[] cert = csrDO.getCertificate();

        // 2. upload cert
        CreateOrRenewSslCertificateReqDto uploadFileDto = null;
        try {
            uploadFileDto = new CreateOrRenewSslCertificateReqDto(null,
                cardBrand, cert, "cert");
        } catch (IOException e) {
            throw new OceanException(ResultStatus.SERVER_ERROR, e.getMessage(), e);
        }
        uploadFileDto.setUser(userAccount);
        uploadFileDto.setAuditStatus(AuditStatus.PUBLISHED);
        certificateManagementService.renewSslCertificate(uploadFileDto);
    }
}
