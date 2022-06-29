package com.cherri.acs_portal.service.audit;

import com.cherri.acs_portal.dto.audit.AuditFileDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.blackList.output.BlackListAuthStatusDTO;
import com.cherri.acs_portal.service.BlackListService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.exception.NoSuchDataException;
import ocean.acs.commons.exception.OceanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** 黑名單驗證狀態設定 */
@Service
public class BlackListAuthStatusAuditWrapper
    implements AuditServiceWrapper<BlackListAuthStatusDTO> {

  @Autowired BlackListService blackListService;
  @Autowired ObjectMapper objectMapper;

  @Override
  public Optional<BlackListAuthStatusDTO> add(BlackListAuthStatusDTO draftData) {
    throw new UnsupportedOperationException("Not implemented yet...");
  }

  @Override
  public boolean delete(DeleteDataDTO draftData) {
    throw new UnsupportedOperationException("Not implemented yet...");
  }

  @Override
  public Optional<BlackListAuthStatusDTO> update(BlackListAuthStatusDTO draftData) {
    return Optional.of(blackListService.updateAuthStatus(draftData));
  }

  @Override
  public Optional<BlackListAuthStatusDTO> convertJsonToConcreteDTO(String draftInJson)
      throws IOException {
    return Optional.of(objectMapper.readValue(draftInJson, BlackListAuthStatusDTO.class));
  }

  @Override
  public Optional<BlackListAuthStatusDTO> getOriginalDataDTO(AuditableDTO draftData)
      throws OceanException {
    BlackListAuthStatusDTO dto =
        blackListService
            .findOneAuthStatus(draftData.getId())
            .orElseThrow(() -> new NoSuchDataException("Id:" + draftData.getId() + " not found."));
    return Optional.of(dto);
  }

  @Override
  public Optional<AuditFileDTO> getOriginalFileDTO(BlackListAuthStatusDTO draftData) {
    return Optional.empty();
  }

  @Override
  public Optional<AuditFileDTO> getDraftFileDTO(BlackListAuthStatusDTO draftData) {
    return Optional.empty();
  }

  @Override
  public void filterForAuditUsed(BlackListAuthStatusDTO originalData) {
    originalData.setAuditStatus(null);
    originalData.setUser(null);
  }

  @Override
  public boolean isAuditingLockAvailable(AuditableDTO draftData) {
    Optional<BlackListAuthStatusDTO> originalDataOpt = getOriginalDataDTO(draftData);
    return originalDataOpt.map(o -> AuditStatus.PUBLISHED.equals(o.getAuditStatus())).orElse(false);
  }

  @Override
  public boolean lockDataAsAuditing(AuditableDTO draftData) {
    BlackListAuthStatusDTO originalData =
        blackListService
            .findOneAuthStatus(draftData.getId())
            .orElseThrow(() -> getMarkAuditingException("mark", "blacklistAuthStatus", draftData));

    originalData.setAuditStatus(AuditStatus.AUDITING);
    blackListService.updateAuthStatus(originalData);
    return true;
  }

  @Override
  public boolean unlockDataFromAuditing(AuditableDTO draftData) {
    BlackListAuthStatusDTO originalData =
        blackListService
            .findOneAuthStatus(draftData.getId())
            .orElseThrow(() -> getMarkAuditingException("unmark", "blacklistAuthStatus", draftData));

    originalData.setAuditStatus(AuditStatus.PUBLISHED);
    blackListService.updateAuthStatus(originalData);
    return true;
  }
}
