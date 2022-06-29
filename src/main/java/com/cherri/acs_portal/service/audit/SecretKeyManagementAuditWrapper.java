package com.cherri.acs_portal.service.audit;

import com.cherri.acs_portal.dto.audit.AuditFileDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.common.IdsQueryDTO;
import com.cherri.acs_portal.dto.system.SecretKeyDTO;
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
 * 密鑰管理
 */
@Service
public class SecretKeyManagementAuditWrapper implements AuditServiceWrapper<SecretKeyDTO> {

    @Autowired
    SystemSettingService systemSettingService;
    @Autowired
    ObjectMapper objectMapper;

    @Override
    public Optional<SecretKeyDTO> add(SecretKeyDTO draftData) {
        return Optional.empty();
    }

    @Override
    public boolean delete(DeleteDataDTO draftData) {
        return true;
    }

    @Override
    public Optional<SecretKeyDTO> update(SecretKeyDTO draftData) {
        return Optional.of(systemSettingService.saveOrUpdateKey(draftData));
    }

    @Override
    public Optional<SecretKeyDTO> convertJsonToConcreteDTO(String draftInJson) throws IOException {
        return Optional.of(objectMapper.readValue(draftInJson, SecretKeyDTO.class));
    }

    @Override
    public Optional<SecretKeyDTO> getOriginalDataDTO(AuditableDTO draftData) throws OceanException {
        SecretKeyDTO keyDto = findOneSecretKey(draftData,
          new OceanException("Create a secret-key data using the create-bank API first."));
        return Optional.of(keyDto);
    }

    private SecretKeyDTO findOneSecretKey(AuditableDTO draftData, OceanException exception)
      throws NoSuchDataException {
        return systemSettingService
          .findOneSecretKey(draftData.getId())
          .map(SecretKeyDTO::valueOf)
          .orElseThrow(() -> exception);
    }

    @Override
    public Optional<AuditFileDTO> getOriginalFileDTO(SecretKeyDTO draftData) {
        return Optional.empty();
    }

    @Override
    public Optional<AuditFileDTO> getDraftFileDTO(SecretKeyDTO draftData) {
        return Optional.empty();
    }

    private IdsQueryDTO getIdQueryDto(AuditableDTO draftData) {
        IdsQueryDTO queryDto = new IdsQueryDTO();
        queryDto.addId(draftData.getId());
        queryDto.setIssuerBankId(draftData.getIssuerBankId());
        return queryDto;
    }

    @Override
    public void filterForAuditUsed(SecretKeyDTO originalData) {
        originalData.setAuditStatus(null);
        originalData.setOperator(null);
    }

    @Override
    public boolean isAuditingLockAvailable(AuditableDTO draftData) {
        Optional<SecretKeyDTO> originalDataOpt = getOriginalDataDTO(draftData);
        // 因為密鑰管理沒有"新增"API，所以建立銀行時會預設新增SecretKey且AuditStatus=UNKNOWN
        return originalDataOpt.map(o -> AuditStatus.PUBLISHED.equals(o.getAuditStatus())
          || AuditStatus.UNKNOWN.equals(o.getAuditStatus()))
          .orElse(false);
    }

    @Override
    public boolean lockDataAsAuditing(AuditableDTO draftData) {
        SecretKeyDTO originalData =
          findOneSecretKey(draftData, getMarkAuditingException("mark", "secretKey", draftData));
        originalData.setAuditStatus(AuditStatus.AUDITING);
        systemSettingService.saveOrUpdateKey(originalData);
        return true;
    }

    @Override
    public boolean unlockDataFromAuditing(AuditableDTO draftData) {
        SecretKeyDTO originalData =
          findOneSecretKey(draftData, getMarkAuditingException("unmark", "secretKey", draftData));
        originalData.setAuditStatus(AuditStatus.PUBLISHED);
        systemSettingService.saveOrUpdateKey(originalData);
        return true;
    }
}
