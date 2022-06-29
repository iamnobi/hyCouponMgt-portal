package ocean.acs.models.data_object.entity;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ocean.acs.commons.annotation.Role;
import ocean.acs.commons.enumerator.Permission;
import ocean.acs.commons.enumerator.UserGroupScope;
import ocean.acs.commons.enumerator.UserGroupType;

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
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class UserGroupDO extends OperatorInfoDO {

    private Long id;
    private Long issuerBankId;
    private String name;
    private UserGroupType type;
    private UserGroupScope scope;
    private String auditStatus;

    @Role(Permission.MODULE_CAN_SEE_PAN)
    private Boolean moduleCanSeePan = Boolean.FALSE;
    @Role(Permission.CAN_SEE_PAN_QUERY)
    private Boolean canSeePanQuery = Boolean.FALSE;

    @Role(Permission.MODULE_REPORT)
    private Boolean moduleReport = Boolean.FALSE;
    @Role(Permission.REPORT_QUERY)
    private Boolean reportQuery = Boolean.FALSE;

    @Role(Permission.MODULE_HEALTH_CHECK)
    private Boolean moduleHealthCheck = Boolean.FALSE;
    @Role(Permission.HEALTH_CHECK_QUERY)
    private Boolean healthCheckQuery = Boolean.FALSE;
    @Role(Permission.MODULE_TX)
    private Boolean moduleTx = Boolean.FALSE;
    @Role(Permission.TX_QUERY)
    private Boolean txQuery = Boolean.FALSE;

    @Role(Permission.MODULE_CARD)
    private Boolean moduleCard = Boolean.FALSE;
    @Role(Permission.CARD_QUERY)
    private Boolean cardQuery = Boolean.FALSE;
    @Role(Permission.CARD_MODIFY)
    private Boolean cardModify = Boolean.FALSE;
    @Role(Permission.CARD_AUDIT)
    private Boolean cardAudit = Boolean.FALSE;

    @Role(Permission.MODULE_RISK_BLACK_LIST)
    private Boolean moduleRiskBlackList = Boolean.FALSE;
    @Role(Permission.BLACK_LIST_QUERY)
    private Boolean blackListQuery = Boolean.FALSE;
    @Role(Permission.BLACK_LIST_MODIFY)
    private Boolean blackListModify = Boolean.FALSE;
    @Role(Permission.BLACK_LIST_AUDIT)
    private Boolean blackListAudit = Boolean.FALSE;

    @Role(Permission.MODULE_RISK_WHITE_LIST)
    private Boolean moduleRiskWhiteList = Boolean.FALSE;
    @Role(Permission.WHITE_LIST_QUERY)
    private Boolean whiteListQuery = Boolean.FALSE;
    @Role(Permission.WHITE_LIST_MODIFY)
    private Boolean whiteListModify = Boolean.FALSE;
    @Role(Permission.WHITE_LIST_AUDIT)
    private Boolean whiteListAudit = Boolean.FALSE;

    @Role(Permission.MODULE_RISK_CONTROL)
    private Boolean moduleRiskControl = Boolean.FALSE;
    @Role(Permission.RISK_CONTROL_QUERY)
    private Boolean riskControlQuery = Boolean.FALSE;
    @Role(Permission.RISK_CONTROL_MODIFY)
    private Boolean riskControlModify = Boolean.FALSE;
    @Role(Permission.RISK_CONTROL_AUDIT)
    private Boolean riskControlAudit = Boolean.FALSE;

    @Role(Permission.MODULE_USER_GROUP)
    private Boolean moduleUserGroup = Boolean.FALSE;
    @Role(Permission.USER_GROUP_QUERY)
    private Boolean userGroupQuery = Boolean.FALSE;
    @Role(Permission.USER_GROUP_MODIFY)
    private Boolean userGroupModify = Boolean.FALSE;
    @Role(Permission.USER_GROUP_AUDIT)
    private Boolean userGroupAudit = Boolean.FALSE;

    @Role(Permission.MODULE_USER_UNLOCK)
    private Boolean moduleUserUnlock = Boolean.FALSE;
    @Role(Permission.UNLOCK_QUERY)
    private Boolean unlockQuery = Boolean.FALSE;
    @Role(Permission.UNLOCK_MODIFY)
    private Boolean unlockModify = Boolean.FALSE;
    @Role(Permission.UNLOCK_AUDIT)
    private Boolean unlockAudit = Boolean.FALSE;

    @Role(Permission.MODULE_USER_AUDIT_LOG)
    private Boolean moduleUserAuditLog = Boolean.FALSE;
    @Role(Permission.AUDIT_LOG_QUERY)
    private Boolean auditLogQuery = Boolean.FALSE;

    @Role(Permission.MODULE_BANK_MANAGE)
    private Boolean moduleBankManage = Boolean.FALSE;
    @Role(Permission.BANK_MANAGE_QUERY)
    private Boolean bankManageQuery = Boolean.FALSE;
    @Role(Permission.BANK_MANAGE_MODIFY)
    private Boolean bankManageModify = Boolean.FALSE;
    @Role(Permission.BANK_MANAGE_AUDIT)
    private Boolean bankManageAudit = Boolean.FALSE;

    @Role(Permission.MODULE_BANK_LOGO)
    private Boolean moduleBankLogo = Boolean.FALSE;
    @Role(Permission.BANK_LOGO_QUERY)
    private Boolean bankLogoQuery = Boolean.FALSE;
    @Role(Permission.BANK_LOGO_MODIFY)
    private Boolean bankLogoModify = Boolean.FALSE;
    @Role(Permission.BANK_LOGO_AUDIT)
    private Boolean bankLogoAudit = Boolean.FALSE;

    @Role(Permission.MODULE_BANK_CHANNEL)
    private Boolean moduleBankChannel = Boolean.FALSE;
    @Role(Permission.BANK_CHANNEL_QUERY)
    private Boolean bankChannelQuery = Boolean.FALSE;
    @Role(Permission.BANK_CHANNEL_MODIFY)
    private Boolean bankChannelModify = Boolean.FALSE;
    @Role(Permission.BANK_CHANNEL_AUDIT)
    private Boolean bankChannelAudit = Boolean.FALSE;

    @Role(Permission.MODULE_BANK_FEE)
    private Boolean moduleBankFee = Boolean.FALSE;
    @Role(Permission.BANK_FEE_QUERY)
    private Boolean bankFeeQuery = Boolean.FALSE;
    @Role(Permission.BANK_FEE_MODIFY)
    private Boolean bankFeeModify = Boolean.FALSE;
    @Role(Permission.BANK_FEE_AUDIT)
    private Boolean bankFeeAudit = Boolean.FALSE;

    @Role(Permission.MODULE_BANK_OTP_SENDING)
    private Boolean moduleBankOtpSending = Boolean.FALSE;
    @Role(Permission.BANK_OTP_SENDING_QUERY)
    private Boolean bankOtpSendingQuery = Boolean.FALSE;
    @Role(Permission.BANK_OTP_SENDING_MODIFY)
    private Boolean bankOtpSendingModify = Boolean.FALSE;
    @Role(Permission.BANK_OTP_SENDING_AUDIT)
    private Boolean bankOtpSendingAudit = Boolean.FALSE;

    @Role(Permission.MODULE_SYS_BIN_RANGE)
    private Boolean moduleSysBinRange = Boolean.FALSE;
    @Role(Permission.SYS_BIN_RANGE_QUERY)
    private Boolean sysBinRangeQuery = Boolean.FALSE;
    @Role(Permission.SYS_BIN_RANGE_MODIFY)
    private Boolean sysBinRangeModify = Boolean.FALSE;
    @Role(Permission.SYS_BIN_RANGE_AUDIT)
    private Boolean sysBinRangeAudit = Boolean.FALSE;

    @Role(Permission.MODULE_GENERAL_SETTING)
    private Boolean moduleGeneralSetting = Boolean.FALSE;
    @Role(Permission.GENERAL_SETTING_QUERY)
    private Boolean generalSettingQuery = Boolean.FALSE;
    @Role(Permission.GENERAL_SETTING_MODIFY)
    private Boolean generalSettingModify = Boolean.FALSE;

    @Role(Permission.MODULE_SYS_CARD_LOGO)
    private Boolean moduleSysCardLogo = Boolean.FALSE;
    @Role(Permission.SYS_CARD_LOGO_QUERY)
    private Boolean sysCardLogoQuery = Boolean.FALSE;
    @Role(Permission.SYS_CARD_LOGO_MODIFY)
    private Boolean sysCardLogoModify = Boolean.FALSE;
    @Role(Permission.SYS_CARD_LOGO_AUDIT)
    private Boolean sysCardLogoAudit = Boolean.FALSE;

    @Role(Permission.MODULE_SYS_CHALLENGE_VIEW)
    private Boolean moduleSysChallengeView = Boolean.FALSE;
    @Role(Permission.SYS_CHALLENGE_VIEW_QUERY)
    private Boolean sysChallengeViewQuery = Boolean.FALSE;
    @Role(Permission.SYS_CHALLENGE_VIEW_MODIFY)
    private Boolean sysChallengeViewModify = Boolean.FALSE;
    @Role(Permission.SYS_CHALLENGE_VIEW_AUDIT)
    private Boolean sysChallengeViewAudit = Boolean.FALSE;

    @Role(Permission.MODULE_SYS_CHALLENGE_SMS_MSG)
    private Boolean moduleSysChallengeSmsMsg = Boolean.FALSE;
    @Role(Permission.SYS_CHALLENGE_SMS_MSG_QUERY)
    private Boolean sysChallengeSmsMsgQuery = Boolean.FALSE;
    @Role(Permission.SYS_CHALLENGE_SMS_MSG_MODIFY)
    private Boolean sysChallengeSmsMsgModify = Boolean.FALSE;
    @Role(Permission.SYS_CHALLENGE_SMS_MSG_AUDIT)
    private Boolean sysChallengeSmsMsgAudit = Boolean.FALSE;

    @Role(Permission.MODULE_SYS_ACS_OPERATOR_ID)
    private Boolean moduleSysAcsOperatorId = Boolean.FALSE;
    @Role(Permission.SYS_ACS_OPERATOR_ID_QUERY)
    private Boolean sysAcsOperatorIdQuery = Boolean.FALSE;
    @Role(Permission.SYS_ACS_OPERATOR_ID_MODIFY)
    private Boolean sysAcsOperatorIdModify = Boolean.FALSE;
    @Role(Permission.SYS_ACS_OPERATOR_ID_AUDIT)
    private Boolean sysAcsOperatorIdAudit = Boolean.FALSE;

    @Role(Permission.MODULE_SYS_TIMEOUT)
    private Boolean moduleSysTimeout = Boolean.FALSE;
    @Role(Permission.SYS_TIMEOUT_QUERY)
    private Boolean sysTimeoutQuery = Boolean.FALSE;
    @Role(Permission.SYS_TIMEOUT_MODIFY)
    private Boolean sysTimeoutModify = Boolean.FALSE;
    @Role(Permission.SYS_TIMEOUT_AUDIT)
    private Boolean sysTimeoutAudit = Boolean.FALSE;

    @Role(Permission.MODULE_SYS_ERROR_CODE)
    private Boolean moduleSysErrorCode = Boolean.FALSE;
    @Role(Permission.SYS_ERROR_CODE_QUERY)
    private Boolean sysErrorCodeQuery = Boolean.FALSE;
    @Role(Permission.SYS_ERROR_CODE_MODIFY)
    private Boolean sysErrorCodeModify = Boolean.FALSE;
    @Role(Permission.SYS_ERROR_CODE_AUDIT)
    private Boolean sysErrorCodeAudit = Boolean.FALSE;

    @Role(Permission.MODULE_SYS_KEY)
    private Boolean moduleSysKey = Boolean.FALSE;
    @Role(Permission.SYS_KEY_QUERY)
    private Boolean sysKeyQuery = Boolean.FALSE;
    @Role(Permission.SYS_KEY_MODIFY)
    private Boolean sysKeyModify = Boolean.FALSE;
    @Role(Permission.SYS_KEY_AUDIT)
    private Boolean sysKeyAudit = Boolean.FALSE;

    @Role(Permission.MODULE_CERT)
    private Boolean moduleCert = Boolean.FALSE;
    @Role(Permission.CERT_QUERY)
    private Boolean certQuery = Boolean.FALSE;
    @Role(Permission.CERT_MODIFY)
    private Boolean certModify = Boolean.FALSE;
    @Role(Permission.MODULE_CLASSIC_RBA)
    private Boolean moduleClassicRba = Boolean.FALSE;

    @Role(Permission.MODULE_PLUGIN_ISSUER_PROPERTY)
    private Boolean modulePluginIssuerProperty = Boolean.FALSE;
    @Role(Permission.PLUGIN_ISSUER_PROPERTY_QUERY)
    private Boolean pluginIssuerPropertyQuery = Boolean.FALSE;
    @Role(Permission.PLUGIN_ISSUER_PROPERTY_MODIFY)
    private Boolean pluginIssuerPropertyModify = Boolean.FALSE;

    @Role(Permission.CLASSIC_RBA_QUERY)
    private Boolean classicRbaQuery = Boolean.FALSE;
    @Role(Permission.CLASSIC_RBA_MODIFY)
    private Boolean classicRbaModify = Boolean.FALSE;
    @Role(Permission.CLASSIC_RBA_AUDIT)
    private Boolean classicRbaAudit = Boolean.FALSE;
    @Role(Permission.CARD_AUDIT)
    private Boolean certAudit = Boolean.FALSE;
    @Role(Permission.ACCESS_MULTI_BANK)
    private Boolean accessMultiBank = Boolean.FALSE;

    @Role(Permission.MODULE_MIMA_POLICY)
    private Boolean moduleMimaPolicy = Boolean.FALSE;
    @Role(Permission.MIMA_POLICY_MODIFY)
    private Boolean mimaPolicyModify = Boolean.FALSE;
    @Role(Permission.MIMA_POLICY_QUERY)
    private Boolean mimaPolicyQuery = Boolean.FALSE;

    @Role(Permission.MODULE_ACQUIRER_BANK)
    private Boolean moduleAcquirerBank = Boolean.FALSE;
    @Role(Permission.ACQUIRER_BANK_MODIFY)
    private Boolean acquirerBankModify = Boolean.FALSE;
    @Role(Permission.ACQUIRER_BANK_QUERY)
    private Boolean acquirerBankQuery = Boolean.FALSE;

    @Role(Permission.MODULE_BANK_DATA_KEY)
    private Boolean moduleBankDataKey = Boolean.FALSE;
    @Role(Permission.BANK_DATA_KEY_QUERY)
    private Boolean bankDataKeyQuery = Boolean.FALSE;
    @Role(Permission.BANK_DATA_KEY_MODIFY)
    private Boolean bankDataKeyModify = Boolean.FALSE;

    @Role(Permission.MODULE_VELOG)
    private Boolean moduleVelog = Boolean.FALSE;
    @Role(Permission.VELOG_QUERY)
    private Boolean velogQuery = Boolean.FALSE;

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
    public static UserGroupDO valueOf(ocean.acs.models.oracle.entity.UserGroup e) {
        return UserGroupDO.builder()
          .id(e.getId())
          .issuerBankId(e.getIssuerBankId())
          .name(e.getName())
          .type(e.getType())
          .scope(e.getScope())
          .moduleCanSeePan(e.getModuleCanSeePan())
          .canSeePanQuery(e.getCanSeePanQuery())
          .moduleReport(e.getModuleReport())
          .reportQuery(e.getReportQuery())
          .moduleHealthCheck(e.getModuleHealthCheck())
          .healthCheckQuery(e.getHealthCheckQuery())
          .moduleTx(e.getModuleTx())
          .txQuery(e.getTxQuery())
          .moduleCard(e.getModuleCard())
          .cardQuery(e.getCardQuery())
          .cardModify(e.getCardModify())
          .cardAudit(e.getCardAudit())
          .moduleRiskBlackList(e.getModuleRiskBlackList())
          .blackListQuery(e.getBlackListQuery())
          .blackListModify(e.getBlackListModify())
          .blackListAudit(e.getBlackListAudit())
          .moduleRiskWhiteList(e.getModuleRiskWhiteList())
          .whiteListQuery(e.getWhiteListQuery())
          .whiteListModify(e.getWhiteListModify())
          .whiteListAudit(e.getWhiteListAudit())
          .moduleRiskControl(e.getModuleRiskControl())
          .riskControlQuery(e.getRiskControlQuery())
          .riskControlModify(e.getRiskControlModify())
          .riskControlAudit(e.getRiskControlAudit())
          .moduleUserGroup(e.getModuleUserGroup())
          .userGroupQuery(e.getUserGroupQuery())
          .userGroupModify(e.getUserGroupModify())
          .userGroupAudit(e.getUserGroupAudit())
          .moduleUserUnlock(e.getModuleUserUnlock())
          .unlockQuery(e.getUnlockQuery())
          .unlockModify(e.getUnlockModify())
          .unlockAudit(e.getUnlockAudit())
          .moduleUserAuditLog(e.getModuleUserAuditLog())
          .auditLogQuery(e.getAuditLogQuery())
          .moduleBankManage(e.getModuleBankManage())
          .bankManageQuery(e.getBankManageQuery())
          .bankManageModify(e.getBankManageModify())
          .bankManageAudit(e.getBankManageAudit())
          .moduleBankLogo(e.getModuleBankLogo())
          .bankLogoQuery(e.getBankLogoQuery())
          .bankLogoModify(e.getBankLogoModify())
          .bankLogoAudit(e.getBankLogoAudit())
          .moduleBankChannel(e.getModuleBankChannel())
          .bankChannelQuery(e.getBankChannelQuery())
          .bankChannelModify(e.getBankChannelModify())
          .bankChannelAudit(e.getBankChannelAudit())
          .moduleBankFee(e.getModuleBankFee())
          .bankFeeQuery(e.getBankFeeQuery())
          .bankFeeModify(e.getBankFeeModify())
          .bankFeeAudit(e.getBankFeeAudit())
          .moduleBankOtpSending(e.getModuleBankOtpSending())
          .bankOtpSendingQuery(e.getBankOtpSendingQuery())
          .bankOtpSendingModify(e.getBankOtpSendingModify())
          .bankOtpSendingAudit(e.getBankOtpSendingAudit())
          .moduleSysBinRange(e.getModuleSysBinRange())
          .sysBinRangeQuery(e.getSysBinRangeQuery())
          .sysBinRangeModify(e.getSysBinRangeModify())
          .sysBinRangeAudit(e.getSysBinRangeAudit())
          .moduleGeneralSetting(e.getModuleGeneralSetting())
          .generalSettingQuery(e.getGeneralSettingQuery())
          .generalSettingModify(e.getGeneralSettingModify())
          .moduleSysCardLogo(e.getModuleSysCardLogo())
          .sysCardLogoQuery(e.getSysCardLogoQuery())
          .sysCardLogoModify(e.getSysCardLogoModify())
          .sysCardLogoAudit(e.getSysCardLogoAudit())
          .moduleSysChallengeView(e.getModuleSysChallengeView())
          .sysChallengeViewQuery(e.getSysChallengeViewQuery())
          .sysChallengeViewModify(e.getSysChallengeViewModify())
          .sysChallengeViewAudit(e.getSysChallengeViewAudit())
          .moduleSysChallengeSmsMsg(e.getModuleSysChallengeSmsMsg())
          .sysChallengeSmsMsgQuery(e.getSysChallengeSmsMsgQuery())
          .sysChallengeSmsMsgModify(e.getSysChallengeSmsMsgModify())
          .sysChallengeSmsMsgAudit(e.getSysChallengeSmsMsgAudit())
          .moduleSysAcsOperatorId(e.getModuleSysAcsOperatorId())
          .sysAcsOperatorIdQuery(e.getSysAcsOperatorIdQuery())
          .sysAcsOperatorIdModify(e.getSysAcsOperatorIdModify())
          .sysAcsOperatorIdAudit(e.getSysAcsOperatorIdAudit())
          .moduleSysTimeout(e.getModuleSysTimeout())
          .sysTimeoutQuery(e.getSysTimeoutQuery())
          .sysTimeoutModify(e.getSysTimeoutModify())
          .sysTimeoutAudit(e.getSysTimeoutAudit())
          .moduleSysErrorCode(e.getModuleSysErrorCode())
          .sysErrorCodeQuery(e.getSysErrorCodeQuery())
          .sysErrorCodeModify(e.getSysErrorCodeModify())
          .sysErrorCodeAudit(e.getSysErrorCodeAudit())
          .moduleSysKey(e.getModuleSysKey())
          .sysKeyQuery(e.getSysKeyQuery())
          .sysKeyModify(e.getSysKeyModify())
          .sysKeyAudit(e.getSysKeyAudit())
          .moduleCert(e.getModuleCert())
          .certQuery(e.getCertQuery())
          .certModify(e.getCertModify())
          .moduleClassicRba(e.getModuleClassicRba())
          .classicRbaQuery(e.getClassicRbaQuery())
          .classicRbaModify(e.getClassicRbaModify())
          .classicRbaAudit(e.getClassicRbaAudit())
          .certAudit(e.getCertAudit())
          .accessMultiBank(e.getAccessMultiBank())
          .auditStatus(e.getAuditStatus())
          .creator(e.getCreator())
          .createMillis(e.getCreateMillis())
          .updater(e.getUpdater())
          .updateMillis(e.getUpdateMillis())
          .deleteFlag(e.getDeleteFlag())
          .deleter(e.getDeleter())
          .deleteMillis(e.getDeleteMillis())
          .modulePluginIssuerProperty(e.getModulePluginIssuerProperty())
          .pluginIssuerPropertyQuery(e.getPluginIssuerPropertyQuery())
          .pluginIssuerPropertyModify(e.getPluginIssuerPropertyModify())
          .moduleMimaPolicy(e.getModuleMimaPolicy())
          .mimaPolicyModify(e.getMimaPolicyModify())
          .mimaPolicyQuery(e.getMimaPolicyQuery())
          .moduleAcquirerBank(e.getModuleAcquirerBank())
          .acquirerBankQuery(e.getAcquirerBankQuery())
          .acquirerBankModify(e.getAcquirerBankModify())
          .moduleBankDataKey(e.getModuleBankDataKey())
          .bankDataKeyQuery(e.getBankDataKeyQuery())
          .bankDataKeyModify(e.getBankDataKeyModify())
          .moduleVelog(e.getModuleVelog())
          .velogQuery(e.getVelogQuery())
          .build();
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
    public static UserGroupDO valueOf(ocean.acs.models.sql_server.entity.UserGroup e) {
        return UserGroupDO.builder()
          .id(e.getId())
          .issuerBankId(e.getIssuerBankId())
          .name(e.getName())
          .type(e.getType())
          .scope(e.getScope())
          .moduleCanSeePan(e.getModuleCanSeePan())
          .canSeePanQuery(e.getCanSeePanQuery())
          .moduleReport(e.getModuleReport())
          .reportQuery(e.getReportQuery())
          .moduleHealthCheck(e.getModuleHealthCheck())
          .healthCheckQuery(e.getHealthCheckQuery())
          .moduleTx(e.getModuleTx())
          .txQuery(e.getTxQuery())
          .moduleCard(e.getModuleCard())
          .cardQuery(e.getCardQuery())
          .cardModify(e.getCardModify())
          .cardAudit(e.getCardAudit())
          .moduleRiskBlackList(e.getModuleRiskBlackList())
          .blackListQuery(e.getBlackListQuery())
          .blackListModify(e.getBlackListModify())
          .blackListAudit(e.getBlackListAudit())
          .moduleRiskWhiteList(e.getModuleRiskWhiteList())
          .whiteListQuery(e.getWhiteListQuery())
          .whiteListModify(e.getWhiteListModify())
          .whiteListAudit(e.getWhiteListAudit())
          .moduleRiskControl(e.getModuleRiskControl())
          .riskControlQuery(e.getRiskControlQuery())
          .riskControlModify(e.getRiskControlModify())
          .riskControlAudit(e.getRiskControlAudit())
          .moduleUserGroup(e.getModuleUserGroup())
          .userGroupQuery(e.getUserGroupQuery())
          .userGroupModify(e.getUserGroupModify())
          .userGroupAudit(e.getUserGroupAudit())
          .moduleUserUnlock(e.getModuleUserUnlock())
          .unlockQuery(e.getUnlockQuery())
          .unlockModify(e.getUnlockModify())
          .unlockAudit(e.getUnlockAudit())
          .moduleUserAuditLog(e.getModuleUserAuditLog())
          .auditLogQuery(e.getAuditLogQuery())
          .moduleBankManage(e.getModuleBankManage())
          .bankManageQuery(e.getBankManageQuery())
          .bankManageModify(e.getBankManageModify())
          .bankManageAudit(e.getBankManageAudit())
          .moduleBankLogo(e.getModuleBankLogo())
          .bankLogoQuery(e.getBankLogoQuery())
          .bankLogoModify(e.getBankLogoModify())
          .bankLogoAudit(e.getBankLogoAudit())
          .moduleBankChannel(e.getModuleBankChannel())
          .bankChannelQuery(e.getBankChannelQuery())
          .bankChannelModify(e.getBankChannelModify())
          .bankChannelAudit(e.getBankChannelAudit())
          .moduleBankFee(e.getModuleBankFee())
          .bankFeeQuery(e.getBankFeeQuery())
          .bankFeeModify(e.getBankFeeModify())
          .bankFeeAudit(e.getBankFeeAudit())
          .moduleBankOtpSending(e.getModuleBankOtpSending())
          .bankOtpSendingQuery(e.getBankOtpSendingQuery())
          .bankOtpSendingModify(e.getBankOtpSendingModify())
          .bankOtpSendingAudit(e.getBankOtpSendingAudit())
          .moduleSysBinRange(e.getModuleSysBinRange())
          .sysBinRangeQuery(e.getSysBinRangeQuery())
          .sysBinRangeModify(e.getSysBinRangeModify())
          .sysBinRangeAudit(e.getSysBinRangeAudit())
          .moduleGeneralSetting(e.getModuleGeneralSetting())
          .generalSettingQuery(e.getGeneralSettingQuery())
          .generalSettingModify(e.getGeneralSettingModify())
          .moduleSysCardLogo(e.getModuleSysCardLogo())
          .sysCardLogoQuery(e.getSysCardLogoQuery())
          .sysCardLogoModify(e.getSysCardLogoModify())
          .sysCardLogoAudit(e.getSysCardLogoAudit())
          .moduleSysChallengeView(e.getModuleSysChallengeView())
          .sysChallengeViewQuery(e.getSysChallengeViewQuery())
          .sysChallengeViewModify(e.getSysChallengeViewModify())
          .sysChallengeViewAudit(e.getSysChallengeViewAudit())
          .moduleSysChallengeSmsMsg(e.getModuleSysChallengeSmsMsg())
          .sysChallengeSmsMsgQuery(e.getSysChallengeSmsMsgQuery())
          .sysChallengeSmsMsgModify(e.getSysChallengeSmsMsgModify())
          .sysChallengeSmsMsgAudit(e.getSysChallengeSmsMsgAudit())
          .moduleSysAcsOperatorId(e.getModuleSysAcsOperatorId())
          .sysAcsOperatorIdQuery(e.getSysAcsOperatorIdQuery())
          .sysAcsOperatorIdModify(e.getSysAcsOperatorIdModify())
          .sysAcsOperatorIdAudit(e.getSysAcsOperatorIdAudit())
          .moduleSysTimeout(e.getModuleSysTimeout())
          .sysTimeoutQuery(e.getSysTimeoutQuery())
          .sysTimeoutModify(e.getSysTimeoutModify())
          .sysTimeoutAudit(e.getSysTimeoutAudit())
          .moduleSysErrorCode(e.getModuleSysErrorCode())
          .sysErrorCodeQuery(e.getSysErrorCodeQuery())
          .sysErrorCodeModify(e.getSysErrorCodeModify())
          .sysErrorCodeAudit(e.getSysErrorCodeAudit())
          .moduleSysKey(e.getModuleSysKey())
          .sysKeyQuery(e.getSysKeyQuery())
          .sysKeyModify(e.getSysKeyModify())
          .sysKeyAudit(e.getSysKeyAudit())
          .moduleCert(e.getModuleCert())
          .certQuery(e.getCertQuery())
          .certModify(e.getCertModify())
          .moduleClassicRba(e.getModuleClassicRba())
          .classicRbaQuery(e.getClassicRbaQuery())
          .classicRbaModify(e.getClassicRbaModify())
          .classicRbaAudit(e.getClassicRbaAudit())
          .certAudit(e.getCertAudit())
          .accessMultiBank(e.getAccessMultiBank())
          .auditStatus(e.getAuditStatus())
          .creator(e.getCreator())
          .createMillis(e.getCreateMillis())
          .updater(e.getUpdater())
          .updateMillis(e.getUpdateMillis())
          .deleteFlag(e.getDeleteFlag())
          .deleter(e.getDeleter())
          .deleteMillis(e.getDeleteMillis())
          .modulePluginIssuerProperty(e.getModulePluginIssuerProperty())
          .pluginIssuerPropertyQuery(e.getPluginIssuerPropertyQuery())
          .pluginIssuerPropertyModify(e.getPluginIssuerPropertyModify())
          .moduleMimaPolicy(e.getModuleMimaPolicy())
          .mimaPolicyModify(e.getMimaPolicyModify())
          .mimaPolicyQuery(e.getMimaPolicyQuery())
          .moduleAcquirerBank(e.getModuleAcquirerBank())
          .acquirerBankQuery(e.getAcquirerBankQuery())
          .acquirerBankModify(e.getAcquirerBankModify())
          .moduleBankDataKey(e.getModuleBankDataKey())
          .bankDataKeyQuery(e.getBankDataKeyQuery())
          .bankDataKeyModify(e.getBankDataKeyModify())
          .moduleVelog(e.getModuleVelog())
          .velogQuery(e.getVelogQuery())
          .build();
    }


    public Set<String> getPermissionSymbol() throws IllegalAccessException {
        Class<? extends UserGroupDO> clazz = this.getClass();
        Field[] fields = clazz.getDeclaredFields();

        Set<String> permissionSymbols = new HashSet<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Role.class) && (boolean) field.get(this)) {
                Role role = field.getAnnotation(Role.class);
                permissionSymbols.add(role.value().getSymbol());
            }
        }

        return permissionSymbols;
    }

    public Set<String> getPermissionDtoField()
      throws IllegalArgumentException, IllegalAccessException {
        Class<? extends UserGroupDO> clazz = this.getClass();
        Field[] fields = clazz.getDeclaredFields();

        Set<String> permissionDtoFields = new HashSet<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Role.class) && (boolean) field.get(this)) {
                Role role = field.getAnnotation(Role.class);
                permissionDtoFields.add(role.value().getPermissionDtoField());
            }
        }

        return permissionDtoFields;
    }

}
