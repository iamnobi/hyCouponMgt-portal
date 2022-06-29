package com.cherri.acs_portal.service.audit;

import com.cherri.acs_portal.dto.audit.AuditFileDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.usermanagement.UserAccountDTO;
import com.cherri.acs_portal.service.UserManagementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.exception.NoSuchDataException;
import ocean.acs.commons.exception.OceanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 使用者管理
 */
@Service
public class UserManagementAuditWrapper implements AuditServiceWrapper<UserAccountDTO> {

    @Autowired
    UserManagementService userManagementService;
    @Autowired
    ObjectMapper objectMapper;

    @Override
    public Optional<UserAccountDTO> add(UserAccountDTO draftData) {
        return Optional.of(userManagementService.createUser(draftData));
    }

    @Override
    public boolean delete(DeleteDataDTO draftData) {
        return userManagementService.deleteUser(draftData);
    }

    @Override
    public Optional<UserAccountDTO> update(UserAccountDTO draftData) {
        return Optional.of(userManagementService.updateUser(draftData));
    }

    @Override
    public Optional<UserAccountDTO> convertJsonToConcreteDTO(String draftInJson)
      throws IOException {
        return Optional.of(objectMapper.readValue(draftInJson, UserAccountDTO.class));
    }

    @Override
    public Optional<UserAccountDTO> getOriginalDataDTO(AuditableDTO draftData)
      throws OceanException {
        UserAccountDTO dto =
          userManagementService.findOneUserIncludeDeleted(draftData.getId())
            .orElseThrow(() -> new NoSuchDataException("Id:" + draftData.getId() + " not found."));
        return Optional.of(dto);
    }

    @Override
    public Optional<AuditFileDTO> getOriginalFileDTO(UserAccountDTO draftData) {
        return Optional.empty();
    }

    @Override
    public Optional<AuditFileDTO> getDraftFileDTO(UserAccountDTO draftData) {
        return Optional.empty();
    }

    @Override
    public void filterForAuditUsed(UserAccountDTO originalData) {
        originalData.setAuditStatus(null);
        originalData.setCreator(null);
        originalData.setUpdater(null);
    }

    @Override
    public boolean isAuditingLockAvailable(AuditableDTO draftData) {
        Optional<UserAccountDTO> originalDataOpt = getOriginalDataDTO(draftData);
        return originalDataOpt.map(o -> AuditStatus.PUBLISHED.equals(o.getAuditStatus()))
          .orElse(false);
    }

    @Override
    public boolean lockDataAsAuditing(AuditableDTO draftData) {
        UserAccountDTO originalData =
          userManagementService
            .findUserById(draftData.getId())
            .orElseThrow(() -> getMarkAuditingException("mark", "userAccount", draftData));

        originalData.setAuditStatus(AuditStatus.AUDITING);
        userManagementService.updateUser(originalData);
        return true;
    }

    @Override
    public boolean unlockDataFromAuditing(AuditableDTO draftData) {
        UserAccountDTO originalData =
          userManagementService
            .findUserById(draftData.getId())
            .orElseThrow(() -> getMarkAuditingException("unmark", "userAccount", draftData));

        originalData.setAuditStatus(AuditStatus.PUBLISHED);
        userManagementService.updateUser(originalData);
        return true;
    }
}
