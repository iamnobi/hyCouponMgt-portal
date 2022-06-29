package com.cherri.acs_portal.service.audit;

import com.cherri.acs_portal.controller.request.CreateOrRenewSslCertificateReqDto;
import com.cherri.acs_portal.dto.audit.AuditFileDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.service.CertificateManagementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.enumerator.KeyStatus;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.models.data_object.entity.SslClientCertificateDO;
import org.springframework.stereotype.Service;

@Service
public class CertSslAuditWrapper implements AuditServiceWrapper<CreateOrRenewSslCertificateReqDto> {

    private CertificateManagementService certificateManagementService;
    private final ObjectMapper objectMapper;

    public CertSslAuditWrapper(
      CertificateManagementService certificateManagementService, ObjectMapper objectMapper) {
        this.certificateManagementService = certificateManagementService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<CreateOrRenewSslCertificateReqDto> add(
      CreateOrRenewSslCertificateReqDto draftData) {
        return Optional.of(certificateManagementService.saveSslCertificate(draftData));
    }

    @Override
    public boolean delete(DeleteDataDTO draftData) {
        return false;
    }

    @Override
    public Optional<CreateOrRenewSslCertificateReqDto> update(
      CreateOrRenewSslCertificateReqDto draftData) {
        Optional<SslClientCertificateDO> disableSslCertOpt =
          certificateManagementService.findUnDeleteByActivityAndCardBrandAndKeyStatus(
            draftData.getCardBrand(), KeyStatus.PROCESSING);
        Optional<SslClientCertificateDO> enableSslCertOpt =
          certificateManagementService.findUnDeleteByActivityAndCardBrand(draftData.getCardBrand());
        boolean isUploadCert = disableSslCertOpt.isPresent() && !enableSslCertOpt.isPresent();
        if (isUploadCert) {
            // 上傳憑證
            return Optional.of(certificateManagementService.saveSslCertificate(draftData));
        }
        // Renew憑證
        return Optional.of(certificateManagementService.renewSslCertificate(draftData));
    }

    @Override
    public Optional<CreateOrRenewSslCertificateReqDto> convertJsonToConcreteDTO(String draftInJson)
      throws IOException {
        return Optional.of(
          objectMapper.readValue(draftInJson, CreateOrRenewSslCertificateReqDto.class));
    }

    @Override
    public Optional<CreateOrRenewSslCertificateReqDto> getOriginalDataDTO(AuditableDTO draftData) {
        CreateOrRenewSslCertificateReqDto dto = (CreateOrRenewSslCertificateReqDto) draftData;
        // 上傳憑證
        Optional<SslClientCertificateDO> sslClientCertificateOpt =
          certificateManagementService.findUnDeleteByActivityAndCardBrandAndKeyStatus(
            dto.getCardBrand(), KeyStatus.PROCESSING);
        if (sslClientCertificateOpt.isPresent()) {
            return sslClientCertificateOpt.map(CreateOrRenewSslCertificateReqDto::valueOf);
        }
        // Renew憑證
        return certificateManagementService
          .findUnDeleteByActivityAndCardBrand(dto.getCardBrand())
          .map(CreateOrRenewSslCertificateReqDto::valueOf);
    }

    @Override
    public Optional<AuditFileDTO> getOriginalFileDTO(
      CreateOrRenewSslCertificateReqDto sslCertData) {
        return sslCertData.getAuditFile();
    }

    @Override
    public Optional<AuditFileDTO> getDraftFileDTO(CreateOrRenewSslCertificateReqDto draftData) {
        return draftData.getAuditFile();
    }

    @Override
    public void filterForAuditUsed(CreateOrRenewSslCertificateReqDto originalData) {
        originalData.setUser(null);
        originalData.setAuditStatus(null);
    }

    @Override
    public boolean isAuditingLockAvailable(AuditableDTO draftData) {
        Optional<CreateOrRenewSslCertificateReqDto> originDataOpt = getOriginalDataDTO(draftData);
        if (!originDataOpt.isPresent()) {
            return false;
        }
        CreateOrRenewSslCertificateReqDto originData = originDataOpt.get();
        return originData.getAuditStatus() == AuditStatus.PUBLISHED;
    }

    @Override
    public boolean lockDataAsAuditing(AuditableDTO draftData) {
        CreateOrRenewSslCertificateReqDto dto = (CreateOrRenewSslCertificateReqDto) draftData;
        CreateOrRenewSslCertificateReqDto originData =
          getOriginalDataDTO(draftData)
            .orElseThrow(
              () ->
                new OceanException(
                  "Failed in mark data as auditing in find Ssl Cert with cardBrand: "
                    + dto.getCardBrand()));
        originData.setAuditStatus(AuditStatus.AUDITING);
        certificateManagementService.updateSslCertLockStatus(originData);
        return true;
    }

    @Override
    public boolean unlockDataFromAuditing(AuditableDTO draftData) {
        CreateOrRenewSslCertificateReqDto dto = (CreateOrRenewSslCertificateReqDto) draftData;
        CreateOrRenewSslCertificateReqDto originData =
          getOriginalDataDTO(draftData)
            .orElseThrow(
              () ->
                new OceanException(
                  "Failed in unmark data as auditing in find Ssl Cert with cardBrand: "
                    + dto.getCardBrand()));
        originData.setAuditStatus(AuditStatus.PUBLISHED);
        certificateManagementService.updateSslCertLockStatus(originData);
        return true;
    }
}
