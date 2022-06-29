package com.cherri.acs_portal.service.audit;

import com.cherri.acs_portal.dto.audit.AuditFileDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.system.ErrorIssueGroupDTO;
import com.cherri.acs_portal.service.SystemSettingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ErrorCodeGroupAuditWrapper implements AuditServiceWrapper<ErrorIssueGroupDTO> {

    private final SystemSettingService systemSettingService;
    private final ObjectMapper objectMapper;

    @Autowired
    public ErrorCodeGroupAuditWrapper(
      SystemSettingService systemSettingService, ObjectMapper objectMapper) {
        this.systemSettingService = systemSettingService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<ErrorIssueGroupDTO> add(ErrorIssueGroupDTO draftData) {
        // No add function in code group
        return Optional.empty();
    }

    @Override
    public boolean delete(DeleteDataDTO draftData) {
        // no delete function in code group
        return false;
    }

    @Override
    public Optional<ErrorIssueGroupDTO> update(ErrorIssueGroupDTO draftData) {
        return Optional.of(systemSettingService.updateErrorCodeMessageGroup(draftData));
    }

    @Override
    public Optional<ErrorIssueGroupDTO> convertJsonToConcreteDTO(String draftInJson)
      throws IOException {
        return Optional.of(objectMapper.readValue(draftInJson, ErrorIssueGroupDTO.class));
    }

    @Override
    public Optional<ErrorIssueGroupDTO> getOriginalDataDTO(AuditableDTO draftData) {
        return systemSettingService.getErrorCodeByGroupId(draftData.getId());
    }

    @Override
    public Optional<AuditFileDTO> getOriginalFileDTO(ErrorIssueGroupDTO draftData) {
        return Optional.empty();
    }

    @Override
    public Optional<AuditFileDTO> getDraftFileDTO(ErrorIssueGroupDTO draftData) {
        return Optional.empty();
    }

    @Override
    public void filterForAuditUsed(ErrorIssueGroupDTO originalData) {

    }

    @Override
    public boolean isAuditingLockAvailable(AuditableDTO draftData) {
        Optional<ErrorIssueGroupDTO> getOriginalDataOpt = getOriginalDataDTO(draftData);
        if (!getOriginalDataOpt.isPresent()) {
            return false;
        }

        ErrorIssueGroupDTO originalData = getOriginalDataOpt.get();

        return originalData.getAuditStatus() == AuditStatus.PUBLISHED;
    }

    @Override
    public boolean lockDataAsAuditing(AuditableDTO draftData) {
        Optional<ErrorIssueGroupDTO> originalDataOpt = getOriginalDataDTO(draftData);
        if (!originalDataOpt.isPresent()) {
            throw new OceanException(ResultStatus.NO_SUCH_DATA,
              "Failed in mark data as auditing in update bin range with binId: " + draftData.getId()
                + ", issuerBankId: " + draftData.getIssuerBankId());
        }

        ErrorIssueGroupDTO originalData = originalDataOpt.get();
        originalData.setAuditStatus(AuditStatus.AUDITING);
        systemSettingService.updateErrorCodeMessageGroup(originalData);

        return true;
    }

    @Override
    public boolean unlockDataFromAuditing(AuditableDTO draftData) {
        Optional<ErrorIssueGroupDTO> originalDataOpt = getOriginalDataDTO(draftData);
        if (!originalDataOpt.isPresent()) {
            throw new OceanException(ResultStatus.NO_SUCH_DATA,
              "Failed in unmark data from auditing in update bin range with binId: " + draftData
                .getId() + ", issuerBankId: " + draftData.getIssuerBankId());
        }

        ErrorIssueGroupDTO originalBinData = originalDataOpt.get();
        originalBinData.setAuditStatus(AuditStatus.PUBLISHED);
        systemSettingService.updateErrorCodeMessageGroup(originalBinData);

        return true;
    }
}
