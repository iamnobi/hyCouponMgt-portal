package com.cherri.acs_portal.service.audit;

import com.cherri.acs_portal.dto.audit.AuditFileDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.bank.IssuerLogoUpdateDto;
import com.cherri.acs_portal.service.BankManagementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.exception.NoSuchDataException;
import ocean.acs.commons.exception.OceanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 銀行費用設定
 */
@Service
public class IssuerBrandLogoAuditWrapper implements AuditServiceWrapper<IssuerLogoUpdateDto> {

    @Autowired
    BankManagementService bankManagementService;
    @Autowired
    ObjectMapper objectMapper;

    @Override
    public Optional<IssuerLogoUpdateDto> add(IssuerLogoUpdateDto draftData) {
        throw new UnsupportedOperationException("Not implemented yet...");
    }

    @Override
    public boolean delete(DeleteDataDTO draftData) {
        throw new UnsupportedOperationException("Not implemented yet...");
    }

    @Override
    public Optional<IssuerLogoUpdateDto> update(IssuerLogoUpdateDto draftData) {
        return Optional.of(bankManagementService.updateIssuerBrandLogo(draftData));
    }

    @Override
    public Optional<IssuerLogoUpdateDto> convertJsonToConcreteDTO(String draftInJson)
      throws IOException {
        return Optional.of(objectMapper.readValue(draftInJson, IssuerLogoUpdateDto.class));
    }

    @Override
    public Optional<IssuerLogoUpdateDto> getOriginalDataDTO(AuditableDTO draftData)
      throws OceanException {
        IssuerLogoUpdateDto dto =
          bankManagementService
            .findOneIssuerBrandLogo(draftData.getIssuerBankId())
            .orElseThrow(() -> new NoSuchDataException(
              "Not found by IssuerBankId:" + draftData.getIssuerBankId()));
        return Optional.of(dto);
    }

    @Override
    public Optional<AuditFileDTO> getOriginalFileDTO(IssuerLogoUpdateDto draftData) {
        return draftData.getAuditFile();
    }

    @Override
    public Optional<AuditFileDTO> getDraftFileDTO(IssuerLogoUpdateDto draftData) {
        return draftData.getAuditFile();
    }

    @Override
    public void filterForAuditUsed(IssuerLogoUpdateDto originalData) {
        originalData.setAuditStatus(null);
        originalData.setUser(null);
    }

    @Override
    public boolean isAuditingLockAvailable(AuditableDTO draftData) {
        Optional<IssuerLogoUpdateDto> originalDataOpt = getOriginalDataDTO(draftData);
        return originalDataOpt.map(o -> AuditStatus.PUBLISHED.equals(o.getAuditStatus()))
          .orElse(false);
    }

    @Override
    public boolean lockDataAsAuditing(AuditableDTO draftData) {
        IssuerLogoUpdateDto originalData =
          bankManagementService
            .findOneIssuerBrandLogo(draftData.getIssuerBankId())
            .orElseThrow(() -> getMarkAuditingException("mark", "issuerBrandLogo", draftData));

        originalData.setAuditStatus(AuditStatus.AUDITING);
        bankManagementService.updateIssuerBrandLogo(originalData);
        return true;
    }

    @Override
    public boolean unlockDataFromAuditing(AuditableDTO draftData) {
        IssuerLogoUpdateDto originalData =
          bankManagementService
            .findOneIssuerBrandLogo(draftData.getIssuerBankId())
            .orElseThrow(() -> getMarkAuditingException("unmark", "issuerBrandLogo", draftData));

        originalData.setAuditStatus(AuditStatus.PUBLISHED);
        bankManagementService.updateIssuerBrandLogo(originalData);
        return true;
    }
}
