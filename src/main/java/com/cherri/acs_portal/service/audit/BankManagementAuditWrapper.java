package com.cherri.acs_portal.service.audit;

import com.cherri.acs_portal.dto.audit.AuditFileDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.bank.IssuerBankDto;
import com.cherri.acs_portal.service.BankManagementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.exception.OceanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 銀行管理審核Wrapper
 */
@Service
public class BankManagementAuditWrapper implements AuditServiceWrapper<IssuerBankDto> {

    private final BankManagementService bankManagementService;
    private final ObjectMapper objectMapper;

    @Autowired
    public BankManagementAuditWrapper(BankManagementService bankManagementService,
      ObjectMapper objectMapper) {
        this.bankManagementService = bankManagementService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<IssuerBankDto> add(IssuerBankDto draftData) {
        draftData.setAuditStatus(AuditStatus.PUBLISHED);
        return Optional.of(bankManagementService.createIssuerBank(draftData));
    }

    @Override
    public boolean delete(DeleteDataDTO draftData) {
        draftData.setAuditStatus(AuditStatus.PUBLISHED);
        bankManagementService.deleteIssuerBank(draftData);
        return true;
    }

    @Override
    public Optional<IssuerBankDto> update(IssuerBankDto draftData) {
        draftData.setAuditStatus(AuditStatus.PUBLISHED);
        return Optional.of(bankManagementService.updateIssuerBank(draftData));
    }

    @Override
    public Optional<IssuerBankDto> convertJsonToConcreteDTO(String draftInJson) throws IOException {
        return Optional.of(objectMapper.readValue(draftInJson, IssuerBankDto.class));
    }

    @Override
    public Optional<IssuerBankDto> getOriginalDataDTO(AuditableDTO draftData) {
        return bankManagementService.getIssuerBankById(draftData.getId());
    }

    @Override
    public Optional<AuditFileDTO> getOriginalFileDTO(IssuerBankDto draftData) {
        return Optional.empty();
    }

    @Override
    public Optional<AuditFileDTO> getDraftFileDTO(IssuerBankDto draftData) {
        return Optional.empty();
    }

    @Override
    public void filterForAuditUsed(IssuerBankDto originalData) {
        originalData.setUser(null);
        originalData.setAuditStatus(null);
    }

    @Override
    public boolean isAuditingLockAvailable(AuditableDTO draftData) {
        Optional<IssuerBankDto> originDataOpt = getOriginalDataDTO(draftData);
        if (!originDataOpt.isPresent()) {
            return false;
        }

        IssuerBankDto originData = originDataOpt.get();
        return originData.getAuditStatus() == AuditStatus.PUBLISHED;
    }

    @Override
    public boolean lockDataAsAuditing(AuditableDTO draftData) {
        Optional<IssuerBankDto> originDataOpt = getOriginalDataDTO(draftData);
        if (!originDataOpt.isPresent()) {
            throw new OceanException(
              "Failed in mark data as auditing in update issuer Bank with id: " + draftData.getId()
                + ", issuerBankId: " + draftData.getIssuerBankId());
        }

        IssuerBankDto originData = originDataOpt.get();
        originData.setAuditStatus(AuditStatus.AUDITING);
        bankManagementService.updateIssuerBank(originData);

        return true;
    }

    @Override
    public boolean unlockDataFromAuditing(AuditableDTO draftData) {
        Optional<IssuerBankDto> originDataOpt = getOriginalDataDTO(draftData);
        if (!originDataOpt.isPresent()) {
            throw new OceanException(
              "Failed in mark data as auditing in update issuer bank with id: " + draftData.getId()
                + ", issuerBankId: " + draftData.getIssuerBankId());
        }
        IssuerBankDto originData = originDataOpt.get();
        originData.setAuditStatus(AuditStatus.PUBLISHED);
        bankManagementService.updateIssuerBank(originData);

        return true;
    }
}
