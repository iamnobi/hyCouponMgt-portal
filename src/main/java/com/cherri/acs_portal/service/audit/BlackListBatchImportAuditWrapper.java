package com.cherri.acs_portal.service.audit;

import com.cherri.acs_portal.dto.audit.AuditFileDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.blackList.input.BatchImportDTO;
import com.cherri.acs_portal.service.BatchImportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import ocean.acs.commons.enumerator.AuditStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlackListBatchImportAuditWrapper implements AuditServiceWrapper<BatchImportDTO> {

    private final ObjectMapper objectMapper;
    private final BatchImportService batchImportService;

    @Autowired
    public BlackListBatchImportAuditWrapper(
      ObjectMapper objectMapper, BatchImportService batchImportService) {
        this.objectMapper = objectMapper;
        this.batchImportService = batchImportService;
    }

    @Override
    public Optional<BatchImportDTO> add(BatchImportDTO draftData) {
//        draftData.setAuditStatus(AuditStatus.PUBLISHED);
//        return Optional.of(batchImportService.addBlackListByBatch(draftData));
        return null;
    }

    @Override
    public boolean delete(DeleteDataDTO draftData) {
        batchImportService.deletePanBlackListBatch(draftData);
        return true;
    }

    @Override
    public Optional<BatchImportDTO> update(BatchImportDTO draftData) {
        return Optional.ofNullable(batchImportService.updatePanBlackListBatch(draftData));
    }

    @Override
    public Optional<BatchImportDTO> convertJsonToConcreteDTO(String draftInJson)
      throws IOException {
        return Optional.of(objectMapper.readValue(draftInJson, BatchImportDTO.class));
    }

    @Override
    public Optional<BatchImportDTO> getOriginalDataDTO(AuditableDTO draftData) {
        return batchImportService.getPanBlackListBatchById(draftData.getId());
    }

    @Override
    public Optional<AuditFileDTO> getOriginalFileDTO(BatchImportDTO importDTO) {
        return importDTO.getAuditFile();
    }

    @Override
    public Optional<AuditFileDTO> getDraftFileDTO(BatchImportDTO draftData) {
        return draftData.getAuditFile();
    }

    @Override
    public void filterForAuditUsed(BatchImportDTO originalData) {
        originalData.setAuthStatus(null);
        originalData.setCreateMillis(null);
        originalData.setUser(null);
    }

    @Override
    public boolean isAuditingLockAvailable(AuditableDTO draftData) {
        Optional<BatchImportDTO> importDTOOpt = getOriginalDataDTO(draftData);
        return importDTOOpt.isPresent()
          && importDTOOpt.get().getAuditStatus() == AuditStatus.PUBLISHED;
    }

    @Override
    public boolean lockDataAsAuditing(AuditableDTO draftData) {
        Optional<BatchImportDTO> importDTOOpt = getOriginalDataDTO(draftData);
        if (!importDTOOpt.isPresent()) {
            return false;
        }

        BatchImportDTO importDTO = importDTOOpt.get();
        importDTO.setAuditStatus(AuditStatus.AUDITING);
        BatchImportDTO batchImport =
          batchImportService.updatePanBlackListBatch(importDTO);
        return AuditStatus.AUDITING.equals(batchImport.getAuditStatus());
    }

    @Override
    public boolean unlockDataFromAuditing(AuditableDTO draftData) {
        Optional<BatchImportDTO> importDTOOpt = getOriginalDataDTO(draftData);
        if (!importDTOOpt.isPresent()) {
            return false;
        }

        BatchImportDTO importDTO = importDTOOpt.get();
        importDTO.setAuditStatus(AuditStatus.PUBLISHED);
        BatchImportDTO batchImport =
          batchImportService.updatePanBlackListBatch(importDTO);
        return AuditStatus.PUBLISHED.equals(batchImport.getAuditStatus());
    }
}
