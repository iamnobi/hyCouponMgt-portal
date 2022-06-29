package com.cherri.acs_portal.service.audit;

import com.cherri.acs_portal.dto.audit.AuditFileDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.bank.IssuerLogoUpdateDto;
import com.cherri.acs_portal.service.SystemSettingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.exception.OceanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 更新發卡行logo AuditWrapper
 */
@Service
public class IssuerLogoUpdateAuditWrapper implements AuditServiceWrapper<IssuerLogoUpdateDto> {

    private final SystemSettingService systemSettingService;
    private final ObjectMapper objectMapper;

    @Autowired
    public IssuerLogoUpdateAuditWrapper(SystemSettingService systemSettingService,
      ObjectMapper objectMapper) {
        this.systemSettingService = systemSettingService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<IssuerLogoUpdateDto> add(IssuerLogoUpdateDto draftData) {
        return Optional.empty();
    }

    @Override
    public boolean delete(DeleteDataDTO draftData) {
        return false;
    }

    @Override
    public Optional<IssuerLogoUpdateDto> update(IssuerLogoUpdateDto draftData) {
        return Optional.of(systemSettingService.updateIssuerLogo(draftData));
    }

    @Override
    public Optional<IssuerLogoUpdateDto> convertJsonToConcreteDTO(String draftInJson)
      throws IOException {
        return Optional.of(objectMapper.readValue(draftInJson, IssuerLogoUpdateDto.class));
    }

    @Override
    public Optional<IssuerLogoUpdateDto> getOriginalDataDTO(AuditableDTO draftData) {
        IssuerLogoUpdateDto dto = (IssuerLogoUpdateDto) draftData;
        return systemSettingService.getIssuerLogoByIssuerBankId(dto.getIssuerBankId());
    }

    @Override
    public Optional<AuditFileDTO> getOriginalFileDTO(IssuerLogoUpdateDto draftData) {
        return draftData.getAuditFile();
    }

    @Override
    public Optional<AuditFileDTO> getDraftFileDTO(IssuerLogoUpdateDto draftData) {
        return draftData.getAuditFile();
    }

    @Override
    public void filterForAuditUsed(IssuerLogoUpdateDto originalData) {
        originalData.setUser(null);
        originalData.setAuditStatus(null);
    }

    @Override
    public boolean isAuditingLockAvailable(AuditableDTO draftData) {
        Optional<IssuerLogoUpdateDto> originDataOpt = getOriginalDataDTO(draftData);
        return originDataOpt.map(o -> AuditStatus.PUBLISHED.equals(o.getAuditStatus()))
          .orElse(false);
    }

    @Override
    public boolean lockDataAsAuditing(AuditableDTO draftData) {
        Optional<IssuerLogoUpdateDto> originDataOpt = getOriginalDataDTO(draftData);
        if (!originDataOpt.isPresent()) {
            throw new OceanException(
              "Failed in mark data as auditing in update issuer logo with id: " + draftData.getId()
                + ", issuerBankId: " + draftData.getIssuerBankId());
        }

        IssuerLogoUpdateDto originData = originDataOpt.get();
        originData.setAuditStatus(AuditStatus.AUDITING);
        systemSettingService.updateIssuerLogo(originData);

        return true;
    }

    @Override
    public boolean unlockDataFromAuditing(AuditableDTO draftData) {
        Optional<IssuerLogoUpdateDto> originDataOpt = getOriginalDataDTO(draftData);
        if (!originDataOpt.isPresent()) {
            throw new OceanException(
              "Failed in mark data as auditing in update issuer logo with id: " + draftData.getId()
                + ", issuerBankId: " + draftData.getIssuerBankId());
        }

        IssuerLogoUpdateDto originData = originDataOpt.get();
        originData.setAuditStatus(AuditStatus.PUBLISHED);
        systemSettingService.updateIssuerLogo(originData);

        return true;
    }
}
