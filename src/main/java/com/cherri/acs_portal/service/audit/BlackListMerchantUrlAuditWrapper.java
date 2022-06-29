package com.cherri.acs_portal.service.audit;

import com.cherri.acs_portal.dto.audit.AuditFileDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.blackList.output.BlackListMerchantUrlDTO;
import com.cherri.acs_portal.service.BlackListService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.exception.NoSuchDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Merchant URL黑名單
 */
@Service
public class BlackListMerchantUrlAuditWrapper
  implements AuditServiceWrapper<BlackListMerchantUrlDTO> {

    private final BlackListService blackListService;
    private final ObjectMapper objectMapper;

    @Autowired
    public BlackListMerchantUrlAuditWrapper(
      BlackListService blackListService, ObjectMapper objectMapper) {
        this.blackListService = blackListService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<BlackListMerchantUrlDTO> add(BlackListMerchantUrlDTO draftData) {
        return Optional.of(blackListService.addBlackListMerchantUrl(draftData));
    }

    @Override
    public boolean delete(DeleteDataDTO draftData) {
        blackListService.deleteMerchantUrlById(draftData);
        return true;
    }

    @Override
    public Optional<BlackListMerchantUrlDTO> update(BlackListMerchantUrlDTO draftData) {
        return Optional.empty();
    }

    @Override
    public Optional<BlackListMerchantUrlDTO> convertJsonToConcreteDTO(String draftInJson)
      throws IOException {
        return Optional.of(objectMapper.readValue(draftInJson, BlackListMerchantUrlDTO.class));
    }

    @Override
    public Optional<BlackListMerchantUrlDTO> getOriginalDataDTO(AuditableDTO draftData) {
        BlackListMerchantUrlDTO dto =
          blackListService
            .findOneBlackListMerchantUrl(draftData.getIssuerBankId(), draftData.getId())
            .orElseThrow(() -> new NoSuchDataException("Id:" + draftData.getId() + " not found."));
        return Optional.of(dto);
    }

    @Override
    public Optional<AuditFileDTO> getOriginalFileDTO(BlackListMerchantUrlDTO draftData) {
        return Optional.empty();
    }

    @Override
    public Optional<AuditFileDTO> getDraftFileDTO(BlackListMerchantUrlDTO draftData) {
        return Optional.empty();
    }

    @Override
    public void filterForAuditUsed(BlackListMerchantUrlDTO originalData) {
        originalData.setAuditStatus(null);
        originalData.setCreator(null);
        originalData.setUpdater(null);
    }

    @Override
    public boolean isAuditingLockAvailable(AuditableDTO draftData) {
        Optional<BlackListMerchantUrlDTO> originalDataOpt = getOriginalDataDTO(draftData);
        return originalDataOpt.map(o -> AuditStatus.PUBLISHED.equals(o.getAuditStatus()))
          .orElse(false);
    }

    @Override
    public boolean lockDataAsAuditing(AuditableDTO draftData) {
        BlackListMerchantUrlDTO originalData =
          blackListService
            .findOneBlackListMerchantUrl(draftData.getIssuerBankId(), draftData.getId())
            .orElseThrow(() -> getMarkAuditingException("mark", "blacklistMerchantUrl", draftData));

        originalData.setAuditStatus(AuditStatus.AUDITING);
        blackListService.updateBlackListMerchantUrl(originalData);
        return true;
    }

    @Override
    public boolean unlockDataFromAuditing(AuditableDTO draftData) {
        BlackListMerchantUrlDTO originalData =
          blackListService
            .findOneBlackListMerchantUrl(draftData.getIssuerBankId(), draftData.getId())
            .orElseThrow(
              () -> getMarkAuditingException("unmark", "blacklistMerchantUrl", draftData));

        originalData.setAuditStatus(AuditStatus.PUBLISHED);
        blackListService.updateBlackListMerchantUrl(originalData);
        return true;
    }
}
