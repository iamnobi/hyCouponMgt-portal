package com.cherri.acs_portal.service;

import com.cherri.acs_portal.config.security.CustomizedUserPrincipal;
import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.cherri.acs_portal.controller.request.CreateOrDeleteGroupMemberReqDto;
import com.cherri.acs_portal.controller.request.DeleteBankGroupReqDto;
import com.cherri.acs_portal.controller.response.BankGroupDto;
import com.cherri.acs_portal.controller.response.GroupMemberDto;
import com.cherri.acs_portal.controller.response.ModuleSettingPermissionResponse;
import com.cherri.acs_portal.controller.response.PermissionDto;
import com.cherri.acs_portal.dto.PageQueryDTO;
import com.cherri.acs_portal.dto.PagingResultDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.usermanagement.AccountGroupDto;
import com.cherri.acs_portal.dto.usermanagement.DeleteGroupMemberDto;
import com.cherri.acs_portal.dto.usermanagement.UserGroupDto;
import com.cherri.acs_portal.util.StringCustomizedUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.enumerator.Permission;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.enumerator.UserGroupScope;
import ocean.acs.commons.enumerator.UserGroupType;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.models.dao.AccountGroupDAO;
import ocean.acs.models.dao.ModuleSettingDAO;
import ocean.acs.models.dao.UserAccountDAO;
import ocean.acs.models.dao.UserGroupDAO;
import ocean.acs.models.data_object.entity.AccountGroupDO;
import ocean.acs.models.data_object.entity.ModuleSettingDO;
import ocean.acs.models.data_object.entity.UserGroupDO;
import ocean.acs.models.data_object.portal.PageQueryDO;
import ocean.acs.models.data_object.portal.UserAccountDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class PermissionService {

    private final UserAccountDAO userAccountDao;
    private final UserGroupDAO userGroupDao;
    private final AccountGroupDAO accountGroupDao;
    private final ModuleSettingDAO moduleSettingDao;

    @Autowired

    public PermissionService(
      UserAccountDAO userAccountDao,
      UserGroupDAO userGroupDao,
      AccountGroupDAO accountGroupDao,
      ModuleSettingDAO moduleSettingDao) {
        this.userAccountDao = userAccountDao;
        this.userGroupDao = userGroupDao;
        this.accountGroupDao = accountGroupDao;
        this.moduleSettingDao = moduleSettingDao;
    }

    public PermissionDto getUserPermissions(Long issuerBankId, String account) {
        // [組織] 先使用帳號查詢
        Optional<Long> userAccountIdOpt = userAccountDao
          .findIdByIssuerBankIdAndAccountAndNotDelete(issuerBankId,
            account);

        if (!userAccountIdOpt.isPresent()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Delete account or illegal argument : issuerBankId or account");
        }
        Long id = userAccountIdOpt.get();
        Iterable<UserGroupDO> userGroupIter = userGroupDao
          .findByUserAccountIdAndNotDelete(id);
        if (StreamSupport.stream(userGroupIter.spliterator(), false).count() == 0) {
            return new PermissionDto();
        } else {
            try {
                Set<String> hasPermissions_field = new HashSet<>();
                for (Iterator<UserGroupDO> iterator = userGroupIter.iterator();
                  iterator.hasNext(); ) {
                    hasPermissions_field.addAll(iterator.next().getPermissionDtoField());
                }

                PermissionDto dto = new PermissionDto();
                Class<? extends PermissionDto> clazz = dto.getClass();
                for (String field : hasPermissions_field) {
                    String fieldSetter = String
                      .format("set%S%s", field.substring(0, 1), field.substring(1));
                    Method method = null;
                    try {
                        method = clazz.getMethod(fieldSetter, Boolean.class);
                    } catch (NoSuchMethodException e) {
                        log.error("[getUserPermissions] no such method: {}.{}(Boolean)",
                          clazz.getSimpleName(), fieldSetter);
                        throw new OceanException(ResultStatus.SERVER_ERROR, e.getMessage(), e);
                    }
                    method.invoke(dto, Boolean.TRUE);
                }
                return dto;

            } catch (IllegalArgumentException | IllegalAccessException |
              SecurityException | InvocationTargetException e) {
                throw new OceanException(ResultStatus.SERVER_ERROR, e.getMessage(), e);
            }
        }
    }

    public List<BankGroupDto> getBankGroups(Long issuerBankId) {
        List<BankGroupDto> bankGroupList = new ArrayList<BankGroupDto>();
        List<UserGroupDO> userGroups = userGroupDao
          .findUserGroupListByIssuerBankIdAndNotDelete(issuerBankId);
        for (UserGroupDO up : userGroups) {
            long members = accountGroupDao.countByGroupId(up.getId());
            BankGroupDto dto = new BankGroupDto(up.getId(), up.getName(), members,
              AuditStatus.getStatusBySymbol(up.getAuditStatus()));
            bankGroupList.add(dto);
        }

        return bankGroupList;
    }

    public boolean isUserGroupNameExisted(Long issuerBankId, String name) {
        return userGroupDao.existsByIssuerBankIdAndNameAndNotDelete(issuerBankId, name);
    }

    /**
     * 如果有修改權限相關請記得同步更改以下Class及{@Table user_group}
     * 1. {@link ocean.acs.models.oracle.entity.UserGroup} Field
     * 2. {@link ocean.acs.models.oracle.entity.UserGroup} All args constructor
     * 3. {@link ocean.acs.models.oracle.entity.UserGroup#valueOf(UserGroupDO)}
     * 4. {@link ocean.acs.models.sql_server.entity.UserGroup} Field
     * 5. {@link ocean.acs.models.sql_server.entity.UserGroup} All args constructor
     * 6. {@link ocean.acs.models.sql_server.entity.UserGroup#valueOf(UserGroupDO)
     * 7. {@link ocean.acs.models.entity.DBKey}}
     * 8. {@link UserGroupDO}
     * 9. {@link UserGroupDO#valueOf(ocean.acs.models.oracle.entity.UserGroup)}
     * 10. {@link UserGroupDO#valueOf(ocean.acs.models.sql_server.entity.UserGroup)}
     * 11. {@link UserGroupDto}
     * 12. {@link UserGroupDto#valueOf(UserGroupDO)}
     * 13. {@link Permission}
     * 14. {@link PermissionDto}
     * 15. {@link com.cherri.acs_portal.controller.request.UpdateGroupPermissionReqDto}
     * 16. {@link com.cherri.acs_portal.controller.request.UpdateGroupPermissionReqDto#checkUpdatingPermissionRule}
     * 17. {@link PermissionService#updateByUserGroupDto(UserGroupDO, UserGroupDto)}
     * 18. {@link PermissionService#createBankGroup(UserGroupDto)}
     */
    public UserGroupDto createBankGroup(UserGroupDto userGroupDto) {

        String name = userGroupDto.getName().trim();
        if (name.length() > 100) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT, "illegal argument : name");
        }

        boolean isExist = userGroupDao
          .existsByIssuerBankIdAndNameAndNotDelete(userGroupDto.getIssuerBankId(), name);
        if (isExist) {
            throw new OceanException(ResultStatus.DUPLICATE_DATA_ELEMENT,
              "Create group failed, because group with specific name has already existed");
        } else {
            UserGroupDO up = new UserGroupDO();
            up.setIssuerBankId(userGroupDto.getIssuerBankId());
            up.setName(name);
            up.setType(UserGroupType.CUSTOMIZED);
            up.setCreator(userGroupDto.getCreator());
            up.setCreateMillis(System.currentTimeMillis());
            up.setAuditStatus(userGroupDto.getAuditStatus().toString());

            if (EnvironmentConstants.ORG_ISSUER_BANK_ID == userGroupDto.getIssuerBankId()) {
                up.setScope(UserGroupScope.ORG);
                up.setAccessMultiBank(Boolean.TRUE);
                for (String permissionName : EnvironmentConstants.ORG_ROLE_MODULE_LIST) {
                    String methodName = null;
                    try {
                        methodName = StringCustomizedUtils
                            .methodNameUnderlineToCamel("set", permissionName.trim());
                        Method method = up.getClass().getDeclaredMethod(methodName, Boolean.class);
                        method.invoke(up, Boolean.TRUE);
                    } catch (Exception e) {
                        String errMsg =
                            String
                                .format("[createBankGroup] method not found, methodName=%s",
                                    methodName);
                        log.error(errMsg, methodName);
                        throw new OceanException(ResultStatus.SERVER_ERROR, errMsg);
                    }
                }
            } else {
                up.setScope(UserGroupScope.BANK);
                List<String> bankRoleModuleList =
                        Arrays.asList(EnvironmentConstants.BANK_ADMIN_DEFAULT_PERMISSION).stream()
                                .filter(permission -> permission.startsWith("MODULE_"))
                                .collect(Collectors.toList());
                for (String permissionName : bankRoleModuleList) {
                    String methodName = null;
                    try {
                        methodName = StringCustomizedUtils
                            .methodNameUnderlineToCamel("set", permissionName.trim());
                        Method method = up.getClass().getDeclaredMethod(methodName, Boolean.class);
                        method.invoke(up, Boolean.TRUE);
                    } catch (Exception e) {
                        String errMsg =
                            String
                                .format("[createBankGroup] method not found, methodName=%s",
                                    methodName);
                        log.error(errMsg, methodName);
                        throw new OceanException(ResultStatus.SERVER_ERROR, errMsg);
                    }
                }
            }

            Optional<UserGroupDO> userGroupOtp = userGroupDao.save(up);
            if (!userGroupOtp.isPresent()) {
                throw new OceanException(ResultStatus.DB_SAVE_ERROR, "Save user_group error");
            } else {
                return UserGroupDto.valueOf(userGroupOtp.get());
            }
        }
    }

    public UserGroupDto updateBankGroup(UserGroupDto userGroupDto) {
        Optional<UserGroupDO> userGroupOtp = userGroupDao
          .findByIdAndNotDelete(userGroupDto.getId());
        if (!userGroupOtp.isPresent()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Deleted groupId or illegal argument : groupId");
        } else {
            String name = userGroupDto.getName().trim();
            if (name.length() > 100) {
                throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT, "illegal argument : name");
            }

            UserGroupDO userGroup = userGroupOtp.get();
            boolean isExist = userGroupDao
              .existsByIssuerBankIdAndNameAndNotDelete(userGroup.getIssuerBankId(), name);
            if (isExist) {
                throw new OceanException(ResultStatus.DUPLICATE_DATA_ELEMENT,
                  "Update group failed, because group with specific name has already existed");
            } else {
                userGroup.setName(name);
                userGroup.setUpdater(userGroupDto.getUpdater());
                userGroup.setUpdateMillis(System.currentTimeMillis());
                userGroup.setAuditStatus(userGroupDto.getAuditStatus().toString());
                userGroupOtp = userGroupDao.save(userGroup);
                if (!userGroupOtp.isPresent()) {
                    throw new OceanException(ResultStatus.DB_SAVE_ERROR, "Update user_group error");
                } else {
                    return UserGroupDto.valueOf(userGroupOtp.get());
                }
            }
        }
    }

    public List<String> deleteBankGroupCheck(DeleteBankGroupReqDto request) {
        return accountGroupDao.findUserListWhoIsOnlyInOneGroup(request.getGroupId());
    }

    @Transactional
    public boolean deleteBankGroup(DeleteDataDTO deleteDataDto) {
        Optional<UserGroupDO> userGroupOtp = userGroupDao
          .findByIdAndNotDelete(deleteDataDto.getId());
        if (!userGroupOtp.isPresent()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Deleted groupId or illegal argument : groupId");
        } else {
            // delete  USER_GROUP
            UserGroupDO userGroup = userGroupOtp.get();
            userGroup.setAuditStatus(deleteDataDto.getAuditStatus().getSymbol());
            userGroupDao.delete(userGroup, deleteDataDto.getUser());
            // delete ACCOUNT_GROUP
            accountGroupDao.deleteByGroupId(userGroup.getId());
            return true;
        }
    }

    public PermissionDto getGroupPermissions(Long groupId) {
        Optional<UserGroupDO> userGroupOtp = userGroupDao.findByIdAndNotDelete(groupId);
        if (!userGroupOtp.isPresent()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Deleted groupId or illegal argument : groupId");
        } else {
            try {
                return PermissionDto.valueOf(userGroupOtp.get());

            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException |
              NoSuchMethodException | SecurityException e) {
                throw new OceanException(ResultStatus.SERVER_ERROR, e.getMessage(), e);
            }
        }
    }

    public UserGroupDto updateGroupPermission(UserGroupDto userGroupDto) {
        Optional<UserGroupDO> userGroupOtp = userGroupDao
          .findByIdAndNotDelete(userGroupDto.getId());
        if (!userGroupOtp.isPresent()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Deleted groupId or illegal argument : groupId");
        } else {
            UserGroupDO userGroup = userGroupOtp.get();
            updateByUserGroupDto(userGroup, userGroupDto);
            userGroup.setUpdater(userGroupDto.getUpdater());
            userGroup.setUpdateMillis(System.currentTimeMillis());
            userGroup.setAuditStatus(userGroupDto.getAuditStatus().toString());
            userGroupOtp = userGroupDao.save(userGroup);
            if (!userGroupOtp.isPresent()) {
                throw new OceanException(ResultStatus.DB_SAVE_ERROR, "Update user_group error");
            } else {
                return UserGroupDto.valueOf(userGroupOtp.get());
            }
        }
    }

    /**
     * 如果有修改權限相關請記得同步更改以下Class及{@Table user_group}
     * 1. {@link ocean.acs.models.oracle.entity.UserGroup} Field
     * 2. {@link ocean.acs.models.oracle.entity.UserGroup} All args constructor
     * 3. {@link ocean.acs.models.oracle.entity.UserGroup#valueOf(UserGroupDO)}
     * 4. {@link ocean.acs.models.sql_server.entity.UserGroup} Field
     * 5. {@link ocean.acs.models.sql_server.entity.UserGroup} All args constructor
     * 6. {@link ocean.acs.models.sql_server.entity.UserGroup#valueOf(UserGroupDO)
     * 7. {@link ocean.acs.models.entity.DBKey}}
     * 8. {@link UserGroupDO}
     * 9. {@link UserGroupDO#valueOf(ocean.acs.models.oracle.entity.UserGroup)}
     * 10. {@link UserGroupDO#valueOf(ocean.acs.models.sql_server.entity.UserGroup)}
     * 11. {@link UserGroupDto}
     * 12. {@link UserGroupDto#valueOf(UserGroupDO)}
     * 13. {@link Permission}
     * 14. {@link PermissionDto}
     * 15. {@link com.cherri.acs_portal.controller.request.UpdateGroupPermissionReqDto}
     * 16. {@link com.cherri.acs_portal.controller.request.UpdateGroupPermissionReqDto#checkUpdatingPermissionRule}
     * 17. {@link PermissionService#updateByUserGroupDto(UserGroupDO, UserGroupDto)}
     * 18. {@link PermissionService#createBankGroup(UserGroupDto)}
     */
    private void updateByUserGroupDto(UserGroupDO a, UserGroupDto d) {
        // NOTE: module 不能更新！
        a.setCanSeePanQuery(d.getCanSeePanQuery());
        a.setReportQuery(d.getReportQuery());
        a.setHealthCheckQuery(d.getHealthCheckQuery());
        a.setTxQuery(d.getTxQuery());
        a.setCardQuery(d.getCardQuery());
        a.setCardModify(d.getCardModify());
        a.setCardAudit(d.getCardAudit());
        a.setBlackListQuery(d.getBlackListQuery());
        a.setBlackListModify(d.getBlackListModify());
        a.setBlackListAudit(d.getBlackListAudit());
        a.setWhiteListQuery(d.getWhiteListQuery());
        a.setWhiteListModify(d.getWhiteListModify());
        a.setWhiteListAudit(d.getWhiteListAudit());
        a.setRiskControlQuery(d.getRiskControlQuery());
        a.setRiskControlModify(d.getRiskControlModify());
        a.setRiskControlAudit(d.getRiskControlAudit());
        a.setUserGroupQuery(d.getUserGroupQuery());
        a.setUserGroupModify(d.getUserGroupModify());
        a.setUserGroupAudit(d.getUserGroupAudit());
        a.setUnlockQuery(d.getUnlockQuery());
        a.setUnlockModify(d.getUnlockModify());
        a.setUnlockAudit(d.getUnlockAudit());
        a.setAuditLogQuery(d.getAuditLogQuery());
        a.setBankManageQuery(d.getBankManageQuery());
        a.setBankManageModify(d.getBankManageModify());
        a.setBankManageAudit(d.getBankManageAudit());
        a.setBankLogoQuery(d.getBankLogoQuery());
        a.setBankLogoModify(d.getBankLogoModify());
        a.setBankLogoAudit(d.getBankLogoAudit());
        a.setBankChannelQuery(d.getBankChannelQuery());
        a.setBankChannelModify(d.getBankChannelModify());
        a.setBankChannelAudit(d.getBankChannelAudit());
        a.setBankFeeQuery(d.getBankFeeQuery());
        a.setBankFeeModify(d.getBankFeeModify());
        a.setBankFeeAudit(d.getBankFeeAudit());
        a.setBankOtpSendingQuery(d.getBankOtpSendingQuery());
        a.setBankOtpSendingModify(d.getBankOtpSendingModify());
        a.setBankOtpSendingAudit(d.getBankOtpSendingAudit());
        a.setAcquirerBankQuery(d.getAcquirerBankQuery());
        a.setAcquirerBankModify(d.getAcquirerBankModify());
        a.setGeneralSettingQuery(d.getGeneralSettingQuery());
        a.setGeneralSettingModify(d.getGeneralSettingModify());
        a.setSysBinRangeQuery(d.getSysBinRangeQuery());
        a.setSysBinRangeModify(d.getSysBinRangeModify());
        a.setSysBinRangeAudit(d.getSysBinRangeAudit());
        a.setSysCardLogoQuery(d.getSysCardLogoQuery());
        a.setSysCardLogoModify(d.getSysCardLogoModify());
        a.setSysCardLogoAudit(d.getSysCardLogoAudit());
        a.setSysChallengeViewQuery(d.getSysChallengeViewQuery());
        a.setSysChallengeViewModify(d.getSysChallengeViewModify());
        a.setSysChallengeViewAudit(d.getSysChallengeViewAudit());
        a.setSysChallengeSmsMsgQuery(d.getSysChallengeSmsMsgQuery());
        a.setSysChallengeSmsMsgModify(d.getSysChallengeSmsMsgModify());
        a.setSysChallengeSmsMsgAudit(d.getSysChallengeSmsMsgAudit());
        a.setSysAcsOperatorIdQuery(d.getSysAcsOperatorIdQuery());
        a.setSysAcsOperatorIdModify(d.getSysAcsOperatorIdModify());
        a.setSysAcsOperatorIdAudit(d.getSysAcsOperatorIdAudit());
        a.setSysTimeoutQuery(d.getSysTimeoutQuery());
        a.setSysTimeoutModify(d.getSysTimeoutModify());
        a.setSysTimeoutAudit(d.getSysTimeoutAudit());
        a.setSysErrorCodeQuery(d.getSysErrorCodeQuery());
        a.setSysErrorCodeModify(d.getSysErrorCodeModify());
        a.setSysErrorCodeAudit(d.getSysErrorCodeAudit());
        a.setSysKeyQuery(d.getSysKeyQuery());
        a.setSysKeyModify(d.getSysKeyModify());
        a.setSysKeyAudit(d.getSysKeyAudit());
        a.setCertQuery(d.getCertQuery());
        a.setCertModify(d.getCertModify());
        a.setCertAudit(d.getCertAudit());
        a.setClassicRbaQuery(d.getClassicRbaQuery());
        a.setClassicRbaModify(d.getClassicRbaModify());
        a.setPluginIssuerPropertyQuery(d.getPluginIssuerPropertyQuery());
        a.setPluginIssuerPropertyModify(d.getPluginIssuerPropertyModify());
        a.setClassicRbaAudit(d.getClassicRbaAudit());
        a.setAccessMultiBank(d.getAccessMultiBank());
        a.setMimaPolicyModify(d.getMimaPolicyModify());
        a.setMimaPolicyQuery(d.getMimaPolicyQuery());
        a.setBankDataKeyQuery(d.getBankDataKeyQuery());
        a.setBankDataKeyModify(d.getBankDataKeyModify());
        a.setVelogQuery(d.getVelogQuery());
    }

    public PagingResultDTO<GroupMemberDto> getGroupMembers(
      Long groupId, PageQueryDTO pageQueryDto) {
        boolean isExist = userGroupDao.existsByIdAndNotDelete(groupId);
        if (!isExist) {
            throw new OceanException(
              ResultStatus.ILLEGAL_ARGUMENT, "Deleted groupId or illegal argument : groupId");
        }

        Page<Object[]> resultPage =
          userAccountDao.findByUserGroupIdAndNotDelete(groupId,
            PageQueryDO.builder().page(pageQueryDto.getPage()).pageSize(pageQueryDto.getPageSize())
              .build());
        List<GroupMemberDto> groupMembers =
          resultPage.getContent().stream()
            .map(
              ary ->
                new GroupMemberDto(
                  ary[0].toString(),
                  ary[1].toString(),
                  AuditStatus.getStatusBySymbol(ary[2].toString())))
            .collect(Collectors.toList());

        return new PagingResultDTO(resultPage.getTotalElements(), pageQueryDto, groupMembers);
    }

    public AccountGroupDto checkAccountGroupBeforeCreate(
      CreateOrDeleteGroupMemberReqDto createGroupMemberReqDto) {
        Optional<UserAccountDO> userAccountOpt = userAccountDao
          .findByIssuerBankIdAndAccountAndDeleteFlagFalse(createGroupMemberReqDto.getIssuerBankId(),
            createGroupMemberReqDto.getAccount());
        if (!userAccountOpt.isPresent()) {
            throw new OceanException(ResultStatus.NO_SUCH_DATA,
              "Account is not exist");
        }

        Optional<UserGroupDO> userGroupOpt = userGroupDao
          .findByIdAndNotDelete(createGroupMemberReqDto.getGroupId());
        if (!userGroupOpt.isPresent()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Delete group or illegal argument : groupId");
        }

        UserAccountDO userAccount = userAccountOpt.get();
        UserGroupDO userGroup = userGroupOpt.get();
        boolean isAccountGroupExist = accountGroupDao
          .existByAccountIdAndGroupId(userAccount.getId(), userGroup.getId());
        if (isAccountGroupExist) {
            throw new OceanException(ResultStatus.DUPLICATE_DATA_ELEMENT,
              "Create group member failed, because member has already been added to the group");
        }

        AccountGroupDto dto = new AccountGroupDto();
        dto.setAccountId(userAccount.getId());
        dto.setAccountName(userAccount.getAccount());
        dto.setGroupId(userGroup.getId());
        dto.setGroupName(userGroup.getName());
        dto.setCreator(createGroupMemberReqDto.getCreatorOrDeleter());
        return dto;
    }

    public AccountGroupDto createGroupMember(AccountGroupDto accountGroupDto) {
        boolean isAccountGroupExist = accountGroupDao
          .existByAccountIdAndGroupId(accountGroupDto.getAccountId(),
            accountGroupDto.getGroupId());
        if (isAccountGroupExist) {
            throw new OceanException(ResultStatus.DUPLICATE_DATA_ELEMENT,
              "Create group member failed, because member has already been added to the group");
        }

        AccountGroupDO ag = new AccountGroupDO();
        ag.setAccountId(accountGroupDto.getAccountId());
        ag.setGroupId(accountGroupDto.getGroupId());
        ag.setAuditStatus(accountGroupDto.getAuditStatus());
        ag.setCreator(accountGroupDto.getCreator());
        ag.setCreateMillis(System.currentTimeMillis());
        ag = accountGroupDao.save(ag);
        if (null == ag.getId()) {
            throw new OceanException(ResultStatus.DB_SAVE_ERROR, "Save account_group error");
        } else {
            return AccountGroupDto.valueOf(ag);
        }
    }

    @Transactional
    public void updateUserGroupList(Long userAccountId, List<Long> groupIdList, String creator) {
        // delete old group
        accountGroupDao.deleteByAccountId(userAccountId);
        // add user to groups
        for (Long groupId : groupIdList) {
            AccountGroupDto accountGroupDto = new AccountGroupDto();
            accountGroupDto.setAccountId(userAccountId);
            accountGroupDto.setGroupId(groupId);
            accountGroupDto.setAuditStatus(AuditStatus.PUBLISHED);
            accountGroupDto.setCreator(creator);
            this.createGroupMember(accountGroupDto);
        }
    }

    public DeleteGroupMemberDto checkAccountGroupBeforeDelete(
      CreateOrDeleteGroupMemberReqDto deleteGroupMemberReqDto) {
        Optional<UserAccountDO> userAccountOpt = userAccountDao
          .findByIssuerBankIdAndAccountAndDeleteFlagFalse(deleteGroupMemberReqDto.getIssuerBankId(),
            deleteGroupMemberReqDto.getAccount());
        if (!userAccountOpt.isPresent()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Delete account or illegal argument : issuerBankId or account");
        }

        Optional<UserGroupDO> userGroupOpt = userGroupDao
          .findByIdAndNotDelete(deleteGroupMemberReqDto.getGroupId());
        if (!userGroupOpt.isPresent()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Delete group or illegal argument : groupId");
        }

        UserAccountDO userAccount = userAccountOpt.get();
        UserGroupDO userGroup = userGroupOpt.get();
        Optional<Long> idOpt = accountGroupDao
          .findIdByAccountIdAndGroupId(userAccount.getId(), userGroup.getId());
        if (!idOpt.isPresent()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "User account does not have this group permission");
        }

        DeleteGroupMemberDto dto = DeleteGroupMemberDto.getBuilder().id(idOpt.get())
          .issuerBankId(deleteGroupMemberReqDto.getIssuerBankId())
          .auditStatus(AuditStatus.PUBLISHED)
          .user(deleteGroupMemberReqDto.getCreatorOrDeleter())
          .accountName(userAccount.getAccount())
          .groupName(userGroup.getName()).build();
        return dto;
    }

    public boolean deleteGroupMember(DeleteDataDTO deleteDataDto) {
        AccountGroupDO accountGroupDO =
            accountGroupDao.findById(deleteDataDto.getId()).orElse(null);
        if (accountGroupDO == null) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "User account does not have this group permission");
        }

        List<UserGroupDO> userGroupList = userGroupDao
            .findByUserAccountIdAndNotDelete(accountGroupDO.getAccountId());
        if (userGroupList.size() == 1) {
            throw new OceanException(ResultStatus.ACCOUNT_ONLY_IN_ONE_GROUP);
        }

        accountGroupDao.deleteById(deleteDataDto.getId());
        return true;
    }

    public boolean checkAccessMultipleBank(CustomizedUserPrincipal userPrincipal,
      Long filterIssuerBankId) {
        if (null == filterIssuerBankId) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "missing argument : issuerBankId");
        }

        if (userPrincipal.getRoles().contains(Permission.ACCESS_MULTI_BANK.getSymbol())) {
            return true;
        } else {
            return userPrincipal.getIssuerBankId().longValue() == filterIssuerBankId.longValue();
        }
    }

    public boolean checkAuditPermission(CustomizedUserPrincipal userPrincipal,
      Long filterIssuerBankId, String functionTypeArg) {
        if (null == filterIssuerBankId) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "missing argument : issuerBankId");
        }

        if (StringCustomizedUtils.isEmpty(functionTypeArg)) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "missing argument : functionType");
        }
        AuditFunctionType functionType = AuditFunctionType.getBySymbol(functionTypeArg);

        return userPrincipal.getRoles().contains(functionType.getPermissionType().getSymbol());
    }

    public Set<ModuleSettingPermissionResponse> getModuleSettingPermission() {
        List<ModuleSettingDO> moduleSettingList = moduleSettingDao.findAll();
        Set<ModuleSettingPermissionResponse> moduleSettings =
          new HashSet<>(moduleSettingList.size());

        for (ModuleSettingDO moduleSetting : moduleSettingList) {
            AuditFunctionType auditFunctionType =
              AuditFunctionType.getBySymbol(moduleSetting.getFunctionType());

            if (AuditFunctionType.UNKNOWN != auditFunctionType) {
                boolean isAuditingFunctionEnabled = moduleSetting.getAuditDemand();
                if (isAuditingFunctionEnabled) {
                    moduleSettings.add(
                      new ModuleSettingPermissionResponse(
                        auditFunctionType.getPermissionType().getPermissionDtoField(),
                        true));
                }
            }
        }

        return moduleSettings;
    }
}
