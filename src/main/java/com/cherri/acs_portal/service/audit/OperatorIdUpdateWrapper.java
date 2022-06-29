package com.cherri.acs_portal.service.audit;

import com.cherri.acs_portal.dto.audit.AuditFileDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.system.OperatorIdUpdateDto;
import com.cherri.acs_portal.dto.system.SystemSettingDTO;
import com.cherri.acs_portal.service.SystemSettingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.exception.OceanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperatorIdUpdateWrapper implements AuditServiceWrapper<OperatorIdUpdateDto> {

    private final SystemSettingService systemSettingService;
    private final ObjectMapper objectMapper;

    @Autowired
    public OperatorIdUpdateWrapper(SystemSettingService systemSettingService,
      ObjectMapper objectMapper) {
        this.systemSettingService = systemSettingService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<OperatorIdUpdateDto> add(OperatorIdUpdateDto draftData) {
        return Optional.empty();
    }

    @Override
    public boolean delete(DeleteDataDTO draftData) {
        return false;
    }

    @Override
    public Optional<OperatorIdUpdateDto> update(OperatorIdUpdateDto draftData) {
        for (SystemSettingDTO systemSettingDTO : draftData.getValueList()) {
            systemSettingDTO.setAuditStatus(AuditStatus.PUBLISHED.getSymbol());
        }
        return Optional.of(systemSettingService.updateOperatorId(draftData));
    }

    @Override
    public Optional<OperatorIdUpdateDto> convertJsonToConcreteDTO(String draftInJson)
      throws IOException {
        return Optional.of(objectMapper.readValue(draftInJson, OperatorIdUpdateDto.class));
    }

    @Override
    public Optional<OperatorIdUpdateDto> getOriginalDataDTO(AuditableDTO draftData) {
        return systemSettingService.getOperatorIdUpdateDTOByAuditDraftData(draftData);
    }

    @Override
    public Optional<AuditFileDTO> getOriginalFileDTO(OperatorIdUpdateDto draftData) {
        return Optional.empty();
    }

    @Override
    public Optional<AuditFileDTO> getDraftFileDTO(OperatorIdUpdateDto draftData) {
        return Optional.empty();
    }

    @Override
    public void filterForAuditUsed(OperatorIdUpdateDto originalData) {
        originalData.setUser(null);
        for (SystemSettingDTO systemSettingDTO : originalData.getValueList()) {
            systemSettingDTO.setAuditStatus(null);
        }
    }

    @Override
    public boolean isAuditingLockAvailable(AuditableDTO draftData) {
        Optional<OperatorIdUpdateDto> originDataOpt = getOriginalDataDTO(draftData);
        if (!originDataOpt.isPresent()) {
            return false;
        }

        OperatorIdUpdateDto originData = originDataOpt.get();
        for (SystemSettingDTO systemSettingDTO : originData.getValueList()) {
            boolean result = AuditStatus.PUBLISHED.getSymbol()
              .equals(systemSettingDTO.getAuditStatus());
            if (!result) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean lockDataAsAuditing(AuditableDTO draftData) {
        Optional<OperatorIdUpdateDto> originDataOpt = getOriginalDataDTO(draftData);
        if (!originDataOpt.isPresent()) {
            throw new OceanException(
              "Failed in mark data as auditing in update System Setting with id: "
                + draftData.getId()
                + ", issuerBankId: "
                + draftData.getIssuerBankId());
        }

        OperatorIdUpdateDto originData = originDataOpt.get();
        originData.setAuditStatus(AuditStatus.AUDITING);
        for (SystemSettingDTO systemSettingDTO : originData.getValueList()) {
            systemSettingDTO.setAuditStatus(AuditStatus.AUDITING.getSymbol());
        }
        systemSettingService.updateOperatorId(originData);
        return true;
    }

    @Override
    public boolean unlockDataFromAuditing(AuditableDTO draftData) {
        Optional<OperatorIdUpdateDto> originDataOpt = getOriginalDataDTO(draftData);
        if (!originDataOpt.isPresent()) {
            throw new OceanException(
              "Failed in mark data as auditing in update System Setting with id: "
                + draftData.getId()
                + ", issuerBankId: "
                + draftData.getIssuerBankId());
        }

        OperatorIdUpdateDto originData = originDataOpt.get();
        originData.setAuditStatus(AuditStatus.PUBLISHED);
        for (SystemSettingDTO systemSettingDTO : originData.getValueList()) {
            systemSettingDTO.setAuditStatus(AuditStatus.PUBLISHED.getSymbol());
        }
        systemSettingService.updateOperatorId(originData);
        return true;
    }
}
