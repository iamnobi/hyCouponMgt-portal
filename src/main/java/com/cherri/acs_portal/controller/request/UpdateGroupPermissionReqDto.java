package com.cherri.acs_portal.controller.request;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.controller.response.PermissionDto;
import com.cherri.acs_portal.dto.usermanagement.UserGroupDto;
import com.cherri.acs_portal.service.PermissionService;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.Permission;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanException;
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
public class UpdateGroupPermissionReqDto {

    @NotNull(message = "{column.notempty}")
    private Long issuerBankId;
    @NotNull(message = "{column.notempty}")
    private Long groupId;
    @NotNull(message = "{column.notempty}")
    private Boolean moduleCanSeePan;
    @NotNull(message = "{column.notempty}")
    private Boolean canSeePanQuery;
    @NotNull(message = "{column.notempty}")
    private Boolean moduleReport;
    @NotNull(message = "{column.notempty}")
    private Boolean reportQuery;
    @NotNull(message = "{column.notempty}")
    private Boolean moduleHealthCheck;
    @NotNull(message = "{column.notempty}")
    private Boolean healthCheckQuery;
    @NotNull(message = "{column.notempty}")
    private Boolean moduleTx;
    @NotNull(message = "{column.notempty}")
    private Boolean txQuery;
    @NotNull(message = "{column.notempty}")
    private Boolean moduleCard;
    @NotNull(message = "{column.notempty}")
    private Boolean cardQuery;
    @NotNull(message = "{column.notempty}")
    private Boolean cardModify;
    @NotNull(message = "{column.notempty}")
    private Boolean cardAudit;
    @NotNull(message = "{column.notempty}")
    private Boolean moduleRiskBlackList;
    @NotNull(message = "{column.notempty}")
    private Boolean blackListQuery;
    @NotNull(message = "{column.notempty}")
    private Boolean blackListModify;
    @NotNull(message = "{column.notempty}")
    private Boolean blackListAudit;
    @NotNull(message = "{column.notempty}")
    private Boolean moduleRiskWhiteList;
    @NotNull(message = "{column.notempty}")
    private Boolean whiteListQuery;
    @NotNull(message = "{column.notempty}")
    private Boolean whiteListModify;
    @NotNull(message = "{column.notempty}")
    private Boolean whiteListAudit;
    @NotNull(message = "{column.notempty}")
    private Boolean moduleRiskControl;
    @NotNull(message = "{column.notempty}")
    private Boolean riskControlQuery;
    @NotNull(message = "{column.notempty}")
    private Boolean riskControlModify;
    @NotNull(message = "{column.notempty}")
    private Boolean riskControlAudit;
    @NotNull(message = "{column.notempty}")
    private Boolean moduleUserGroup;
    @NotNull(message = "{column.notempty}")
    private Boolean userGroupQuery;
    @NotNull(message = "{column.notempty}")
    private Boolean userGroupModify;
    @NotNull(message = "{column.notempty}")
    private Boolean userGroupAudit;
    @NotNull(message = "{column.notempty}")
    private Boolean moduleUserUnlock;
    @NotNull(message = "{column.notempty}")
    private Boolean unlockQuery;
    @NotNull(message = "{column.notempty}")
    private Boolean unlockModify;
    @NotNull(message = "{column.notempty}")
    private Boolean unlockAudit;
    @NotNull(message = "{column.notempty}")
    private Boolean moduleUserAuditLog;
    @NotNull(message = "{column.notempty}")
    private Boolean auditLogQuery;
    @NotNull(message = "{column.notempty}")
    private Boolean moduleBankManage;
    @NotNull(message = "{column.notempty}")
    private Boolean bankManageQuery;
    @NotNull(message = "{column.notempty}")
    private Boolean bankManageModify;
    @NotNull(message = "{column.notempty}")
    private Boolean bankManageAudit;
    @NotNull(message = "{column.notempty}")
    private Boolean moduleBankLogo;
    @NotNull(message = "{column.notempty}")
    private Boolean bankLogoQuery;
    @NotNull(message = "{column.notempty}")
    private Boolean bankLogoModify;
    @NotNull(message = "{column.notempty}")
    private Boolean bankLogoAudit;
    @NotNull(message = "{column.notempty}")
    private Boolean moduleBankChannel;
    @NotNull(message = "{column.notempty}")
    private Boolean bankChannelQuery;
    @NotNull(message = "{column.notempty}")
    private Boolean bankChannelModify;
    @NotNull(message = "{column.notempty}")
    private Boolean bankChannelAudit;
    @NotNull(message = "{column.notempty}")
    private Boolean moduleBankFee;
    @NotNull(message = "{column.notempty}")
    private Boolean bankFeeQuery;
    @NotNull(message = "{column.notempty}")
    private Boolean bankFeeModify;
    @NotNull(message = "{column.notempty}")
    private Boolean bankFeeAudit;
    @NotNull(message = "{column.notempty}")
    private Boolean moduleBankOtpSending;
    @NotNull(message = "{column.notempty}")
    private Boolean bankOtpSendingQuery;
    @NotNull(message = "{column.notempty}")
    private Boolean bankOtpSendingModify;
    @NotNull(message = "{column.notempty}")
    private Boolean bankOtpSendingAudit;
    @NotNull(message = "{column.notempty}")
    private Boolean moduleSysBinRange;
    @NotNull(message = "{column.notempty}")
    private Boolean sysBinRangeQuery;
    @NotNull(message = "{column.notempty}")
    private Boolean sysBinRangeModify;
    @NotNull(message = "{column.notempty}")
    private Boolean sysBinRangeAudit;
    @NotNull(message = "{column.notempty}")
    private Boolean moduleSysCardLogo;
    @NotNull(message = "{column.notempty}")
    private Boolean sysCardLogoQuery;
    @NotNull(message = "{column.notempty}")
    private Boolean sysCardLogoModify;
    @NotNull(message = "{column.notempty}")
    private Boolean sysCardLogoAudit;
    @NotNull(message = "{column.notempty}")
    private Boolean moduleSysChallengeView;
    @NotNull(message = "{column.notempty}")
    private Boolean sysChallengeViewQuery;
    @NotNull(message = "{column.notempty}")
    private Boolean sysChallengeViewModify;
    @NotNull(message = "{column.notempty}")
    private Boolean sysChallengeViewAudit;
    @NotNull(message = "{column.notempty}")
    private Boolean moduleSysChallengeSmsMsg;
    @NotNull(message = "{column.notempty}")
    private Boolean sysChallengeSmsMsgQuery;
    @NotNull(message = "{column.notempty}")
    private Boolean sysChallengeSmsMsgModify;
    @NotNull(message = "{column.notempty}")
    private Boolean sysChallengeSmsMsgAudit;
    @NotNull(message = "{column.notempty}")
    private Boolean moduleSysAcsOperatorId;
    @NotNull(message = "{column.notempty}")
    private Boolean sysAcsOperatorIdQuery;
    @NotNull(message = "{column.notempty}")
    private Boolean sysAcsOperatorIdModify;
    @NotNull(message = "{column.notempty}")
    private Boolean sysAcsOperatorIdAudit;
    @NotNull(message = "{column.notempty}")
    private Boolean moduleSysTimeout;
    @NotNull(message = "{column.notempty}")
    private Boolean sysTimeoutQuery;
    @NotNull(message = "{column.notempty}")
    private Boolean sysTimeoutModify;
    @NotNull(message = "{column.notempty}")
    private Boolean sysTimeoutAudit;
    @NotNull(message = "{column.notempty}")
    private Boolean moduleSysErrorCode;
    @NotNull(message = "{column.notempty}")
    private Boolean sysErrorCodeQuery;
    @NotNull(message = "{column.notempty}")
    private Boolean sysErrorCodeModify;
    @NotNull(message = "{column.notempty}")
    private Boolean sysErrorCodeAudit;
    @NotNull(message = "{column.notempty}")
    private Boolean moduleSysKey;
    @NotNull(message = "{column.notempty}")
    private Boolean sysKeyQuery;
    @NotNull(message = "{column.notempty}")
    private Boolean sysKeyModify;
    @NotNull(message = "{column.notempty}")
    private Boolean sysKeyAudit;
    @NotNull(message = "{column.notempty}")
    private Boolean moduleCert;
    @NotNull(message = "{column.notempty}")
    private Boolean certQuery;
    @NotNull(message = "{column.notempty}")
    private Boolean certModify;
    @NotNull(message = "{column.notempty}")
    private Boolean certAudit;
    @NotNull(message = "{column.notempty}")
    private Boolean moduleClassicRba;
    @NotNull(message = "{column.notempty}")
    private Boolean classicRbaQuery;
    @NotNull(message = "{column.notempty}")
    private Boolean classicRbaModify;
    @NotNull(message = "{column.notempty}")
    private Boolean classicRbaAudit;

    @NotNull(message = "{column.notempty}")
    private Boolean modulePluginIssuerProperty;
    @NotNull(message = "{column.notempty}")
    private Boolean pluginIssuerPropertyQuery;
    @NotNull(message = "{column.notempty}")
    private Boolean pluginIssuerPropertyModify;

    @NotNull(message = "{column.notempty}")
    private Boolean moduleMimaPolicy;
    @NotNull(message = "{column.notempty}")
    private Boolean mimaPolicyQuery;
    @NotNull(message = "{column.notempty}")
    private Boolean mimaPolicyModify;

    @NotNull(message = "{column.notempty}")
    private Boolean moduleAcquirerBank;
    @NotNull(message = "{column.notempty}")
    private Boolean acquirerBankQuery;
    @NotNull(message = "{column.notempty}")
    private Boolean acquirerBankModify;

    @NotNull(message = "{column.notempty}")
    private Boolean moduleGeneralSetting;
    @NotNull(message = "{column.notempty}")
    private Boolean generalSettingQuery;
    @NotNull(message = "{column.notempty}")
    private Boolean generalSettingModify;

    @NotNull(message = "{column.notempty}")
    private Boolean moduleBankDataKey;
    @NotNull(message = "{column.notempty}")
    private Boolean bankDataKeyQuery;
    @NotNull(message = "{column.notempty}")
    private Boolean bankDataKeyModify;
    @NotNull(message = "{column.notempty}")
    private Boolean moduleVelog;
    @NotNull(message = "{column.notempty}")
    private Boolean velogQuery;

    @NotNull(message = "{column.notempty}")
    private Boolean accessMultiBank;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
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
    public void checkUpdatingPermissionRule() {
        // moduleCanSeePan
        if (!getModuleCanSeePan() && getCanSeePanQuery()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : canSeePanQuery, because moduleCanSeePan is false");
        }

        // moduleReport
        if (!getModuleReport() && getReportQuery()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : reportQuery, because moduleReport is false");
        }

        // moduleTx
        if (!getModuleTx() && getTxQuery()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : txQuery, because moduleTxis false");
        }

        // moduleCard
        if (!getModuleCard() && (getCardQuery() || getCardModify() || getCardAudit())) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : cardQuery or cardModify or cardAudit, because moduleCard is false");
        } else if ((getCardModify() || getCardAudit()) && !getCardQuery()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : cardModify or cardAudit, because cardQuery is false");
        }

        // moduleRiskBlackList
        if (!getModuleRiskBlackList() && (getBlackListQuery() || getBlackListModify()
          || getBlackListAudit())) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : blackListQuery or blackListModify or blackListAudit, because moduleRiskBlackList is false");
        } else if ((getBlackListModify() || getBlackListAudit()) && !getBlackListQuery()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : blackListModify or blackListAudit, because blackListQuery is false");
        }

        // moduleRiskWhiteList
        if (!getModuleRiskWhiteList() && (getWhiteListQuery() || getWhiteListModify()
          || getWhiteListAudit())) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : whiteListQuery or whiteListModify or whiteListAudit, because moduleRiskWhiteList is false");
        } else if ((getWhiteListModify() || getWhiteListAudit()) && !getWhiteListQuery()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : whiteListModify or whiteListAudit, because whiteListQuery is false");
        }

        // moduleRiskControl
        if (!getModuleRiskControl() && (getRiskControlQuery() || getRiskControlModify()
          || getRiskControlAudit())) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : riskControlQuery or riskControlModify or riskControlAudit, because moduleRiskControl is false");
        } else if ((getRiskControlModify() || getRiskControlAudit()) && !getRiskControlQuery()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : riskControlModify or riskControlAudit, because riskControlQuery is false");
        }

        // moduleUserGroup
        if (!getModuleUserGroup() && (getUserGroupQuery() || getUserGroupModify()
          || getUserGroupAudit())) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : userGroupQuery or userGroupModify or userGroupAudit, because moduleUserGroup is false");
        } else if ((getUserGroupModify() || getUserGroupAudit()) && !getUserGroupQuery()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : userGroupModify or userGroupAudit, because userGroupQuery is false");
        }

        // moduleUserUnlock
        if (!getModuleUserUnlock() && (getUnlockQuery() || getUnlockModify() || getUnlockAudit())) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : unlockQuery or unlockModify or unlockAudit, because moduleUserUnlock is false");
        } else if ((getUnlockModify() || getUnlockAudit()) && !getUnlockQuery()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : unlockModify or unlockAudit, because unlockQuery is false");
        }

        // moduleUserAuditLog
        if (!getModuleUserAuditLog() && getAuditLogQuery()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : AuditLog, because moduleUserAuditLog is false");
        }

        // moduleBankManage
        if (!getModuleBankManage() && (getBankManageQuery() || getBankManageModify()
          || getBankManageAudit())) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : bankManageQuery or bankManageModify or bankManageAudit, because moduleBankManage is false");
        } else if ((getBankManageModify() || getBankManageAudit()) && !getBankManageQuery()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : bankManageModify or bankManageAudit, because bankManageQuery is false");
        }

        // moduleBankLogo
        if (!getModuleBankLogo() && (getBankLogoQuery() || getBankLogoModify()
          || getBankLogoAudit())) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : bankLogoQuery or bankLogoModify or bankLogoAudit, because moduleBankLogo is false");
        } else if ((getBankLogoModify() || getBankLogoAudit()) && !getBankLogoQuery()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : bankLogoModify or bankLogoAudit, because bankLogoQuery is false");
        }

        // moduleBankChannel
        if (!getModuleBankChannel() && (getBankChannelQuery() || getBankChannelModify()
          || getBankChannelAudit())) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : bankChannelQuery or bankChannelModify or bankChannelAudit, because moduleBankChannel is false");
        } else if ((getBankChannelModify() || getBankChannelAudit()) && !getBankChannelQuery()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : bankChannelModify or bankChannelAudit, because bankChannelQuery is false");
        }

        // moduleBankFee
        if (!getModuleBankFee() && (getBankFeeQuery() || getBankFeeModify() || getBankFeeAudit())) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : bankFeeQuery or bankFeeModify or bankFeeAudit, because moduleBankFee is false");
        } else if ((getBankFeeModify() || getBankFeeAudit()) && !getBankFeeQuery()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : bankFeeModify or bankFeeAudit, because bankFeeQuery is false");
        }

        //moduleBankOtpSending
        if (!getModuleBankOtpSending() && (getBankOtpSendingQuery() || getBankOtpSendingModify()
          || getBankOtpSendingAudit())) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : bankOtpSendingQuery or bankOtpSendingModify or bankOtpSendingAudit, because moduleBankOtpSending is false");
        } else if ((getBankOtpSendingModify() || getBankOtpSendingAudit())
          && !getBankOtpSendingQuery()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : bankOtpSendingModify or bankOtpSendingAudit, because bankOtpSendingQuery is false");
        }

        // moduleSysBinRange
        if (!getModuleSysBinRange() && (getSysBinRangeQuery() || getSysBinRangeModify()
          || getSysBinRangeAudit())) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : sysBinRangeQuery or sysBinRangeModify or sysBinRangeAudit, because moduleSysBinRange is false");
        } else if ((getSysBinRangeModify() || getSysBinRangeAudit()) && !getSysBinRangeQuery()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : sysBinRangeModify or sysBinRangeAudit, because sysBinRangeQuery is false");
        }

        // moduleSysCardLogo
        if (!getModuleSysCardLogo() && (getSysCardLogoQuery() || getSysCardLogoModify()
          || getSysCardLogoAudit())) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : sysCardLogoQuery or sysCardLogoModify or sysCardLogoAudit, because moduleSysCardLogo is false");
        } else if ((getSysCardLogoModify() || getSysCardLogoAudit()) && !getSysCardLogoQuery()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : sysCardLogoModify or sysCardLogoAudit, because sysCardLogoQuery is false");
        }

        // moduleSysChallengeView
        if (!getModuleSysChallengeView() && (getSysChallengeViewQuery()
          || getSysChallengeViewModify() || getSysChallengeViewAudit())) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : sysChallengeViewQuery or sysChallengeViewModify or sysChallengeViewAudit, because moduleSysChallengeView is false");
        } else if ((getSysChallengeViewModify() || getSysChallengeViewAudit())
          && !getSysChallengeViewQuery()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : sysChallengeViewModify or sysChallengeViewAudit, because sysChallengeViewQuery is false");
        }

        // moduleSysChallengeSmsMsg
        if (!getModuleSysChallengeSmsMsg() && (getSysChallengeSmsMsgQuery()
          || getSysChallengeSmsMsgModify() || getSysChallengeSmsMsgAudit())) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : sysChallengeSmsMsgQuery or sysChallengeSmsMsgModify or sysChallengeSmsMsgAudit, because moduleSysChallengeSmsMsg is false");
        } else if ((getSysChallengeSmsMsgModify() || getSysChallengeSmsMsgAudit())
          && !getSysChallengeSmsMsgQuery()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : sysChallengeSmsMsgModify or sysChallengeSmsMsgAudit, because sysChallengeSmsMsgQuery is false");
        }

        // moduleSysAcsOperatorId
        if (!getModuleSysAcsOperatorId() && (getSysAcsOperatorIdQuery()
          || getSysAcsOperatorIdModify() || getSysAcsOperatorIdAudit())) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : sysAcsOperatorIdQuery or sysAcsOperatorIdModify or sysAcsOperatorIdAudit, because moduleSysAcsOperatorId is false");
        } else if ((getSysAcsOperatorIdModify() || getSysAcsOperatorIdAudit())
          && !getSysAcsOperatorIdQuery()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : sysAcsOperatorIdModify or sysAcsOperatorIdAudit, because sysAcsOperatorIdQuery is false");
        }

        // moduleSysTimeout
        if (!getModuleSysTimeout() && (getSysTimeoutQuery() || getSysTimeoutModify()
          || getSysTimeoutAudit())) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : sysTimeoutQuery or sysTimeoutModify or sysTimeoutAudit, because moduleSysTimeout is false");
        } else if ((getSysTimeoutModify() || getSysTimeoutAudit()) && !getSysTimeoutQuery()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : sysTimeoutModify or sysTimeoutAudit, because sysTimeoutQuery is false");
        }

        // moduleSysErrorCode
        if (!getModuleSysErrorCode() && (getSysErrorCodeQuery() || getSysErrorCodeModify()
          || getSysErrorCodeAudit())) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : sysErrorCodeQuery or sysErrorCodeModify or sysErrorCodeAudit, because moduleSysErrorCode is false");
        } else if ((getSysErrorCodeModify() || getSysErrorCodeAudit()) && !getSysErrorCodeQuery()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : sysErrorCodeModify or sysErrorCodeAudit, because sysErrorCodeQuery is false");
        }

        // moduleSysKey
        if (!getModuleSysKey() && (getSysKeyQuery() || getSysKeyModify() || getSysKeyAudit())) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : sysKeyQuery or sysKeyModify or sysKeyAudit, because moduleSysKey is false");
        } else if ((getSysKeyModify() || getSysKeyAudit()) && !getSysKeyQuery()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : sysKeyModify or sysKeyAudit, because sysKeyQuery is false");
        }

        // moduleCert
        if (!getModuleCert() && (getCertQuery() || getCertModify() || getCertAudit())) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : certQuery or certModify or certAudit, because moduleCert is false");
        } else if ((getCertModify() || getCertAudit()) && !getCertQuery()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : certModify or certAudit, because certQuery is false");
        }

        // moduleClassicRba
        if (!getModuleClassicRba() && (getClassicRbaQuery() || getClassicRbaModify()
          || getClassicRbaAudit())) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : classicRbaQuery or classicRbaModify or classicRbaAudit, because moduleClassicRba is false");
        } else if ((getClassicRbaModify() || getClassicRbaAudit()) && !getClassicRbaQuery()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : classicRbaModify or classicRbaAudit, because classicRbaQuery is false");
        }
        // modulePluginIssuerProperty
        if (!getModulePluginIssuerProperty() && (getPluginIssuerPropertyQuery()
          || getPluginIssuerPropertyModify())) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : pluginIssuerPropertyQuery or pluginIssuerPropertyModify, because modulePluginIssuerProperty is false");
        } else if (getPluginIssuerPropertyModify() && !getPluginIssuerPropertyQuery()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : pluginIssuerPropertyModify, because pluginIssuerPropertyQuery is false");
        }

        /* Mima Policy */
        if (!getModuleMimaPolicy() && (getMimaPolicyModify() || getMimaPolicyQuery())) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : MimaPolicyQuery or MimaPolicyModify, because ModuleMimaPolicy is false");
        } else if (getMimaPolicyModify() && !getMimaPolicyQuery()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : MimaPolicyModify, because MimaPolicyQuery is false");
        }

        /* moduleAcquirerBank */
        if (!getModuleAcquirerBank() && (getAcquirerBankModify() || getAcquirerBankQuery())) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
                "Illegal argument : AcquirerBankQuery or AcquirerBankModify, because ModuleAcquirerBank is false");
        } else if (getAcquirerBankModify() && !getAcquirerBankQuery()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
                "Illegal argument : AcquirerBankModify, because AcquirerBankQuery is false");
        }

        /* moduleGeneralSetting */
        if (!getModuleGeneralSetting() && (getGeneralSettingModify() || getGeneralSettingQuery())) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
                    "Illegal argument : GeneralSettingQuery or GeneralSettingModify, because ModuleGeneralSetting is false");
        } else if (getGeneralSettingModify() && !getGeneralSettingQuery()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
                    "Illegal argument : GeneralSettingModify, because GeneralSettingQuery is false");
        }

        /* Bank Data Key */
        if (!getModuleBankDataKey() && (getBankDataKeyQuery() || getBankDataKeyModify())) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : BankDataKeyQuery or BankDataKeyModify, because ModuleBankDataKey is false");
        } else if (getBankDataKeyModify() && !getBankDataKeyQuery()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
              "Illegal argument : BankDataKeyModify, because BankDataKeyQuery is false");
        }

        /* VELog */
        if (!getModuleVelog() && getVelogQuery()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT,
                "Illegal argument : velogQuery, because moduleVelog is false");
        }
    }


}
