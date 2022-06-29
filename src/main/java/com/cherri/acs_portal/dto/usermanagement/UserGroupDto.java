package com.cherri.acs_portal.dto.usermanagement;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.controller.request.CreateBankGroupReqDto;
import com.cherri.acs_portal.controller.request.UpdateBankGroupReqDto;
import com.cherri.acs_portal.controller.request.UpdateGroupPermissionReqDto;
import com.cherri.acs_portal.controller.response.PermissionDto;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.service.PermissionService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.enumerator.Permission;
import ocean.acs.commons.enumerator.UserGroupScope;
import ocean.acs.commons.enumerator.UserGroupType;
import ocean.acs.models.data_object.entity.UserGroupDO;
import org.apache.commons.lang3.builder.ToStringBuilder;

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
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserGroupDto extends AuditableDTO {

    private Long id;
    private Long issuerBankId;
    private String name;
    private UserGroupType type;
    private UserGroupScope scope;

    private Boolean moduleCanSeePan;
    private Boolean canSeePanQuery;
    private Boolean moduleReport;
    private Boolean reportQuery;
    private Boolean moduleHealthCheck;
    private Boolean healthCheckQuery;
    private Boolean moduleTx;
    private Boolean txQuery;
    private Boolean moduleCard;
    private Boolean cardQuery;
    private Boolean cardModify;
    private Boolean cardAudit;
    private Boolean moduleRiskBlackList;
    private Boolean blackListQuery;
    private Boolean blackListModify;
    private Boolean blackListAudit;
    private Boolean moduleRiskWhiteList;
    private Boolean whiteListQuery;
    private Boolean whiteListModify;
    private Boolean whiteListAudit;
    private Boolean moduleRiskControl;
    private Boolean riskControlQuery;
    private Boolean riskControlModify;
    private Boolean riskControlAudit;
    private Boolean moduleUserGroup;
    private Boolean userGroupQuery;
    private Boolean userGroupModify;
    private Boolean userGroupAudit;
    private Boolean moduleUserUnlock;
    private Boolean unlockQuery;
    private Boolean unlockModify;
    private Boolean unlockAudit;
    private Boolean moduleUserAuditLog;
    private Boolean auditLogQuery;
    private Boolean moduleBankManage;
    private Boolean bankManageQuery;
    private Boolean bankManageModify;
    private Boolean bankManageAudit;
    private Boolean moduleBankLogo;
    private Boolean bankLogoQuery;
    private Boolean bankLogoModify;
    private Boolean bankLogoAudit;
    private Boolean moduleBankChannel;
    private Boolean bankChannelQuery;
    private Boolean bankChannelModify;
    private Boolean bankChannelAudit;
    private Boolean moduleBankFee;
    private Boolean bankFeeQuery;
    private Boolean bankFeeModify;
    private Boolean bankFeeAudit;
    private Boolean moduleBankOtpSending;
    private Boolean bankOtpSendingQuery;
    private Boolean bankOtpSendingModify;
    private Boolean bankOtpSendingAudit;
    private Boolean moduleGeneralSetting;
    private Boolean generalSettingModify;
    private Boolean generalSettingQuery;
    private Boolean moduleSysBinRange;
    private Boolean sysBinRangeQuery;
    private Boolean sysBinRangeModify;
    private Boolean sysBinRangeAudit;
    private Boolean moduleSysCardLogo;
    private Boolean sysCardLogoQuery;
    private Boolean sysCardLogoModify;
    private Boolean sysCardLogoAudit;
    private Boolean moduleSysChallengeView;
    private Boolean sysChallengeViewQuery;
    private Boolean sysChallengeViewModify;
    private Boolean sysChallengeViewAudit;
    private Boolean moduleSysChallengeSmsMsg;
    private Boolean sysChallengeSmsMsgQuery;
    private Boolean sysChallengeSmsMsgModify;
    private Boolean sysChallengeSmsMsgAudit;
    private Boolean moduleSysAcsOperatorId;
    private Boolean sysAcsOperatorIdQuery;
    private Boolean sysAcsOperatorIdModify;
    private Boolean sysAcsOperatorIdAudit;
    private Boolean moduleSysTimeout;
    private Boolean sysTimeoutQuery;
    private Boolean sysTimeoutModify;
    private Boolean sysTimeoutAudit;
    private Boolean moduleSysErrorCode;
    private Boolean sysErrorCodeQuery;
    private Boolean sysErrorCodeModify;
    private Boolean sysErrorCodeAudit;
    private Boolean moduleSysKey;
    private Boolean sysKeyQuery;
    private Boolean sysKeyModify;
    private Boolean sysKeyAudit;
    private Boolean moduleCert;
    private Boolean certQuery;
    private Boolean certModify;
    private Boolean certAudit;
    private Boolean moduleClassicRba;
    private Boolean classicRbaQuery;
    private Boolean classicRbaModify;
    private Boolean modulePluginIssuerProperty;
    private Boolean pluginIssuerPropertyQuery;
    private Boolean pluginIssuerPropertyModify;
    private Boolean classicRbaAudit;
    private Boolean accessMultiBank;
    private AuditStatus auditStatus;
    private Boolean moduleMimaPolicy;
    private Boolean mimaPolicyModify;
    private Boolean mimaPolicyQuery;
    private Boolean moduleAcquirerBank;
    private Boolean acquirerBankModify;
    private Boolean acquirerBankQuery;
    private Boolean moduleBankDataKey;
    private Boolean bankDataKeyQuery;
    private Boolean bankDataKeyModify;
    private Boolean moduleVelog;
    private Boolean velogQuery;

    private String creator;
    private Long createMillis;
    private String updater;
    private Long updateMillis;
    private Boolean deleteFlag;
    private String deleter;
    private Long deleteMillis;

    public static UserGroupDto valueOf(UserGroupDO entity) {
        UserGroupDto dto = new UserGroupDto();
        dto.setId(entity.getId());
        dto.setIssuerBankId(entity.getIssuerBankId());
        dto.setName(entity.getName());
        dto.setType(entity.getType());
        dto.setScope(entity.getScope());

        dto.setModuleCanSeePan(entity.getModuleCanSeePan());
        dto.setCanSeePanQuery(entity.getCanSeePanQuery());
        dto.setModuleReport(entity.getModuleReport());
        dto.setReportQuery(entity.getReportQuery());
        dto.setModuleHealthCheck(entity.getModuleHealthCheck());
        dto.setHealthCheckQuery(entity.getHealthCheckQuery());
        dto.setModuleTx(entity.getModuleTx());
        dto.setTxQuery(entity.getTxQuery());
        dto.setModuleCard(entity.getModuleCard());
        dto.setCardQuery(entity.getCardQuery());
        dto.setCardModify(entity.getCardModify());
        dto.setCardAudit(entity.getCardAudit());
        dto.setModuleRiskBlackList(entity.getModuleRiskBlackList());
        dto.setBlackListQuery(entity.getBlackListQuery());
        dto.setBlackListModify(entity.getBlackListModify());
        dto.setBlackListAudit(entity.getBlackListAudit());
        dto.setModuleRiskWhiteList(entity.getModuleRiskWhiteList());
        dto.setWhiteListQuery(entity.getWhiteListQuery());
        dto.setWhiteListModify(entity.getWhiteListModify());
        dto.setWhiteListAudit(entity.getWhiteListAudit());
        dto.setModuleRiskControl(entity.getModuleRiskControl());
        dto.setRiskControlQuery(entity.getRiskControlQuery());
        dto.setRiskControlModify(entity.getRiskControlModify());
        dto.setRiskControlAudit(entity.getRiskControlAudit());
        dto.setModuleUserGroup(entity.getModuleUserGroup());
        dto.setUserGroupQuery(entity.getUserGroupQuery());
        dto.setUserGroupModify(entity.getUserGroupModify());
        dto.setUserGroupAudit(entity.getUserGroupAudit());
        dto.setModuleUserUnlock(entity.getModuleUserUnlock());
        dto.setUnlockQuery(entity.getUnlockQuery());
        dto.setUnlockModify(entity.getUnlockModify());
        dto.setUnlockAudit(entity.getUnlockAudit());
        dto.setModuleUserAuditLog(entity.getModuleUserAuditLog());
        dto.setAuditLogQuery(entity.getAuditLogQuery());
        dto.setModuleBankManage(entity.getModuleBankManage());
        dto.setBankManageQuery(entity.getBankManageQuery());
        dto.setBankManageModify(entity.getBankManageModify());
        dto.setBankManageAudit(entity.getBankManageAudit());
        dto.setModuleBankLogo(entity.getModuleBankLogo());
        dto.setBankLogoQuery(entity.getBankLogoQuery());
        dto.setBankLogoModify(entity.getBankLogoModify());
        dto.setBankLogoAudit(entity.getBankLogoAudit());
        dto.setModuleBankChannel(entity.getModuleBankChannel());
        dto.setBankChannelQuery(entity.getBankChannelQuery());
        dto.setBankChannelModify(entity.getBankChannelModify());
        dto.setBankChannelAudit(entity.getBankChannelAudit());
        dto.setModuleBankFee(entity.getModuleBankFee());
        dto.setBankFeeQuery(entity.getBankFeeQuery());
        dto.setBankFeeModify(entity.getBankFeeModify());
        dto.setBankFeeAudit(entity.getBankFeeAudit());
        dto.setModuleBankOtpSending(entity.getModuleBankOtpSending());
        dto.setBankOtpSendingQuery(entity.getBankOtpSendingQuery());
        dto.setBankOtpSendingModify(entity.getBankOtpSendingModify());
        dto.setBankOtpSendingAudit(entity.getBankOtpSendingAudit());
        dto.setModuleGeneralSetting(entity.getModuleGeneralSetting());
        dto.setGeneralSettingQuery(entity.getGeneralSettingQuery());
        dto.setGeneralSettingModify(entity.getGeneralSettingModify());
        dto.setModuleSysBinRange(entity.getModuleSysBinRange());
        dto.setSysBinRangeQuery(entity.getSysBinRangeQuery());
        dto.setSysBinRangeModify(entity.getSysBinRangeModify());
        dto.setSysBinRangeAudit(entity.getSysBinRangeAudit());
        dto.setModuleSysCardLogo(entity.getModuleSysCardLogo());
        dto.setSysCardLogoQuery(entity.getSysCardLogoQuery());
        dto.setSysCardLogoModify(entity.getSysCardLogoModify());
        dto.setSysCardLogoAudit(entity.getSysCardLogoAudit());
        dto.setModuleSysChallengeView(entity.getModuleSysChallengeView());
        dto.setSysChallengeViewQuery(entity.getSysChallengeViewQuery());
        dto.setSysChallengeViewModify(entity.getSysChallengeViewModify());
        dto.setSysChallengeViewAudit(entity.getSysChallengeViewAudit());
        dto.setModuleSysChallengeSmsMsg(entity.getModuleSysChallengeSmsMsg());
        dto.setSysChallengeSmsMsgQuery(entity.getSysChallengeSmsMsgQuery());
        dto.setSysChallengeSmsMsgModify(entity.getSysChallengeSmsMsgModify());
        dto.setSysChallengeSmsMsgAudit(entity.getSysChallengeSmsMsgAudit());
        dto.setModuleSysAcsOperatorId(entity.getModuleSysAcsOperatorId());
        dto.setSysAcsOperatorIdQuery(entity.getSysAcsOperatorIdQuery());
        dto.setSysAcsOperatorIdModify(entity.getSysAcsOperatorIdModify());
        dto.setSysAcsOperatorIdAudit(entity.getSysAcsOperatorIdAudit());
        dto.setModuleSysTimeout(entity.getModuleSysTimeout());
        dto.setSysTimeoutQuery(entity.getSysTimeoutQuery());
        dto.setSysTimeoutModify(entity.getSysTimeoutModify());
        dto.setSysTimeoutAudit(entity.getSysTimeoutAudit());
        dto.setModuleSysErrorCode(entity.getModuleSysErrorCode());
        dto.setSysErrorCodeQuery(entity.getSysErrorCodeQuery());
        dto.setSysErrorCodeModify(entity.getSysErrorCodeModify());
        dto.setSysErrorCodeAudit(entity.getSysErrorCodeAudit());
        dto.setModuleSysKey(entity.getModuleSysKey());
        dto.setSysKeyQuery(entity.getSysKeyQuery());
        dto.setSysKeyModify(entity.getSysKeyModify());
        dto.setSysKeyAudit(entity.getSysKeyAudit());
        dto.setModuleCert(entity.getModuleCert());
        dto.setCertQuery(entity.getCertQuery());
        dto.setCertModify(entity.getCertModify());
        dto.setCertAudit(entity.getCertAudit());
        dto.setModuleClassicRba(entity.getModuleClassicRba());
        dto.setClassicRbaQuery(entity.getClassicRbaQuery());
        dto.setClassicRbaModify(entity.getClassicRbaModify());
        dto.setClassicRbaAudit(entity.getClassicRbaAudit());
        dto.setModulePluginIssuerProperty(entity.getModulePluginIssuerProperty());
        dto.setPluginIssuerPropertyQuery(entity.getPluginIssuerPropertyQuery());
        dto.setPluginIssuerPropertyModify(entity.getPluginIssuerPropertyModify());
        dto.setAccessMultiBank(entity.getAccessMultiBank());
        dto.setAuditStatus(AuditStatus.getStatusBySymbol(entity.getAuditStatus()));
        dto.setModuleMimaPolicy(entity.getModuleMimaPolicy());
        dto.setMimaPolicyModify(entity.getMimaPolicyModify());
        dto.setMimaPolicyQuery(entity.getMimaPolicyQuery());
        dto.setModuleAcquirerBank(entity.getModuleAcquirerBank());
        dto.setAcquirerBankQuery(entity.getAcquirerBankQuery());
        dto.setAcquirerBankModify(entity.getAcquirerBankModify());
        dto.setModuleBankDataKey(entity.getModuleBankDataKey());
        dto.setBankDataKeyQuery(entity.getBankDataKeyQuery());
        dto.setBankDataKeyModify(entity.getBankDataKeyModify());
        dto.setModuleVelog(entity.getModuleVelog());
        dto.setVelogQuery(entity.getVelogQuery());
        return dto;
    }

    public static UserGroupDto valueOf(UpdateGroupPermissionReqDto d) {
        UserGroupDto userGroupDto = new UserGroupDto();
        userGroupDto.setId(d.getGroupId());
        userGroupDto.setIssuerBankId(d.getIssuerBankId());

        /* Module */
        userGroupDto.setModuleCanSeePan(d.getModuleCanSeePan());
        userGroupDto.setCanSeePanQuery(d.getCanSeePanQuery());
        userGroupDto.setModuleReport(d.getModuleReport());
        userGroupDto.setReportQuery(d.getReportQuery());
        userGroupDto.setModuleHealthCheck(d.getModuleHealthCheck());
        userGroupDto.setHealthCheckQuery(d.getHealthCheckQuery());
        userGroupDto.setModuleTx(d.getModuleTx());
        userGroupDto.setTxQuery(d.getTxQuery());
        userGroupDto.setModuleCard(d.getModuleCard());
        userGroupDto.setCardQuery(d.getCardQuery());
        userGroupDto.setCardModify(d.getCardModify());
        userGroupDto.setCardAudit(d.getCardAudit());
        userGroupDto.setModuleRiskBlackList(d.getModuleRiskBlackList());
        userGroupDto.setBlackListQuery(d.getBlackListQuery());
        userGroupDto.setBlackListModify(d.getBlackListModify());
        userGroupDto.setBlackListAudit(d.getBlackListAudit());
        userGroupDto.setModuleRiskWhiteList(d.getModuleRiskWhiteList());
        userGroupDto.setWhiteListQuery(d.getWhiteListQuery());
        userGroupDto.setWhiteListModify(d.getWhiteListModify());
        userGroupDto.setWhiteListAudit(d.getWhiteListAudit());
        userGroupDto.setModuleRiskControl(d.getModuleRiskControl());
        userGroupDto.setRiskControlQuery(d.getRiskControlQuery());
        userGroupDto.setRiskControlModify(d.getRiskControlModify());
        userGroupDto.setRiskControlAudit(d.getRiskControlAudit());
        userGroupDto.setModuleUserGroup(d.getModuleUserGroup());
        userGroupDto.setUserGroupQuery(d.getUserGroupQuery());
        userGroupDto.setUserGroupModify(d.getUserGroupModify());
        userGroupDto.setUserGroupAudit(d.getUserGroupAudit());
        userGroupDto.setModuleUserUnlock(d.getModuleUserUnlock());
        userGroupDto.setUnlockQuery(d.getUnlockQuery());
        userGroupDto.setUnlockModify(d.getUnlockModify());
        userGroupDto.setUnlockAudit(d.getUnlockAudit());
        userGroupDto.setModuleUserAuditLog(d.getModuleUserAuditLog());
        userGroupDto.setAuditLogQuery(d.getAuditLogQuery());
        userGroupDto.setModuleBankManage(d.getModuleBankManage());
        userGroupDto.setBankManageQuery(d.getBankManageQuery());
        userGroupDto.setBankManageModify(d.getBankManageModify());
        userGroupDto.setBankManageAudit(d.getBankManageAudit());
        userGroupDto.setModuleBankLogo(d.getModuleBankLogo());
        userGroupDto.setBankLogoQuery(d.getBankLogoQuery());
        userGroupDto.setBankLogoModify(d.getBankLogoModify());
        userGroupDto.setBankLogoAudit(d.getBankLogoAudit());
        userGroupDto.setModuleBankChannel(d.getModuleBankChannel());
        userGroupDto.setBankChannelQuery(d.getBankChannelQuery());
        userGroupDto.setBankChannelModify(d.getBankChannelModify());
        userGroupDto.setBankChannelAudit(d.getBankChannelAudit());
        userGroupDto.setModuleBankFee(d.getModuleBankFee());
        userGroupDto.setBankFeeQuery(d.getBankFeeQuery());
        userGroupDto.setBankFeeModify(d.getBankFeeModify());
        userGroupDto.setBankFeeAudit(d.getBankFeeAudit());
        userGroupDto.setModuleBankOtpSending(d.getModuleBankOtpSending());
        userGroupDto.setBankOtpSendingQuery(d.getBankOtpSendingQuery());
        userGroupDto.setBankOtpSendingModify(d.getBankOtpSendingModify());
        userGroupDto.setBankOtpSendingAudit(d.getBankOtpSendingAudit());
        userGroupDto.setModuleGeneralSetting(d.getModuleGeneralSetting());
        userGroupDto.setGeneralSettingQuery(d.getGeneralSettingQuery());
        userGroupDto.setGeneralSettingModify(d.getGeneralSettingModify());
        userGroupDto.setModuleSysBinRange(d.getModuleSysBinRange());
        userGroupDto.setSysBinRangeQuery(d.getSysBinRangeQuery());
        userGroupDto.setSysBinRangeModify(d.getSysBinRangeModify());
        userGroupDto.setSysBinRangeAudit(d.getSysBinRangeAudit());
        userGroupDto.setModuleSysCardLogo(d.getModuleSysCardLogo());
        userGroupDto.setSysCardLogoQuery(d.getSysCardLogoQuery());
        userGroupDto.setSysCardLogoModify(d.getSysCardLogoModify());
        userGroupDto.setSysCardLogoAudit(d.getSysCardLogoAudit());
        userGroupDto.setModuleSysChallengeView(d.getModuleSysChallengeView());
        userGroupDto.setSysChallengeViewQuery(d.getSysChallengeViewQuery());
        userGroupDto.setSysChallengeViewModify(d.getSysChallengeViewModify());
        userGroupDto.setSysChallengeViewAudit(d.getSysChallengeViewAudit());
        userGroupDto.setModuleSysChallengeSmsMsg(d.getModuleSysChallengeSmsMsg());
        userGroupDto.setSysChallengeSmsMsgQuery(d.getSysChallengeSmsMsgQuery());
        userGroupDto.setSysChallengeSmsMsgModify(d.getSysChallengeSmsMsgModify());
        userGroupDto.setSysChallengeSmsMsgAudit(d.getSysChallengeSmsMsgAudit());
        userGroupDto.setModuleSysAcsOperatorId(d.getModuleSysAcsOperatorId());
        userGroupDto.setSysAcsOperatorIdQuery(d.getSysAcsOperatorIdQuery());
        userGroupDto.setSysAcsOperatorIdModify(d.getSysAcsOperatorIdModify());
        userGroupDto.setSysAcsOperatorIdAudit(d.getSysAcsOperatorIdAudit());
        userGroupDto.setModuleSysTimeout(d.getModuleSysTimeout());
        userGroupDto.setSysTimeoutQuery(d.getSysTimeoutQuery());
        userGroupDto.setSysTimeoutModify(d.getSysTimeoutModify());
        userGroupDto.setSysTimeoutAudit(d.getSysTimeoutAudit());
        userGroupDto.setModuleSysErrorCode(d.getModuleSysErrorCode());
        userGroupDto.setSysErrorCodeQuery(d.getSysErrorCodeQuery());
        userGroupDto.setSysErrorCodeModify(d.getSysErrorCodeModify());
        userGroupDto.setSysErrorCodeAudit(d.getSysErrorCodeAudit());
        userGroupDto.setModuleSysKey(d.getModuleSysKey());
        userGroupDto.setSysKeyQuery(d.getSysKeyQuery());
        userGroupDto.setSysKeyModify(d.getSysKeyModify());
        userGroupDto.setSysKeyAudit(d.getSysKeyAudit());
        userGroupDto.setModuleCert(d.getModuleCert());
        userGroupDto.setCertQuery(d.getCertQuery());
        userGroupDto.setCertModify(d.getCertModify());
        userGroupDto.setCertAudit(d.getCertAudit());
        userGroupDto.setModuleClassicRba(d.getModuleClassicRba());
        userGroupDto.setClassicRbaQuery(d.getClassicRbaQuery());
        userGroupDto.setClassicRbaModify(d.getClassicRbaModify());
        userGroupDto.setModulePluginIssuerProperty(d.getModulePluginIssuerProperty());
        userGroupDto.setPluginIssuerPropertyQuery(d.getPluginIssuerPropertyQuery());
        userGroupDto.setPluginIssuerPropertyModify(d.getPluginIssuerPropertyModify());
        userGroupDto.setClassicRbaAudit(d.getClassicRbaAudit());
        userGroupDto.setAccessMultiBank(d.getAccessMultiBank());
        userGroupDto.setModuleMimaPolicy(d.getModuleMimaPolicy());
        userGroupDto.setMimaPolicyModify(d.getMimaPolicyModify());
        userGroupDto.setMimaPolicyQuery(d.getMimaPolicyQuery());
        userGroupDto.setModuleAcquirerBank(d.getModuleAcquirerBank());
        userGroupDto.setAcquirerBankQuery(d.getAcquirerBankQuery());
        userGroupDto.setAcquirerBankModify(d.getAcquirerBankModify());
        userGroupDto.setModuleBankDataKey(d.getModuleBankDataKey());
        userGroupDto.setBankDataKeyQuery(d.getBankDataKeyQuery());
        userGroupDto.setBankDataKeyModify(d.getBankDataKeyModify());
        userGroupDto.setModuleVelog(d.getModuleVelog());
        userGroupDto.setVelogQuery(d.getVelogQuery());
        return userGroupDto;
    }

    public static UserGroupDto valueOf(CreateBankGroupReqDto createBankGroupReqDto) {
        UserGroupDto dto = new UserGroupDto();
        dto.setIssuerBankId(createBankGroupReqDto.getIssuerBankId());
        dto.setName(createBankGroupReqDto.getName());
        return dto;
    }

    public static UserGroupDto valueOf(UpdateBankGroupReqDto updateBankGroupReqDto) {
        UserGroupDto dto = new UserGroupDto();
        dto.setIssuerBankId(updateBankGroupReqDto.getIssuerBankId());
        dto.setId(updateBankGroupReqDto.getGroupId());
        dto.setName(updateBankGroupReqDto.getName());
        return dto;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }

    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.USER_GROUP;
    }

}
