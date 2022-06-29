package com.cherri.acs_portal.service.audit;

import com.cherri.acs_portal.dto.audit.AuditFileDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.system.ChallengeViewMessageDTO;
import com.cherri.acs_portal.service.SystemSettingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import ocean.acs.commons.enumerator.AuditStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChallengeViewMessageAuditWrapper implements
  AuditServiceWrapper<ChallengeViewMessageDTO> {

    private final SystemSettingService systemSettingService;
    private final ObjectMapper objectMapper;

    @Autowired
    public ChallengeViewMessageAuditWrapper(SystemSettingService systemSettingService,
      ObjectMapper objectMapper) {
        this.systemSettingService = systemSettingService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<ChallengeViewMessageDTO> add(ChallengeViewMessageDTO draftData) {
        return Optional.empty();
    }

    @Override
    public boolean delete(DeleteDataDTO draftData) {
        return false;
    }

    @Override
    public Optional<ChallengeViewMessageDTO> update(ChallengeViewMessageDTO draftData) {
        updateAuditStatus(draftData, AuditStatus.PUBLISHED);
        return Optional.of(systemSettingService.updateChallengeViewMessage(draftData));
    }

    @Override
    public Optional<ChallengeViewMessageDTO> convertJsonToConcreteDTO(String draftInJson)
      throws IOException {
        ChallengeViewMessageDTO dto = objectMapper
          .readValue(draftInJson, ChallengeViewMessageDTO.class);
        systemSettingService.replaceJsNewLineToHtmlBrSymbol(dto.getOtpVerifyPage());
        systemSettingService.replaceJsNewLineToHtmlBrSymbol(dto.getPhoneVerifyPage());
        return Optional.of(dto);
    }

    @Override
    public Optional<ChallengeViewMessageDTO> getOriginalDataDTO(AuditableDTO draftData) {
        ChallengeViewMessageDTO dto = (ChallengeViewMessageDTO) draftData;
        return systemSettingService.getChallengeViewMessage(dto.getIssuerBankId(), dto.getLanguageCode());
    }

    @Override
    public Optional<AuditFileDTO> getOriginalFileDTO(ChallengeViewMessageDTO draftData) {
        return Optional.empty();
    }

    @Override
    public Optional<AuditFileDTO> getDraftFileDTO(ChallengeViewMessageDTO draftData) {
        return Optional.empty();
    }

    @Override
    public void filterForAuditUsed(ChallengeViewMessageDTO originalData) {
        originalData.getPhoneVerifyPage().setAuditStatus(null);
        originalData.getOtpVerifyPage().setAuditStatus(null);
        originalData.getOtpVerifyPage().getOtpVerifySetting().setAuditStatus(null);
        originalData.setUser(null);
    }

    @Override
    public boolean isAuditingLockAvailable(AuditableDTO draftData) {
        Optional<ChallengeViewMessageDTO> originalDataOpt = getOriginalDataDTO(draftData);
        return originalDataOpt.map(e ->
          AuditStatus.PUBLISHED.equals(e.getPhoneVerifyPage().getAuditStatus())
            && AuditStatus.PUBLISHED.equals(e.getOtpVerifyPage().getAuditStatus())
            && AuditStatus.PUBLISHED
            .equals(e.getOtpVerifyPage().getOtpVerifySetting().getAuditStatus()))
          .orElse(false);

    }

    @Override
    public boolean lockDataAsAuditing(AuditableDTO draftData) {
        ChallengeViewMessageDTO originalData = getOriginalDataDTO(draftData)
          .map(e -> updateAuditStatus(e, AuditStatus.AUDITING))
          .orElseThrow(() -> getMarkAuditingException("mark", "challengeViewMessage", draftData));
        systemSettingService.updateChallengeViewMessage(originalData);
        return true;
    }

    @Override
    public boolean unlockDataFromAuditing(AuditableDTO draftData) {
        ChallengeViewMessageDTO originalData = getOriginalDataDTO(draftData)
          .map(e -> updateAuditStatus(e, AuditStatus.PUBLISHED))
          .orElseThrow(() -> getMarkAuditingException("unmark", "challengeViewMessage", draftData));
        systemSettingService.updateChallengeViewMessage(originalData);
        return true;
    }

    private ChallengeViewMessageDTO updateAuditStatus(ChallengeViewMessageDTO dto,
      AuditStatus auditStatus) {
        dto.setAuditStatus(auditStatus);
        if (dto.getPhoneVerifyPage() != null) {
            dto.getPhoneVerifyPage().setAuditStatus(auditStatus);
        }
        if (dto.getOtpVerifyPage() != null) {
            dto.getOtpVerifyPage().setAuditStatus(auditStatus);
            if (dto.getOtpVerifyPage().getOtpVerifySetting() != null) {
                dto.getOtpVerifyPage().getOtpVerifySetting().setAuditStatus(auditStatus);
            }
        }
        return dto;
    }
}
