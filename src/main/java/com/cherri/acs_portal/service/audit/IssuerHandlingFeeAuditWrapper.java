package com.cherri.acs_portal.service.audit;

import com.cherri.acs_portal.dto.audit.AuditFileDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.bank.IssuerHandingFeeUpdateDto;
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
public class IssuerHandlingFeeAuditWrapper
  implements AuditServiceWrapper<IssuerHandingFeeUpdateDto> {

    @Autowired
    BankManagementService bankManagementService;
    @Autowired
    ObjectMapper objectMapper;

    @Override
    public Optional<IssuerHandingFeeUpdateDto> add(IssuerHandingFeeUpdateDto draftData) {
        throw new UnsupportedOperationException("Not implemented yet...");
    }

    @Override
    public boolean delete(DeleteDataDTO draftData) {
        throw new UnsupportedOperationException("Not implemented yet...");
    }

    @Override
    public Optional<IssuerHandingFeeUpdateDto> update(IssuerHandingFeeUpdateDto draftData) {
        return Optional.of(bankManagementService.updateIssuerHandingFee(draftData));
    }

    @Override
    public Optional<IssuerHandingFeeUpdateDto> convertJsonToConcreteDTO(String draftInJson)
      throws IOException {
        return Optional.of(objectMapper.readValue(draftInJson, IssuerHandingFeeUpdateDto.class));
    }

    @Override
    public Optional<IssuerHandingFeeUpdateDto> getOriginalDataDTO(AuditableDTO draftData)
      throws OceanException {
        IssuerHandingFeeUpdateDto dto =
          bankManagementService
            .findOneIssuerHandingFee(draftData.getId())
            .orElseThrow(() -> new NoSuchDataException("Id:" + draftData.getId() + " not found."));
        return Optional.of(dto);
    }

    @Override
    public Optional<AuditFileDTO> getOriginalFileDTO(IssuerHandingFeeUpdateDto draftData) {
        return Optional.empty();
    }

    @Override
    public Optional<AuditFileDTO> getDraftFileDTO(IssuerHandingFeeUpdateDto draftData) {
        return Optional.empty();
    }

    @Override
    public void filterForAuditUsed(IssuerHandingFeeUpdateDto originalData) {
        originalData.setAuditStatus(null);
        originalData.setUser(null);
    }

    @Override
    public boolean isAuditingLockAvailable(AuditableDTO draftData) {
        Optional<IssuerHandingFeeUpdateDto> originalDataOpt = getOriginalDataDTO(draftData);
        return originalDataOpt.map(o -> AuditStatus.PUBLISHED.equals(o.getAuditStatus()))
          .orElse(false);
    }

    @Override
    public boolean lockDataAsAuditing(AuditableDTO draftData) {
        IssuerHandingFeeUpdateDto originalData =
          bankManagementService
            .findOneIssuerHandingFee(draftData.getId())
            .orElseThrow(() -> getMarkAuditingException("mark", "issuerHandlingFee", draftData));

        originalData.setAuditStatus(AuditStatus.AUDITING);
        bankManagementService.updateIssuerHandingFee(originalData);
        return true;
    }

    @Override
    public boolean unlockDataFromAuditing(AuditableDTO draftData) {
        IssuerHandingFeeUpdateDto originalData =
          bankManagementService
            .findOneIssuerHandingFee(draftData.getId())
            .orElseThrow(() -> getMarkAuditingException("unmark", "issuerHandlingFee", draftData));

        originalData.setAuditStatus(AuditStatus.PUBLISHED);
        bankManagementService.updateIssuerHandingFee(originalData);
        return true;
    }
}
