package com.cherri.acs_portal.service.audit;

import com.cherri.acs_portal.dto.audit.AuditFileDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.bank.IssuerBankAdminDto;
import com.cherri.acs_portal.service.UserManagementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.exception.OceanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 銀行管理者審核Wrapper
 */
@Service
public class BankAdminAuditWrapper implements AuditServiceWrapper<IssuerBankAdminDto> {

    private final UserManagementService userManagementService;
    private final ObjectMapper objectMapper;

    @Autowired
    public BankAdminAuditWrapper(UserManagementService userManagementService,
      ObjectMapper objectMapper) {
        this.userManagementService = userManagementService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<IssuerBankAdminDto> add(IssuerBankAdminDto draftData) {
        draftData.setAuditStatus(AuditStatus.PUBLISHED);
        return Optional.of(userManagementService.createIssuerBankAdmin(draftData));
    }

    @Override
    public boolean delete(DeleteDataDTO draftData) {
        draftData.setAuditStatus(AuditStatus.PUBLISHED);
        userManagementService.deleteIssuerBankAdmin(draftData);
        return true;
    }

    @Override
    public Optional<IssuerBankAdminDto> update(IssuerBankAdminDto draftData) {
        draftData.setAuditStatus(AuditStatus.PUBLISHED);
        return Optional.of(userManagementService.updateIssuerBankAdmin(draftData));
    }

    @Override
    public Optional<IssuerBankAdminDto> convertJsonToConcreteDTO(String draftInJson)
      throws IOException {
        return Optional.of(objectMapper.readValue(draftInJson, IssuerBankAdminDto.class));
    }

    @Override
    public Optional<IssuerBankAdminDto> getOriginalDataDTO(AuditableDTO draftData) {
        return userManagementService.getIssuerBankAdminById(draftData.getId());
    }

    @Override
    public Optional<AuditFileDTO> getOriginalFileDTO(IssuerBankAdminDto draftData) {
        return Optional.empty();
    }

    @Override
    public Optional<AuditFileDTO> getDraftFileDTO(IssuerBankAdminDto draftData) {
        return Optional.empty();
    }

    @Override
    public void filterForAuditUsed(IssuerBankAdminDto originalData) {
        originalData.setUser(null);
        originalData.setAuditStatus(null);
    }

    @Override
    public boolean isAuditingLockAvailable(AuditableDTO draftData) {
        Optional<IssuerBankAdminDto> originDataOpt = getOriginalDataDTO(draftData);
        if (!originDataOpt.isPresent()) {
            return false;
        }

        IssuerBankAdminDto originData = originDataOpt.get();
        return originData.getAuditStatus() == AuditStatus.PUBLISHED;
    }

    @Override
    public boolean lockDataAsAuditing(AuditableDTO draftData) {
        Optional<IssuerBankAdminDto> originDataOpt = userManagementService
          .getIssuerBankAdminById(draftData.getId());
        if (!originDataOpt.isPresent()) {
            throw new OceanException(
              "Failed in mark data as auditing in update issuer Bank Admin with id: " + draftData
                .getId() + ", issuerBankId: " + draftData.getIssuerBankId());
        }

        IssuerBankAdminDto originData = originDataOpt.get();
        originData.setAuditStatus(AuditStatus.AUDITING);
        userManagementService.updateIssuerBankAdminAuditStatus(originData);
        return true;
    }

    @Override
    public boolean unlockDataFromAuditing(AuditableDTO draftData) {
        Optional<IssuerBankAdminDto> originDataOpt = userManagementService
          .getIssuerBankAdminById(draftData.getId());
        if (!originDataOpt.isPresent()) {
            throw new OceanException(
              "Failed in mark data as auditing in update issuer Bank Admin with id: " + draftData
                .getId() + ", issuerBankId: " + draftData.getIssuerBankId());
        }

        IssuerBankAdminDto originData = originDataOpt.get();
        originData.setAuditStatus(AuditStatus.PUBLISHED);
        userManagementService.updateIssuerBankAdminAuditStatus(originData);

        return true;
    }
}
