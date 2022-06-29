package com.cherri.acs_portal.service.audit;

import com.cherri.acs_portal.dto.audit.AuditFileDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.blackList.output.BlackListIpGroupDTO;
import com.cherri.acs_portal.service.BlackListService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import ocean.acs.commons.enumerator.AuditStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 新增黑名單 by IP Wrapper
 */
@Service
public class BlackListIpGroupAuditWrapper
  implements AuditServiceWrapper<BlackListIpGroupDTO> {

    private final BlackListService blackListService;
    private final ObjectMapper objectMapper;

    @Autowired
    public BlackListIpGroupAuditWrapper(
      BlackListService blackListService, ObjectMapper objectMapper) {
        this.blackListService = blackListService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<BlackListIpGroupDTO> add(BlackListIpGroupDTO draftData) {
        return Optional.of(blackListService.addBlackListIp(draftData));
    }

    @Override
    public boolean delete(DeleteDataDTO draftData) {
        blackListService.deleteIpByIpGroupIds(draftData);
        return true;
    }

    @Override
    public Optional<BlackListIpGroupDTO> update(BlackListIpGroupDTO draftData) {
        return Optional.empty();
    }

    @Override
    public Optional<BlackListIpGroupDTO> convertJsonToConcreteDTO(String draftInJson)
      throws IOException {
        return Optional.of(objectMapper.readValue(draftInJson, BlackListIpGroupDTO.class));
    }

    @Override
    public Optional<BlackListIpGroupDTO> getOriginalDataDTO(AuditableDTO draftData) {
        try {
            return Optional
              .ofNullable(blackListService.getBlackListIpGroupCreateDTO(draftData.getId()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<AuditFileDTO> getOriginalFileDTO(BlackListIpGroupDTO draftData) {
        return Optional.empty();
    }

    @Override
    public Optional<AuditFileDTO> getDraftFileDTO(BlackListIpGroupDTO draftData) {
        return Optional.empty();
    }

    @Override
    public void filterForAuditUsed(BlackListIpGroupDTO originalData) {
        originalData.setCreator(null);
        originalData.setUpdater(null);
        originalData.setAuditStatus(null);
    }

    @Override
    public boolean isAuditingLockAvailable(AuditableDTO draftData) {
        Optional<BlackListIpGroupDTO> originalDataOpt = getOriginalDataDTO(draftData);
        return originalDataOpt.map(o -> AuditStatus.PUBLISHED.equals(o.getAuditStatus()))
          .orElse(false);
    }

    @Override
    public boolean lockDataAsAuditing(AuditableDTO draftData) {
        BlackListIpGroupDTO originalData =
          blackListService
            .findOneBlackListIpGroup(draftData.getIssuerBankId(), draftData.getId())
            .orElseThrow(() -> getMarkAuditingException("mark", "blacklistIpGroup", draftData));

        originalData.setAuditStatus(AuditStatus.AUDITING);
        blackListService.updateBlackListIp(originalData);
        return true;
    }

    @Override
    public boolean unlockDataFromAuditing(AuditableDTO draftData) {
        BlackListIpGroupDTO originalData =
          blackListService
            .findOneBlackListIpGroup(draftData.getIssuerBankId(), draftData.getId())
            .orElseThrow(() -> getMarkAuditingException("unmark", "blacklistIpGroup", draftData));

        originalData.setAuditStatus(AuditStatus.PUBLISHED);
        blackListService.updateBlackListIp(originalData);
        return true;
    }
}
