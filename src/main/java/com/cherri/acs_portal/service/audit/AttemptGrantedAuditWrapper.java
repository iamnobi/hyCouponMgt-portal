package com.cherri.acs_portal.service.audit;

import com.cherri.acs_portal.dto.audit.AuditFileDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.whitelist.AttemptGrantedDTO;
import com.cherri.acs_portal.service.WhiteListAttemptService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.exception.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 人工彈性授權審核Wrapper
 */
@Service
public class AttemptGrantedAuditWrapper implements AuditServiceWrapper<AttemptGrantedDTO> {

    private final WhiteListAttemptService whiteListAttemptService;
    private final ObjectMapper objectMapper;

    @Autowired
    public AttemptGrantedAuditWrapper(
      WhiteListAttemptService whiteListAttemptService, ObjectMapper objectMapper) {
        this.whiteListAttemptService = whiteListAttemptService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<AttemptGrantedDTO> add(AttemptGrantedDTO draftData) throws DatabaseException {
        return Optional.of(whiteListAttemptService.addAttemptGrantedSetting(draftData));
    }

    @Override
    public boolean delete(DeleteDataDTO draftData) {
        return true;
    }

    @Override
    public Optional<AttemptGrantedDTO> update(AttemptGrantedDTO draftData) {
        return Optional.empty();
    }

    @Override
    public Optional<AttemptGrantedDTO> convertJsonToConcreteDTO(String draftInJson)
      throws IOException {
        return Optional.of(objectMapper.readValue(draftInJson, AttemptGrantedDTO.class));
    }

    @Override
    public Optional<AttemptGrantedDTO> getOriginalDataDTO(AuditableDTO draftData)
      throws DatabaseException {
        AttemptGrantedDTO queryDto = (AttemptGrantedDTO) draftData;
        return whiteListAttemptService
            .findOneWhiteListAttemptSettingByPanId(queryDto.getPanId());
    }

    @Override
    public Optional<AuditFileDTO> getOriginalFileDTO(AttemptGrantedDTO draftData) {
        return Optional.empty();
    }

    @Override
    public Optional<AuditFileDTO> getDraftFileDTO(AttemptGrantedDTO draftData) {
        return Optional.empty();
    }

    @Override
    public void filterForAuditUsed(AttemptGrantedDTO originalData) {
        originalData.setAuditStatus(null);
        originalData.setCreator(null);
    }

    @Override
    public boolean isAuditingLockAvailable(AuditableDTO draftData) throws DatabaseException {
        Optional<AttemptGrantedDTO> originalDataOpt = getOriginalDataDTO(draftData);
        return originalDataOpt
          .map(o -> AuditStatus.PUBLISHED.equals(o.getAuditStatus()))
          .orElse(false);
    }

    @Override
    public boolean lockDataAsAuditing(AuditableDTO draftData) throws DatabaseException {
        AttemptGrantedDTO queryDto = (AttemptGrantedDTO) draftData;
        AttemptGrantedDTO originalData =
            whiteListAttemptService
                .findOneWhiteListAttemptSettingByPanId(queryDto.getPanId())
                .orElseThrow(() -> getMarkAuditingException("mark", "attemptGrant", draftData));

        originalData.setAuditStatus(AuditStatus.AUDITING);
        whiteListAttemptService.updateWhiteListAttemptSetting(originalData);
        return true;
    }

    @Override
    public boolean unlockDataFromAuditing(AuditableDTO draftData) throws DatabaseException {
        AttemptGrantedDTO queryDto = (AttemptGrantedDTO) draftData;
        AttemptGrantedDTO originalData =
            whiteListAttemptService
                .findOneWhiteListAttemptSettingByPanId(queryDto.getPanId())
                .orElseThrow(() -> getMarkAuditingException("mark", "attemptGrant", draftData));

        originalData.setAuditStatus(AuditStatus.PUBLISHED);
        whiteListAttemptService.updateWhiteListAttemptSetting(originalData);
        return true;
    }
}
