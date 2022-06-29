package com.cherri.acs_portal.service.audit;

import com.cherri.acs_portal.dto.audit.AuditFileDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.usermanagement.UserGroupDto;
import com.cherri.acs_portal.service.PermissionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.exception.NoSuchDataException;
import ocean.acs.models.dao.UserGroupDAO;
import ocean.acs.models.data_object.entity.UserGroupDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserGroupWrapper implements AuditServiceWrapper<UserGroupDto> {

    @Autowired
    private PermissionService permissionService;
    @Autowired
    private UserGroupDAO userGroupDao;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Optional<UserGroupDto> add(UserGroupDto draftData) {
        return Optional.of(permissionService.createBankGroup(draftData));
    }

    @Override
    public boolean delete(DeleteDataDTO draftData) {
        return permissionService.deleteBankGroup(draftData);
    }

    @Override
    public Optional<UserGroupDto> update(UserGroupDto draftData) {
        return Optional.of(permissionService.updateBankGroup(draftData));
    }

    @Override
    public Optional<UserGroupDto> convertJsonToConcreteDTO(String draftInJson) throws IOException {
        return Optional.of(objectMapper.readValue(draftInJson, UserGroupDto.class));
    }

    @Override
    public Optional<UserGroupDto> getOriginalDataDTO(AuditableDTO draftData) {
        Optional<UserGroupDO> userGroupOtp = userGroupDao.findByIdAndNotDelete(draftData.getId());
        if (!userGroupOtp.isPresent()) {
            throw new NoSuchDataException("UserGroup Id:" + draftData.getId() + " not found.");
        } else {
            UserGroupDto userGroupDto = UserGroupDto.valueOf(userGroupOtp.get());
            return Optional.of(userGroupDto);
        }
    }

    @Override
    public Optional<AuditFileDTO> getOriginalFileDTO(UserGroupDto draftData) {
        return Optional.empty();
    }

    @Override
    public Optional<AuditFileDTO> getDraftFileDTO(UserGroupDto draftData) {
        return Optional.empty();
    }

    @Override
    public void filterForAuditUsed(UserGroupDto originalData) {
        originalData.setAuditStatus(null);
        originalData.setCreator(null);
        originalData.setUpdater(null);
    }

    @Override
    public boolean isAuditingLockAvailable(AuditableDTO draftData) {
        Optional<UserGroupDto> originalDataOpt = this.getOriginalDataDTO(draftData);
        return originalDataOpt.map(o -> AuditStatus.PUBLISHED.equals(o.getAuditStatus()))
          .orElse(false);
    }

    @Override
    public boolean lockDataAsAuditing(AuditableDTO draftData) {
        UserGroupDO userGroup = userGroupDao.findByIdAndNotDelete(draftData.getId())
          .orElseThrow(() -> getMarkAuditingException("mark", "userGroup", draftData));
        userGroup.setAuditStatus(AuditStatus.AUDITING.getSymbol());
        userGroupDao.save(userGroup);
        return true;
    }

    @Override
    public boolean unlockDataFromAuditing(AuditableDTO draftData) {
        UserGroupDO userGroup = userGroupDao.findByIdAndNotDelete(draftData.getId())
          .orElseThrow(() -> getMarkAuditingException("unmark", "userGroup", draftData));
        userGroup.setAuditStatus(AuditStatus.PUBLISHED.getSymbol());
        userGroupDao.save(userGroup);
        return true;
    }

}
