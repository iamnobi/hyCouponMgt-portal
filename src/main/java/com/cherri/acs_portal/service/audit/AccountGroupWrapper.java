package com.cherri.acs_portal.service.audit;

import com.cherri.acs_portal.dto.audit.AuditFileDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.usermanagement.AccountGroupDto;
import com.cherri.acs_portal.service.PermissionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.exception.NoSuchDataException;
import ocean.acs.models.dao.AccountGroupDAO;
import ocean.acs.models.dao.UserAccountDAO;
import ocean.acs.models.dao.UserGroupDAO;
import ocean.acs.models.data_object.entity.AccountGroupDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountGroupWrapper implements AuditServiceWrapper<AccountGroupDto> {

    @Autowired
    private PermissionService permissionService;
    @Autowired
    private AccountGroupDAO accountGroupDao;
    @Autowired
    private UserAccountDAO userAccountDao;
    @Autowired
    private UserGroupDAO userGroupDao;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Optional<AccountGroupDto> add(AccountGroupDto draftData) {
        return Optional.of(permissionService.createGroupMember(draftData));
    }

    @Override
    public boolean delete(DeleteDataDTO draftData) {
        return permissionService.deleteGroupMember(draftData);
    }

    @Override
    public Optional<AccountGroupDto> update(AccountGroupDto draftData) {
        return Optional.empty();
    }

    @Override
    public Optional<AccountGroupDto> convertJsonToConcreteDTO(String draftInJson)
      throws IOException {
        return Optional.of(objectMapper.readValue(draftInJson, AccountGroupDto.class));
    }

    @Override
    public Optional<AccountGroupDto> getOriginalDataDTO(AuditableDTO draftData) {
        Optional<AccountGroupDO> accountGroupOpt = accountGroupDao.findById(draftData.getId());
        if (!accountGroupOpt.isPresent()) {
            throw new NoSuchDataException("Id:" + draftData.getId() + " not found.");
        } else {
            AccountGroupDto ag = AccountGroupDto.valueOf(accountGroupOpt.get());

            Optional<String> accountNameOpt = userAccountDao.findAccountNameById(ag.getAccountId());
            ag.setAccountName(accountNameOpt.orElse("null"));

            Optional<String> groupNameOpt = userGroupDao.findGroupNameById(ag.getGroupId());
            ag.setGroupName(groupNameOpt.orElse("null"));
            return Optional.of(ag);
        }
    }

    @Override
    public Optional<AuditFileDTO> getOriginalFileDTO(AccountGroupDto draftData) {
        return Optional.empty();
    }

    @Override
    public Optional<AuditFileDTO> getDraftFileDTO(AccountGroupDto draftData) {
        return Optional.empty();
    }

    @Override
    public void filterForAuditUsed(AccountGroupDto originalData) {
        originalData.setAuditStatus(null);
        originalData.setCreator(null);
        originalData.setCreateMillis(null);
    }

    @Override
    public boolean isAuditingLockAvailable(AuditableDTO draftData) {
        Optional<AccountGroupDto> originalDataOpt = this.getOriginalDataDTO(draftData);
        return originalDataOpt.map(o -> AuditStatus.PUBLISHED.equals(o.getAuditStatus()))
          .orElse(false);
    }

    @Override
    public boolean lockDataAsAuditing(AuditableDTO draftData) {
        AccountGroupDO accountGroup = accountGroupDao.findById(draftData.getId())
          .orElseThrow(() -> getMarkAuditingException("mark", "accountGroup", draftData));
        accountGroup.setAuditStatus(AuditStatus.AUDITING);
        accountGroupDao.save(accountGroup);
        return true;
    }

    @Override
    public boolean unlockDataFromAuditing(AuditableDTO draftData) {
        AccountGroupDO accountGroup = accountGroupDao.findById(draftData.getId())
          .orElseThrow(() -> getMarkAuditingException("mark", "accountGroup", draftData));
        accountGroup.setAuditStatus(AuditStatus.PUBLISHED);
        accountGroupDao.save(accountGroup);
        return true;
    }

}
