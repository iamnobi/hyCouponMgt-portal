package com.cherri.acs_portal.service.audit;

import com.cherri.acs_portal.dto.audit.AuditFileDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.system.SmsTemplateDTO;
import com.cherri.acs_portal.service.SystemSettingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.exception.NoSuchDataException;
import ocean.acs.commons.exception.OceanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 簡訊驗證模板
 */
@Service
public class SmsTemplateAuditWrapper implements AuditServiceWrapper<SmsTemplateDTO> {

    private final SystemSettingService systemSettingService;
    private final ObjectMapper objectMapper;

    @Autowired
    public SmsTemplateAuditWrapper(
      SystemSettingService systemSettingService, ObjectMapper objectMapper) {
        this.systemSettingService = systemSettingService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<SmsTemplateDTO> add(SmsTemplateDTO draftData) {
        return Optional.empty();
    }

    @Override
    public boolean delete(DeleteDataDTO draftData) {
        return true;
    }

    @Override
    public Optional<SmsTemplateDTO> update(SmsTemplateDTO draftData) {
        return Optional.of(systemSettingService.updateSmsTemplate(draftData));
    }

    @Override
    public Optional<SmsTemplateDTO> convertJsonToConcreteDTO(String draftInJson)
      throws IOException {
        return Optional.of(objectMapper.readValue(draftInJson, SmsTemplateDTO.class));
    }

    @Override
    public Optional<SmsTemplateDTO> getOriginalDataDTO(AuditableDTO draftData)
      throws OceanException {
        SmsTemplateDTO dto =
          systemSettingService
            .findOneSmsTemplate(draftData.getIssuerBankId())
            .orElseThrow(() -> new NoSuchDataException(
              "IssuerBankId:" + draftData.getIssuerBankId() + " not found."));
        return Optional.of(dto);
    }

    @Override
    public Optional<AuditFileDTO> getOriginalFileDTO(SmsTemplateDTO draftData) {
        return Optional.empty();
    }

    @Override
    public Optional<AuditFileDTO> getDraftFileDTO(SmsTemplateDTO draftData) {
        return Optional.empty();
    }

    @Override
    public void filterForAuditUsed(SmsTemplateDTO originalData) {
        originalData.setAuditStatus(null);
        originalData.setOperator(null);
    }

    @Override
    public boolean isAuditingLockAvailable(AuditableDTO draftData) {
        Optional<SmsTemplateDTO> originalDataOpt = getOriginalDataDTO(draftData);
        return originalDataOpt.map(o -> AuditStatus.PUBLISHED.equals(o.getAuditStatus()))
          .orElse(false);
    }

    @Override
    public boolean lockDataAsAuditing(AuditableDTO draftData) {
        SmsTemplateDTO originalData =
          systemSettingService
            .findOneSmsTemplate(draftData.getIssuerBankId())
            .orElseThrow(() -> getMarkAuditingException("mark", "smsTemplate", draftData));

        originalData.setAuditStatus(AuditStatus.AUDITING);
        systemSettingService.updateSmsTemplate(originalData);
        return true;
    }

    @Override
    public boolean unlockDataFromAuditing(AuditableDTO draftData) {
        SmsTemplateDTO originalData =
          systemSettingService
            .findOneSmsTemplate(draftData.getIssuerBankId())
            .orElseThrow(() -> getMarkAuditingException("unmark", "smsTemplate", draftData));

        originalData.setAuditStatus(AuditStatus.PUBLISHED);
        systemSettingService.updateSmsTemplate(originalData);
        return true;
    }
}
