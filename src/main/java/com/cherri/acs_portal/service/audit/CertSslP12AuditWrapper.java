package com.cherri.acs_portal.service.audit;

import com.cherri.acs_portal.controller.request.CreateOrRenewSslCertificateP12ReqDto;
import com.cherri.acs_portal.dto.audit.AuditFileDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.service.CertificateManagementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.exception.OceanException;
import org.springframework.stereotype.Service;

@Service
public class CertSslP12AuditWrapper implements
  AuditServiceWrapper<CreateOrRenewSslCertificateP12ReqDto> {

    private CertificateManagementService certificateManagementService;
    private final ObjectMapper objectMapper;

    public CertSslP12AuditWrapper(CertificateManagementService certificateManagementService,
      ObjectMapper objectMapper) {
        this.certificateManagementService = certificateManagementService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<CreateOrRenewSslCertificateP12ReqDto> add(
      CreateOrRenewSslCertificateP12ReqDto draftData) {
        return Optional.of(certificateManagementService.uploadSslCertificateP12(draftData));
    }

    @Override
    public boolean delete(DeleteDataDTO draftData) {
        return false;
    }

    @Override
    public Optional<CreateOrRenewSslCertificateP12ReqDto> update(
      CreateOrRenewSslCertificateP12ReqDto draftData) {
        return Optional.of(certificateManagementService.renewSslCertificateByP12(draftData));
    }

    @Override
    public Optional<CreateOrRenewSslCertificateP12ReqDto> convertJsonToConcreteDTO(
      String draftInJson) throws IOException {
        return Optional
          .of(objectMapper.readValue(draftInJson, CreateOrRenewSslCertificateP12ReqDto.class));
    }

    @Override
    public Optional<CreateOrRenewSslCertificateP12ReqDto> getOriginalDataDTO(
      AuditableDTO draftData) {
        Optional<CreateOrRenewSslCertificateP12ReqDto> sslClientCertificateOpt = certificateManagementService
          .findSslP12CertById(draftData.getId());
        return sslClientCertificateOpt;
    }

    @Override
    public Optional<AuditFileDTO> getOriginalFileDTO(
      CreateOrRenewSslCertificateP12ReqDto sslP12CertData) {
        return sslP12CertData.getAuditFile();
    }

    @Override
    public Optional<AuditFileDTO> getDraftFileDTO(CreateOrRenewSslCertificateP12ReqDto draftData) {
        return draftData.getAuditFile();
    }

    @Override
    public void filterForAuditUsed(CreateOrRenewSslCertificateP12ReqDto originalData) {
        originalData.setUser(null);
        originalData.setAuditStatus(null);
    }

    @Override
    public boolean isAuditingLockAvailable(AuditableDTO draftData) {
        Optional<CreateOrRenewSslCertificateP12ReqDto> originDataOpt = getOriginalDataDTO(
          draftData);
        if (!originDataOpt.isPresent()) {
            return false;
        }
        CreateOrRenewSslCertificateP12ReqDto originData = originDataOpt.get();
        return originData.getAuditStatus() == AuditStatus.PUBLISHED;
    }

    @Override
    public boolean lockDataAsAuditing(AuditableDTO draftData) {
        Optional<CreateOrRenewSslCertificateP12ReqDto> originDataOpt = certificateManagementService
          .findSslP12CertById(draftData.getId());
        if (!originDataOpt.isPresent()) {
            throw new OceanException(
              "Failed in mark data as auditing in find Ssl Cert with id: " + draftData.getId());
        }
        CreateOrRenewSslCertificateP12ReqDto sslClientCertificate = originDataOpt.get();
        sslClientCertificate.setAuditStatus(AuditStatus.AUDITING);
        certificateManagementService.updateSslCertLockStatus(sslClientCertificate);
        return true;
    }

    @Override
    public boolean unlockDataFromAuditing(AuditableDTO draftData) {
        Optional<CreateOrRenewSslCertificateP12ReqDto> originDataOpt = certificateManagementService
          .findSslP12CertById(draftData.getId());
        if (!originDataOpt.isPresent()) {
            throw new OceanException(
              "Failed in mark data as auditing in find Ssl Cert with id: " + draftData.getId());
        }
        CreateOrRenewSslCertificateP12ReqDto uploadDto = originDataOpt.get();
        uploadDto.setAuditStatus(AuditStatus.PUBLISHED);
        certificateManagementService.uploadSslCertificateP12(uploadDto);
        return true;
    }

}
