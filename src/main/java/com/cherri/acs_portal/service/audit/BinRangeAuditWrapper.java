package com.cherri.acs_portal.service.audit;

import com.cherri.acs_portal.dto.audit.AuditFileDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.system.BinRangeDTO;
import com.cherri.acs_portal.service.SystemSettingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.exception.OceanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BinRangeAuditWrapper implements AuditServiceWrapper<BinRangeDTO> {

  private final SystemSettingService systemSettingService;
  private final ObjectMapper objectMapper;

  @Autowired
  public BinRangeAuditWrapper(
      SystemSettingService systemSettingService,
      ObjectMapper objectMapper) {
    this.systemSettingService = systemSettingService;
    this.objectMapper = objectMapper;
  }

  @Override
  public Optional<BinRangeDTO> add(BinRangeDTO draftData) {
    draftData = systemSettingService.addBinRange(draftData);
    return Optional.ofNullable(draftData);
  }

  @Override
  public boolean delete(DeleteDataDTO deleteDataDTO) {
    return systemSettingService.deleteBinRange(deleteDataDTO);
  }

  @Override
  public Optional<BinRangeDTO> update(BinRangeDTO draftData) {
    systemSettingService.updateBinRange(draftData);
    return Optional.of(draftData);
  }

  @Override
  public Optional<BinRangeDTO> convertJsonToConcreteDTO(String draftInJson) throws IOException {
    return Optional.of(objectMapper.readValue(draftInJson, BinRangeDTO.class));
  }

  @Override
  public Optional<BinRangeDTO> getOriginalDataDTO(AuditableDTO draftData) {
    return systemSettingService.getBinRangeById(draftData.getId(), draftData.getIssuerBankId());
  }

  @Override
  public Optional<AuditFileDTO> getOriginalFileDTO(BinRangeDTO draftData) {
    return Optional.empty();
  }

  @Override
  public Optional<AuditFileDTO> getDraftFileDTO(BinRangeDTO draftData) {
    return Optional.empty();
  }

  @Override
  public void filterForAuditUsed(BinRangeDTO originalData) {
    originalData.setUser(null);
    originalData.setAuditStatus(null);
  }

  @Override
  public boolean isAuditingLockAvailable(AuditableDTO draftData) {
    Optional<BinRangeDTO> getOriginalDataOpt = getOriginalDataDTO(draftData);
    if (!getOriginalDataOpt.isPresent()) {
      return false;
    }

    BinRangeDTO originalData = getOriginalDataOpt.get();
    return AuditStatus.PUBLISHED == originalData.getAuditStatus();
  }

  @Override
  public boolean lockDataAsAuditing(AuditableDTO draftData) {
    Optional<BinRangeDTO> originalDataOpt =
        systemSettingService.getBinRangeById(draftData.getId(), draftData.getIssuerBankId());
    if (!originalDataOpt.isPresent()) {
      throw new OceanException(
          "Failed in mark data as auditing in update bin range with binId: "
              + draftData.getId()
              + ", issuerBankId: "
              + draftData.getIssuerBankId());
    }

    BinRangeDTO originalBinData = originalDataOpt.get();
    originalBinData.setAuditStatus(AuditStatus.AUDITING);
    systemSettingService.updateBinRangeAuditStatus(originalBinData);
    return true;
  }

  @Override
  public boolean unlockDataFromAuditing(AuditableDTO draftData) {
    Optional<BinRangeDTO> originalDataOpt =
        systemSettingService.getBinRangeById(draftData.getId(), draftData.getIssuerBankId());
    if (!originalDataOpt.isPresent()) {
      throw new OceanException(
          "Failed in unmark data from auditing in update bin range with binId: "
              + draftData.getId()
              + ", issuerBankId: "
              + draftData.getIssuerBankId());
    }

    BinRangeDTO originalBinData = originalDataOpt.get();
    originalBinData.setAuditStatus(AuditStatus.PUBLISHED);
    systemSettingService.updateBinRangeAuditStatus(originalBinData);
    return true;
  }

  private Map<String, Object> draftToIdUserMap(AuditableDTO draftData)
      throws UnsupportedOperationException {
    final Long id;
    final String user;
    if (draftData instanceof BinRangeDTO) {
      BinRangeDTO binRangeDto = (BinRangeDTO) draftData;
      id = binRangeDto.getId();
      user = binRangeDto.getUser();
    } else if (draftData instanceof DeleteDataDTO) {
      DeleteDataDTO deleteDataDto = (DeleteDataDTO) draftData;
      id = deleteDataDto.getId();
      user = deleteDataDto.getUser();
    } else {
      throw new UnsupportedOperationException("[draftToIdUserMap] unknown draftData.");
    }
    Map<String, Object> map = new HashMap<>();
    map.put("id", id);
    map.put("user", user);
    return map;
  }
}
