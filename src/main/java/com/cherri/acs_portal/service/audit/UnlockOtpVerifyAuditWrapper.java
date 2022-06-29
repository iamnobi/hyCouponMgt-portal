package com.cherri.acs_portal.service.audit;

import com.cherri.acs_portal.dto.audit.AuditFileDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.cardholder.UnlockOtpVerifyDTO;
import com.cherri.acs_portal.service.PanInfoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.exception.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** OTP解鎖審核Wrapper */
@Service
public class UnlockOtpVerifyAuditWrapper implements AuditServiceWrapper<UnlockOtpVerifyDTO> {

    private final PanInfoService panInfoService;
    private final ObjectMapper objectMapper;

    @Autowired
    public UnlockOtpVerifyAuditWrapper(PanInfoService panInfoService, ObjectMapper objectMapper) {
        this.panInfoService = panInfoService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<UnlockOtpVerifyDTO> add(UnlockOtpVerifyDTO draftData) {
        return Optional.empty();
    }

    @Override
    public boolean delete(DeleteDataDTO draftData) {
        return false;
    }

    @Override
    public Optional<UnlockOtpVerifyDTO> update(UnlockOtpVerifyDTO draftData)
      throws DatabaseException {
        return Optional.of(panInfoService.unlockOtp(draftData));
    }

    @Override
    public Optional<UnlockOtpVerifyDTO> convertJsonToConcreteDTO(String draftInJson)
      throws IOException {
        return Optional.of(objectMapper.readValue(draftInJson, UnlockOtpVerifyDTO.class));
    }

    @Override
    public Optional<UnlockOtpVerifyDTO> getOriginalDataDTO(AuditableDTO draftData)
      throws DatabaseException {
        UnlockOtpVerifyDTO query = (UnlockOtpVerifyDTO) draftData;
        return panInfoService
            .findOnePanOtpStatistics(query.getIssuerBankId(), query.getPanId());
    }

    @Override
    public Optional<AuditFileDTO> getOriginalFileDTO(UnlockOtpVerifyDTO draftData) {
        return Optional.empty();
    }

    @Override
    public Optional<AuditFileDTO> getDraftFileDTO(UnlockOtpVerifyDTO draftData) {
        return Optional.empty();
    }

    @Override
    public void filterForAuditUsed(UnlockOtpVerifyDTO originalData) {
        originalData.setUser(null);
        originalData.setAuditStatus(null);
    }

    @Override
    public boolean isAuditingLockAvailable(AuditableDTO draftData) throws DatabaseException {
        Optional<UnlockOtpVerifyDTO> originDataOpt = getOriginalDataDTO(draftData);
        return originDataOpt
          .map(e -> AuditStatus.PUBLISHED.equals(e.getAuditStatus()))
          .orElse(false);
    }

    @Override
    public boolean lockDataAsAuditing(AuditableDTO draftData) throws DatabaseException {
        UnlockOtpVerifyDTO unlockOtpVerifyDto = (UnlockOtpVerifyDTO) draftData;
        UnlockOtpVerifyDTO originalData = panInfoService
            .findOnePanOtpStatistics(unlockOtpVerifyDto.getIssuerBankId(),
                unlockOtpVerifyDto.getPanId())
            .orElseThrow(() -> getMarkAuditingException("mark", "panOtpStatistics", draftData));

        originalData.setAuditStatus(AuditStatus.AUDITING);
        panInfoService.updatePanOtpStatistics(originalData);
        return true;
    }

    @Override
    public boolean unlockDataFromAuditing(AuditableDTO draftData) throws DatabaseException {
        UnlockOtpVerifyDTO unlockOtpVerifyDto = (UnlockOtpVerifyDTO) draftData;
        UnlockOtpVerifyDTO originalData = panInfoService
            .findOnePanOtpStatistics(unlockOtpVerifyDto.getIssuerBankId(),
                unlockOtpVerifyDto.getPanId())
            .orElseThrow(() -> getMarkAuditingException("unmark", "panOtpStatistics", draftData));

        originalData.setAuditStatus(AuditStatus.PUBLISHED);
        panInfoService.updatePanOtpStatistics(originalData);
        return true;
    }
}
