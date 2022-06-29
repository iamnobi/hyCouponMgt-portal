package ocean.acs.commons.enumerator;

import lombok.Getter;

/**
 * @author *
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
public enum Permission {

    /* 顯示明碼卡號 -------------------------------------------------------------------------------*/
    MODULE_CAN_SEE_PAN("MODULE_CAN_SEE_PAN", "moduleCanSeePan"),
    CAN_SEE_PAN_QUERY("CAN_SEE_PAN_QUERY", "canSeePanQuery"),

    /* 報表 -------------------------------------------------------------------------------------*/
    MODULE_REPORT("MODULE_REPORT", "moduleReport"),
    REPORT_QUERY("RT_QUERY", "reportQuery"),

    MODULE_HEALTH_CHECK("MODULE_HEALTH_CHECK", "moduleHealthCheck"),
    HEALTH_CHECK_QUERY("HEALTH_CHECK_QUERY", "healthCheckQuery"),

    /* 交易 -------------------------------------------------------------------------------------*/
    MODULE_TX("MODULE_TX", "moduleTx"),
    TX_QUERY("TX_QUERY", "txQuery"),
    /* 交易紀錄查詢 - VELOG */
    MODULE_VELOG("MODULE_VELOG", "moduleVelog"),
    VELOG_QUERY("VELOG_QUERY", "velogQuery"),

    /* 持卡人管理 -------------------------------------------------------------------------------*/
    MODULE_CARD("MODULE_CARD", "moduleCard"),
    CARD_QUERY("CARD_QUERY", "cardQuery"),
    CARD_MODIFY("CARD_MODIFY", "cardModify"),
    CARD_AUDIT("CARD_AUDIT", "cardAudit"),

    /* 風險設定 - 黑名單 ------------------------------------------------------------------------*/
    MODULE_RISK_BLACK_LIST("MODULE_RISK_BLACK_LIST", "moduleRiskBlackList"),
    BLACK_LIST_QUERY("BLACK_LIST_QUERY", "blackListQuery"),
    BLACK_LIST_MODIFY("BLACK_LIST_MODIFY", "blackListModify"),
    BLACK_LIST_AUDIT("BLACK_LIST_AUDIT", "blackListAudit"),
    /* 風險設定 - 白名單 */
    MODULE_RISK_WHITE_LIST("MODULE_RISK_WHITE_LIST", "moduleRiskWhiteList"),
    WHITE_LIST_QUERY("WHITE_LIST_QUERY", "whiteListQuery"),
    WHITE_LIST_MODIFY("WHITE_LIST_MODIFY", "whiteListModify"),
    WHITE_LIST_AUDIT("WHITE_LIST_AUDIT", "whiteListAudit"),
    /* 風險設定 - 風控設定 */
    MODULE_RISK_CONTROL("MODULE_RISK_CONTROL", "moduleRiskControl"),
    RISK_CONTROL_QUERY("RISK_CONTROL_QUERY", "riskControlQuery"),
    RISK_CONTROL_MODIFY("RISK_CONTROL_MODIFY", "riskControlModify"),
    RISK_CONTROL_AUDIT("RISK_CONTROL_AUDIT", "riskControlAudit"),
    /* 風控設定 - 經典風控模組設定 */
    MODULE_CLASSIC_RBA("MODULE_CLASSIC_RBA", "moduleClassicRba"),
    CLASSIC_RBA_QUERY("CLASSIC_RBA_QUERY", "classicRbaQuery"),
    CLASSIC_RBA_MODIFY("CLASSIC_RBA_MODIFY", "classicRbaModify"),
    CLASSIC_RBA_AUDIT("CLASSIC_RBA_AUDIT", "classicRbaAudit"),

    /* 人員管理 - 群組權限 ----------------------------------------------------------------------*/
    MODULE_USER_GROUP("MODULE_USER_GROUP", "moduleUserGroup"),
    USER_GROUP_QUERY("USER_GROUP_QUERY", "userGroupQuery"),
    USER_GROUP_MODIFY("USER_GROUP_MODIFY", "userGroupModify"),
    USER_GROUP_AUDIT("USER_GROUP_AUDIT", "userGroupAudit"),
    /* 人員管理 - 使用者解鎖 */
    MODULE_USER_UNLOCK("MODULE_USER_UNLOCK", "moduleUserUnlock"),
    UNLOCK_QUERY("UNLOCK_QUERY", "unlockQuery"),
    UNLOCK_MODIFY("UNLOCK_MODIFY", "unlockModify"),
    UNLOCK_AUDIT("UNLOCK_AUDIT", "unlockAudit"),
    /* 人員管理 - 操作紀錄 */
    MODULE_USER_AUDIT_LOG("MODULE_USER_AUDIT_LOG", "moduleUserAuditLog"),
    AUDIT_LOG_QUERY("AUDIT_LOG_QUERY", "auditLogQuery"),
    /* 人員管理 - 密碼原則管理 */
    MODULE_MIMA_POLICY("MODULE_MIMA_POLICY", "moduleMimaPolicy"),
    MIMA_POLICY_QUERY("MIMA_POLICY_QUERY", "mimaPolicyQuery"),
    MIMA_POLICY_MODIFY("MIMA_POLICY_MODIFY", "mimaPolicyModify"),

    /* 銀行管理 - 會員銀行管理 ------------------------------------------------------------------*/
    MODULE_BANK_MANAGE("MODULE_BANK_MANAGE", "moduleBankManage"),
    BANK_MANAGE_QUERY("BANK_MANAGE_QUERY", "bankManageQuery"),
    BANK_MANAGE_MODIFY("BANK_MANAGE_MODIFY", "bankManageModify"),
    BANK_MANAGE_AUDIT("BANK_MANAGE_AUDIT", "bankManageAudit"),
    /* 銀行管理 - 商標設定 */
    MODULE_BANK_LOGO("MODULE_BANK_LOGO", "moduleBankLogo"),
    BANK_LOGO_QUERY("BANK_LOGO_QUERY", "bankLogoQuery"),
    BANK_LOGO_MODIFY("BANK_LOGO_MODIFY", "bankLogoModify"),
    BANK_LOGO_AUDIT("BANK_LOGO_AUDIT", "bankLogoAudit"),
    /* 銀行管理 - 交易管道設定 */
    MODULE_BANK_CHANNEL("MODULE_BANK_CHANNEL", "moduleBankChannel"),
    BANK_CHANNEL_QUERY("BANK_CHANNEL_QUERY", "bankChannelQuery"),
    BANK_CHANNEL_MODIFY("BANK_CHANNEL_MODIFY", "bankChannelModify"),
    BANK_CHANNEL_AUDIT("BANK_CHANNEL_AUDIT", "bankChannelAudit"),
    /* 銀行管理 - 手續費設定 */
    MODULE_BANK_FEE("MODULE_BANK_FEE", "moduleBankFee"),
    BANK_FEE_QUERY("BANK_FEE_QUERY", "bankFeeQuery"),
    BANK_FEE_MODIFY("BANK_FEE_MODIFY", "bankFeeModify"),
    BANK_FEE_AUDIT("BANK_FEE_AUDIT", "bankFeeAudit"),
    /* 銀行管理 - OTP設定 */
    MODULE_BANK_OTP_SENDING("MODULE_BANK_OTP_SENDING", "moduleBankOtpSending"),
    BANK_OTP_SENDING_QUERY("BANK_OTP_SENDING_QUERY", "bankOtpSendingQuery"),
    BANK_OTP_SENDING_MODIFY("BANK_OTP_SENDING_MODIFY", "bankOtpSendingModify"),
    BANK_OTP_SENDING_AUDIT("BANK_OTP_SENDING_AUDIT", "bankOtpSendingAudit"),

    /* 系統設定 - BinRange設定 ------------------------------------------------------------------*/
    MODULE_SYS_BIN_RANGE("MODULE_SYS_BIN_RANGE", "moduleSysBinRange"),
    SYS_BIN_RANGE_QUERY("SYS_BIN_RANGE_QUERY", "sysBinRangeQuery"),
    SYS_BIN_RANGE_MODIFY("SYS_BIN_RANGE_MODIFY", "sysBinRangeModify"),
    SYS_BIN_RANGE_AUDIT("SYS_BIN_RANGE_AUDIT", "sysBinRangeAudit"),

    /* 系統設定 - 一般設定 */
    MODULE_GENERAL_SETTING("MODULE_GENERAL_SETTING", "moduleGeneralSetting"),
    GENERAL_SETTING_QUERY("GENERAL_SETTING_QUERY", "generalSettingQuery"),
    GENERAL_SETTING_MODIFY("GENERAL_SETTING_MODIFY", "generalSettingModify"),

    /* 系統設定 - 卡組織商標設定 */
    MODULE_SYS_CARD_LOGO("MODULE_SYS_CARD_LOGO", "moduleSysCardLogo"),
    SYS_CARD_LOGO_QUERY("SYS_CARD_LOGO_QUERY", "sysCardLogoQuery"),
    SYS_CARD_LOGO_MODIFY("SYS_CARD_LOGO_MODIFY", "sysCardLogoModify"),
    SYS_CARD_LOGO_AUDIT("SYS_CARD_LOGO_AUDIT", "sysCardLogoAudit"),
    /* 系統設定 - 驗證畫面設定 */
    MODULE_SYS_CHALLENGE_VIEW("MODULE_SYS_CHALLENGE_VIEW", "moduleSysChallengeView"),
    SYS_CHALLENGE_VIEW_QUERY("SYS_CHALLENGE_VIEW_QUERY", "sysChallengeViewQuery"),
    SYS_CHALLENGE_VIEW_MODIFY("SYS_CHALLENGE_VIEW_MODIFY", "sysChallengeViewModify"),
    SYS_CHALLENGE_VIEW_AUDIT("SYS_CHALLENGE_VIEW_AUDIT", "sysChallengeViewAudit"),
    /* 系統設定 - 簡訊驗證模板設定 */
    MODULE_SYS_CHALLENGE_SMS_MSG("MODULE_SYS_CHALLENGE_SMS_MSG", "moduleSysChallengeSmsMsg"),
    SYS_CHALLENGE_SMS_MSG_QUERY("SYS_CHALLENGE_SMS_MSG_QUERY", "sysChallengeSmsMsgQuery"),
    SYS_CHALLENGE_SMS_MSG_MODIFY("SYS_CHALLENGE_SMS_MSG_MODIFY", "sysChallengeSmsMsgModify"),
    SYS_CHALLENGE_SMS_MSG_AUDIT("SYS_CHALLENGE_SMS_MSG_AUDIT", "sysChallengeSmsMsgAudit"),
    /* 系統設定 - ACS 操作者 ID */
    MODULE_SYS_ACS_OPERATOR_ID("MODULE_SYS_ACS_OPERATOR_ID", "moduleSysAcsOperatorId"),
    SYS_ACS_OPERATOR_ID_QUERY("SYS_ACS_OPERATOR_ID_QUERY", "sysAcsOperatorIdQuery"),
    SYS_ACS_OPERATOR_ID_MODIFY("SYS_ACS_OPERATOR_ID_MODIFY", "sysAcsOperatorIdModify"),
    SYS_ACS_OPERATOR_ID_AUDIT("SYS_ACS_OPERATOR_ID_AUDIT", "sysAcsOperatorIdAudit"),
    /* 系統設定 - 連線逾時設定 */
    MODULE_SYS_TIMEOUT("MODULE_SYS_TIMEOUT", "moduleSysTimeout"),
    SYS_TIMEOUT_QUERY("SYS_TIMEOUT_QUERY", "sysTimeoutQuery"),
    SYS_TIMEOUT_MODIFY("SYS_TIMEOUT_MODIFY", "sysTimeoutModify"),
    SYS_TIMEOUT_AUDIT("SYS_TIMEOUT_AUDIT", "sysTimeoutAudit"),
    /* 系統設定 - 錯誤代碼設定 */
    MODULE_SYS_ERROR_CODE("MODULE_SYS_ERROR_CODE", "moduleSysErrorCode"),
    SYS_ERROR_CODE_QUERY("SYS_ERROR_CODE_QUERY", "sysErrorCodeQuery"),
    SYS_ERROR_CODE_MODIFY("SYS_ERROR_CODE_MODIFY", "sysErrorCodeModify"),
    SYS_ERROR_CODE_AUDIT("SYS_ERROR_CODE_AUDIT", "sysErrorCodeAudit"),
    /* 系統設定 - 密鑰管理 */
    MODULE_SYS_KEY("MODULE_SYS_KEY", "moduleSysKey"),
    SYS_KEY_QUERY("SYS_KEY_QUERY", "sysKeyQuery"),
    SYS_KEY_MODIFY("SYS_KEY_MODIFY", "sysKeyModify"),
    SYS_KEY_AUDIT("SYS_KEY_AUDIT", "sysKeyAudit"),
    /* 系統設定 - 資料金鑰管理 */
    MODULE_BANK_DATA_KEY("MODULE_BANK_DATA_KEY", "moduleBankDataKey"),
    BANK_DATA_KEY_QUERY("BANK_DATA_KEY_QUERY", "bankDataKeyQuery"),
    BANK_DATA_KEY_MODIFY("BANK_DATA_KEY_MODIFY", "bankDataKeyModify"),
    /* 系統設定 - Plugin執行屬性設定 */
    MODULE_PLUGIN_ISSUER_PROPERTY("MODULE_PLUGIN_ISSUER_PROPERTY", "modulePluginIssuerProperty"),
    PLUGIN_ISSUER_PROPERTY_QUERY("PLUGIN_ISSUER_PROPERTY_QUERY", "pluginIssuerPropertyQuery"),
    PLUGIN_ISSUER_PROPERTY_MODIFY("PLUGIN_ISSUER_PROPERTY_MODIFY", "pluginIssuerPropertyModify"),

    /* 憑證管理 ---------------------------------------------------------------------------------*/
    MODULE_CERT("MODULE_CERT", "moduleCert"),
    CERT_QUERY("CERT_QUERY", "certQuery"),
    CERT_MODIFY("CERT_MODIFY", "certModify"),
    CERT_AUDIT("CERT_AUDIT", "certAudit"),

    /* Acquirer Bank ---------------------------------------------------------------------------------*/
    MODULE_ACQUIRER_BANK("MODULE_ACQUIRER_BANK", "moduleAcquirerBank"),
    ACQUIRER_BANK_QUERY("ACQUIRER_BANK_QUERY", "acquirerBankQuery"),
    ACQUIRER_BANK_MODIFY("ACQUIRER_BANK_MODIFY", "acquirerBankModify"),

    /* OTHER ------------------------------------------------------------------------------------*/
    ACCESS_MULTI_BANK("ACCESS_MULTI_BANK", "accessMultiBank"),
    UNKNOWN("UNKNOWN", "<notExist>");


    @Getter
    private String symbol;
    @Getter
    private String permissionDtoField;

    Permission(String symbol, String field) {
        this.symbol = symbol;
        this.permissionDtoField = field;
    }
}
