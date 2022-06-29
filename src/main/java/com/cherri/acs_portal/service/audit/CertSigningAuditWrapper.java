package com.cherri.acs_portal.service.audit;

import com.cherri.acs_portal.controller.request.CreateSigningCertificateReqDto;
import com.cherri.acs_portal.dto.audit.AuditFileDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.service.CertificateManagementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanException;
import org.springframework.stereotype.Service;

@Service
public class CertSigningAuditWrapper implements
  AuditServiceWrapper<CreateSigningCertificateReqDto> {

    private CertificateManagementService certificateManagementService;
    private final ObjectMapper objectMapper;

    public CertSigningAuditWrapper(CertificateManagementService certificateManagementService,
      ObjectMapper objectMapper) {
        this.certificateManagementService = certificateManagementService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<CreateSigningCertificateReqDto> add(CreateSigningCertificateReqDto draftData) {
        return saveOrUpdate(draftData);
    }

    private Optional<CreateSigningCertificateReqDto> saveOrUpdate(
      CreateSigningCertificateReqDto draftData) {
        draftData.setAuditStatus(AuditStatus.PUBLISHED);
        try {
            return Optional.of(certificateManagementService.uploadSigningCertificate(draftData));
        } catch (Exception e) {
            throw new OceanException(ResultStatus.SERVER_ERROR,
              "failed in upload Signing certificate file.");
        }
    }

    @Override
    public boolean delete(DeleteDataDTO draftData) {
        return false;
    }

    @Override
    public Optional<CreateSigningCertificateReqDto> update(
      CreateSigningCertificateReqDto draftData) {
        return saveOrUpdate(draftData);
    }

    @Override
    public Optional<CreateSigningCertificateReqDto> convertJsonToConcreteDTO(String draftInJson)
      throws IOException {
        return Optional
          .of(objectMapper.readValue(draftInJson, CreateSigningCertificateReqDto.class));
    }

    @Override
    public Optional<CreateSigningCertificateReqDto> getOriginalDataDTO(AuditableDTO draftData) {
        CreateSigningCertificateReqDto reqDto = (CreateSigningCertificateReqDto) draftData;
        return certificateManagementService
          .findSigningCertificateByCardBrandAndBankCode(reqDto.getVersion(), reqDto.getCardBrand(),
            reqDto.getIssuerBankId());
    }

    @Override
    public Optional<AuditFileDTO> getOriginalFileDTO(CreateSigningCertificateReqDto draftData) {
        return draftData.getAuditFile();
    }

    @Override
    public Optional<AuditFileDTO> getDraftFileDTO(CreateSigningCertificateReqDto draftData) {
        return draftData.getAuditFile();
    }

    @Override
    public void filterForAuditUsed(CreateSigningCertificateReqDto originalData) {
        originalData.setUser(null);
        originalData.setAuditStatus(null);
    }

    @Override
    public boolean isAuditingLockAvailable(AuditableDTO draftData) {
        Optional<CreateSigningCertificateReqDto> originDataOpt = getOriginalDataDTO(draftData);
        return originDataOpt
          .map(o -> AuditStatus.PUBLISHED == o.getAuditStatus() ||
            AuditStatus.UNKNOWN == o.getAuditStatus())
          .orElse(false);
    }

    @Override
    public boolean lockDataAsAuditing(AuditableDTO draftData) {
        CreateSigningCertificateReqDto createSigningCertificateReqDto = getOriginalDataDTO(
          draftData)
          .orElseThrow(() -> getMarkAuditingException("mark", "signingCertificate", draftData));

        createSigningCertificateReqDto.setAuditStatus(AuditStatus.AUDITING);
        certificateManagementService.updateSigningCertAuditStatus(createSigningCertificateReqDto);
        return true;
    }

    @Override
    public boolean unlockDataFromAuditing(AuditableDTO draftData) {
        CreateSigningCertificateReqDto createSigningCertificateReqDto = getOriginalDataDTO(
          draftData)
          .orElseThrow(() -> getMarkAuditingException("unmark", "signingCertificate", draftData));
        createSigningCertificateReqDto.setAuditStatus(AuditStatus.PUBLISHED);
        certificateManagementService.updateSigningCertAuditStatus(createSigningCertificateReqDto);
        return true;
    }
}
