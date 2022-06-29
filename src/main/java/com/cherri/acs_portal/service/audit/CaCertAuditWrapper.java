package com.cherri.acs_portal.service.audit;

import com.cherri.acs_portal.dto.audit.AuditFileDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.system.CaCertificateDto;
import com.cherri.acs_portal.service.CertificateManagementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import ocean.acs.commons.enumerator.AuditStatus;
import org.springframework.stereotype.Service;

@Service
public class CaCertAuditWrapper implements AuditServiceWrapper<CaCertificateDto> {

    private CertificateManagementService certificateManagementService;
    private final ObjectMapper objectMapper;

    public CaCertAuditWrapper(CertificateManagementService certificateManagementService,
      ObjectMapper objectMapper) {
        this.certificateManagementService = certificateManagementService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<CaCertificateDto> add(CaCertificateDto draftData) {
        return Optional.of(certificateManagementService.uploadCaCertificate(draftData));
    }

    @Override
    public boolean delete(DeleteDataDTO draftData) {
        draftData.setAuditStatus(AuditStatus.PUBLISHED);
        certificateManagementService.delete(draftData);
        return true;
    }

    @Override
    public Optional<CaCertificateDto> update(CaCertificateDto draftData) {
        draftData.setAuditStatus(AuditStatus.PUBLISHED);
        return Optional.of(certificateManagementService.uploadCaCertificate(draftData));
    }

    @Override
    public Optional<CaCertificateDto> convertJsonToConcreteDTO(String draftInJson)
      throws IOException {
        return Optional.of(objectMapper.readValue(draftInJson, CaCertificateDto.class));
    }

    @Override
    public Optional<CaCertificateDto> getOriginalDataDTO(AuditableDTO draftData) {
        return Optional.empty();
//        // For Update
//        if (draftData instanceof CaCertificateDto) {
//            String cardBrand = ((CaCertificateDto) draftData).getCardBrand();
//            return certificateManagementService.getCaCertificateByCardBrand(cardBrand);
//        }
//        // For Delete
//        return certificateManagementService.findCaCertById(draftData.getId());
    }

    @Override
    public Optional<AuditFileDTO> getOriginalFileDTO(CaCertificateDto caCertData) {
        return caCertData.getAuditFile();
    }

    @Override
    public Optional<AuditFileDTO> getDraftFileDTO(CaCertificateDto draftData) {
        return draftData.getAuditFile();
    }

    @Override
    public void filterForAuditUsed(CaCertificateDto originalData) {
        originalData.setUser(null);
        originalData.setAuditStatus(null);
    }

    @Override
    public boolean isAuditingLockAvailable(AuditableDTO draftData) {
        Optional<CaCertificateDto> originDataOpt = getOriginalDataDTO(draftData);
        if (!originDataOpt.isPresent()) {
            return false;
        }
        CaCertificateDto originData = originDataOpt.get();
        return originData.getAuditStatus() == AuditStatus.PUBLISHED;
    }

    @Override
    public boolean lockDataAsAuditing(AuditableDTO draftData) {
        return false;
//        Optional<CaCertificateDto> originDataOpt;
//        if (draftData instanceof CaCertificateDto) {
//            String cardBrand = ((CaCertificateDto) draftData).getCardBrand();
//            originDataOpt = certificateManagementService.getCaCertificateByCardBrand(cardBrand);
//        } else {
//            originDataOpt = certificateManagementService.findCaCertById(draftData.getId());
//        }
//
//        CaCertificateDto caCertificateDto =
//          originDataOpt.orElseThrow(
//            () -> getMarkAuditingException("mark", "ca certificate", draftData));
//        caCertificateDto.setAuditStatus(AuditStatus.AUDITING);
//        certificateManagementService.updateCaCertAuditStatus(caCertificateDto);
//        return true;
    }

    @Override
    public boolean unlockDataFromAuditing(AuditableDTO draftData) {
        return false;
//        Optional<CaCertificateDto> originDataOpt;
//        if (draftData instanceof CaCertificateDto) {
//            String cardBrand = ((CaCertificateDto) draftData).getCardBrand();
//            originDataOpt = certificateManagementService.getCaCertificateByCardBrand(cardBrand);
//        } else {
//            originDataOpt = certificateManagementService.findCaCertById(draftData.getId());
//        }
//        CaCertificateDto caCert = originDataOpt.orElseThrow(
//          () -> getMarkAuditingException("unmark", "ca certificate", draftData));
//        caCert.setAuditStatus(AuditStatus.PUBLISHED);
//        certificateManagementService.updateCaCertAuditStatus(caCert);
//        return true;
    }
}
