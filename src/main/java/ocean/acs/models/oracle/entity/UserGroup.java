package ocean.acs.models.oracle.entity;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ocean.acs.commons.annotation.Role;
import ocean.acs.commons.enumerator.Permission;
import ocean.acs.commons.enumerator.UserGroupScope;
import ocean.acs.commons.enumerator.UserGroupType;
import ocean.acs.models.data_object.entity.UserGroupDO;
import ocean.acs.models.data_object.portal.IssuerBankRelativeData;
import ocean.acs.models.entity.DBKey;
import ocean.acs.models.entity.OperatorInfo;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

/**
 * 如果有修改權限相關請記得同步更改以下Class及{@Table user_group}
 * 1. {@link UserGroup} Field
 * 2. {@link UserGroup} All args constructor
 * 3. {@link UserGroup#valueOf(UserGroupDO)}
 * 4. {@link ocean.acs.models.sql_server.entity.UserGroup} Field
 * 5. {@link ocean.acs.models.sql_server.entity.UserGroup} All args constructor
 * 6. {@link ocean.acs.models.sql_server.entity.UserGroup#valueOf(UserGroupDO)
 * 7. {@link DBKey}}
 * 8. {@link UserGroupDO}
 * 9. {@link UserGroupDO#valueOf(UserGroup)}
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
@DynamicUpdate
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = DBKey.TABLE_USER_GROUP)
public class UserGroup extends OperatorInfo implements IssuerBankRelativeData {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "user_group_seq_generator",
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
        @org.hibernate.annotations.Parameter(name = "sequence_name",
          value = "USER_GROUP_ID_SEQ"),
        @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_group_seq_generator")
    @Column(name = DBKey.COL_USER_GROUP_ID)
    private Long id;

    @NotNull
    @Column(name = DBKey.COL_USER_GROUP_ISSUER_BANK_ID)
    private Long issuerBankId;

    /** 群組名稱 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_NAME)
    private String name;

    /** 群組類型, SYSTEM:系統|CUSTOMIZED自訂 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_TYPE)
    @Enumerated(EnumType.STRING)
    private UserGroupType type;

    /** 群組範圍, ORG:組織|BANK:銀行 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_SCOPE)
    @Enumerated(EnumType.STRING)
    private UserGroupScope scope;

    /** 顯示明碼卡號 */
    @NonNull
    @Column(name = "MODULE_CAN_SEE_PAN")
    @Role(Permission.MODULE_CAN_SEE_PAN)
    private Boolean moduleCanSeePan = Boolean.FALSE;

    /** 顯示明碼卡號 */
    @NonNull
    @Column(name = "CAN_SEE_PAN_QUERY")
    @Role(Permission.CAN_SEE_PAN_QUERY)
    private Boolean canSeePanQuery = Boolean.FALSE;

    /** 報表 - 報表模組 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_MODULE_REPORT)
    @Role(Permission.MODULE_REPORT)
    private Boolean moduleReport = Boolean.FALSE;

    /** 報表 - 報表查詢 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_RT_QUERY)
    @Role(Permission.REPORT_QUERY)
    private Boolean reportQuery = Boolean.FALSE;

    /** 系統監控 - 系統監控模組 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_MODULE_HEALTH_CHECK)
    @Role(Permission.MODULE_HEALTH_CHECK)
    private Boolean moduleHealthCheck = Boolean.FALSE;

    /** 報表 - 系統監控 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_HEALTH_CHECK_QUERY)
    @Role(Permission.HEALTH_CHECK_QUERY)
    private Boolean healthCheckQuery = Boolean.FALSE;

    /** 交易 - 交易紀錄模組 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_MODULE_TX)
    @Role(Permission.MODULE_TX)
    private Boolean moduleTx = Boolean.FALSE;

    /** 交易 - 交易紀錄查詢 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_TX_QUERY)
    @Role(Permission.TX_QUERY)
    private Boolean txQuery = Boolean.FALSE;

    /** 持卡人/卡片管理 - 持卡人/卡片管理模組 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_MODULE_CARD)
    @Role(Permission.MODULE_CARD)
    private Boolean moduleCard = Boolean.FALSE;

    /** 持卡人/卡片管理 - 持卡人查詢 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_CARD_QUERY)
    @Role(Permission.CARD_QUERY)
    private Boolean cardQuery = Boolean.FALSE;

    /** 持卡人/卡片管理 - 持卡人編輯 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_CARD_MODIFY)
    @Role(Permission.CARD_MODIFY)
    private Boolean cardModify = Boolean.FALSE;

    /** 持卡人/卡片管理 - 持卡人管理稽核 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_CARD_AUDIT)
    @Role(Permission.CARD_AUDIT)
    private Boolean cardAudit = Boolean.FALSE;

    /** 風險管理 - 黑名單管理模組 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_MODULE_RISK_BLACK_LIST)
    @Role(Permission.MODULE_RISK_BLACK_LIST)
    private Boolean moduleRiskBlackList = Boolean.FALSE;

    /** 風險管理 - 黑名單查詢 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_BLACK_LIST_QUERY)
    @Role(Permission.BLACK_LIST_QUERY)
    private Boolean blackListQuery = Boolean.FALSE;

    /** 風險管理 - 黑名單編輯 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_BLACK_LIST_MODIFY)
    @Role(Permission.BLACK_LIST_MODIFY)
    private Boolean blackListModify = Boolean.FALSE;

    /** 風險管理 - 黑名單管理稽核 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_BLACK_LIST_AUDIT)
    @Role(Permission.BLACK_LIST_AUDIT)
    private Boolean blackListAudit = Boolean.FALSE;

    /** 風險管理 - 白名單管理模組 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_MODULE_RISK_WHITE_LIST)
    @Role(Permission.MODULE_RISK_WHITE_LIST)
    private Boolean moduleRiskWhiteList = Boolean.FALSE;

    /** 風險管理 - 白名單查詢 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_WHITE_LIST_QUERY)
    @Role(Permission.WHITE_LIST_QUERY)
    private Boolean whiteListQuery = Boolean.FALSE;

    /** 風險管理 - 白名單編輯 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_WHITE_LIST_MODIFY)
    @Role(Permission.WHITE_LIST_MODIFY)
    private Boolean whiteListModify = Boolean.FALSE;

    /** 風險管理 - 白名單管理稽核 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_WHITE_LIST_AUDIT)
    @Role(Permission.WHITE_LIST_AUDIT)
    private Boolean whiteListAudit = Boolean.FALSE;

    /** 風險管理 - 風險設定模組 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_MODULE_RISK_CONTROL)
    @Role(Permission.MODULE_RISK_CONTROL)
    private Boolean moduleRiskControl = Boolean.FALSE;

    /** 風險管理 - 風險設定查詢 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_RISK_CONTROL_QUERY)
    @Role(Permission.RISK_CONTROL_QUERY)
    private Boolean riskControlQuery = Boolean.FALSE;

    /** 風險管理 - 風險設定編輯 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_RISK_CONTROL_MODIFY)
    @Role(Permission.RISK_CONTROL_MODIFY)
    private Boolean riskControlModify = Boolean.FALSE;

    /** 風險管理 - 風險設定稽核 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_RISK_CONTROL_AUDIT)
    @Role(Permission.RISK_CONTROL_AUDIT)
    private Boolean riskControlAudit = Boolean.FALSE;

    /** 人員管理 - 群組管理模組 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_MODULE_USER_GROUP)
    @Role(Permission.MODULE_USER_GROUP)
    private Boolean moduleUserGroup = Boolean.FALSE;

    /** 人員管理 - 群組查詢 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_USER_GROUP_QUERY)
    @Role(Permission.USER_GROUP_QUERY)
    private Boolean userGroupQuery = Boolean.FALSE;

    /** 人員管理 - 群組編輯 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_USER_GROUP_MODIFY)
    @Role(Permission.USER_GROUP_MODIFY)
    private Boolean userGroupModify = Boolean.FALSE;

    /** 人員管理 - 群組管理稽核 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_USER_GROUP_AUDIT)
    @Role(Permission.USER_GROUP_AUDIT)
    private Boolean userGroupAudit = Boolean.FALSE;

    /** 人員管理 - 使用者解鎖模組 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_MODULE_USER_UNLOCK)
    @Role(Permission.MODULE_USER_UNLOCK)
    private Boolean moduleUserUnlock = Boolean.FALSE;

    /** 人員管理 - 解鎖使用者查詢功能 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_UNLOCK_QUERY)
    @Role(Permission.UNLOCK_QUERY)
    private Boolean unlockQuery = Boolean.FALSE;

    /** 人員管理 - 解鎖使用者編輯功能 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_UNLOCK_MODIFY)
    @Role(Permission.UNLOCK_MODIFY)
    private Boolean unlockModify = Boolean.FALSE;

    /** 人員管理 - 解鎖使用者稽核功能 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_UNLOCK_AUDIT)
    @Role(Permission.UNLOCK_AUDIT)
    private Boolean unlockAudit = Boolean.FALSE;

    /** 人員管理 - 操作記錄模組 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_MODULE_USER_AUDIT_LOG)
    @Role(Permission.MODULE_USER_AUDIT_LOG)
    private Boolean moduleUserAuditLog = Boolean.FALSE;

    /** 人員管理 - 操作記錄查詢 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_AUDIT_LOG_QUERY)
    @Role(Permission.AUDIT_LOG_QUERY)
    private Boolean auditLogQuery = Boolean.FALSE;

    /** 會員銀行 - 會員銀行管理模組 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_MODULE_BANK_MANAGE)
    @Role(Permission.MODULE_BANK_MANAGE)
    private Boolean moduleBankManage = Boolean.FALSE;

    /** 會員銀行 - 會員銀行查詢 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_BANK_MANAGE_QUERY)
    @Role(Permission.BANK_MANAGE_QUERY)
    private Boolean bankManageQuery = Boolean.FALSE;

    /** 會員銀行 - 會員銀行編輯 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_BANK_MANAGE_MODIFY)
    @Role(Permission.BANK_MANAGE_MODIFY)
    private Boolean bankManageModify = Boolean.FALSE;

    /** 會員銀行 - 會員銀行管理稽核 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_BANK_MANAGE_AUDIT)
    @Role(Permission.BANK_MANAGE_AUDIT)
    private Boolean bankManageAudit = Boolean.FALSE;

    /** 會員銀行 - 商標設定模組 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_MODULE_BANK_LOGO)
    @Role(Permission.MODULE_BANK_LOGO)
    private Boolean moduleBankLogo = Boolean.FALSE;

    /** 會員銀行 - 商標查詢 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_BANK_LOGO_QUERY)
    @Role(Permission.BANK_LOGO_QUERY)
    private Boolean bankLogoQuery = Boolean.FALSE;

    /** 會員銀行 - 商標編輯 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_BANK_LOGO_MODIFY)
    @Role(Permission.BANK_LOGO_MODIFY)
    private Boolean bankLogoModify = Boolean.FALSE;

    /** 會員銀行 - 商標設定稽核 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_BANK_LOGO_AUDIT)
    @Role(Permission.BANK_LOGO_AUDIT)
    private Boolean bankLogoAudit = Boolean.FALSE;

    /** 會員銀行 - 交易管道設定模組 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_MODULE_BANK_CHANNEL)
    @Role(Permission.MODULE_BANK_CHANNEL)
    private Boolean moduleBankChannel = Boolean.FALSE;

    /** 會員銀行 - 交易管道查詢 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_BANK_CHANNEL_QUERY)
    @Role(Permission.BANK_CHANNEL_QUERY)
    private Boolean bankChannelQuery = Boolean.FALSE;

    /** 會員銀行 - 交易管道編輯 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_BANK_CHANNEL_MODIFY)
    @Role(Permission.BANK_CHANNEL_MODIFY)
    private Boolean bankChannelModify = Boolean.FALSE;

    /** 會員銀行 - 交易管道設定稽核 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_BANK_CHANNEL_AUDIT)
    @Role(Permission.BANK_CHANNEL_AUDIT)
    private Boolean bankChannelAudit = Boolean.FALSE;

    /** 會員銀行 - 系統費用設定模組 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_MODULE_BANK_FEE)
    @Role(Permission.MODULE_BANK_FEE)
    private Boolean moduleBankFee = Boolean.FALSE;

    /** 會員銀行 - 系統費用查詢 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_BANK_FEE_QUERY)
    @Role(Permission.BANK_FEE_QUERY)
    private Boolean bankFeeQuery = Boolean.FALSE;

    /** 會員銀行 - 系統費用編輯 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_BANK_FEE_MODIFY)
    @Role(Permission.BANK_FEE_MODIFY)
    private Boolean bankFeeModify = Boolean.FALSE;

    /** 會員銀行 - 系統費用設定稽核 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_BANK_FEE_AUDIT)
    @Role(Permission.BANK_FEE_AUDIT)
    private Boolean bankFeeAudit = Boolean.FALSE;

    /** 會員銀行 - OTP發送設定模組 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_MODULE_BANK_OTP_SENDING)
    @Role(Permission.MODULE_BANK_OTP_SENDING)
    private Boolean moduleBankOtpSending = Boolean.FALSE;

    /** 會員銀行 - OTP發送設定查詢 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_BANK_OTP_SENDING_QUERY)
    @Role(Permission.BANK_OTP_SENDING_QUERY)
    private Boolean bankOtpSendingQuery = Boolean.FALSE;

    /** 會員銀行 - OTP發送設定編輯 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_BANK_OTP_SENDING_MODIFY)
    @Role(Permission.BANK_OTP_SENDING_MODIFY)
    private Boolean bankOtpSendingModify = Boolean.FALSE;

    /** 會員銀行 - OTP發送設定稽核 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_BANK_OTP_SENDING_AUDIT)
    @Role(Permission.BANK_OTP_SENDING_AUDIT)
    private Boolean bankOtpSendingAudit = Boolean.FALSE;

    /** 系統設定 - BIN Range管理模組 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_MODULE_SYS_BIN_RANGE)
    @Role(Permission.MODULE_SYS_BIN_RANGE)
    private Boolean moduleSysBinRange = Boolean.FALSE;

    /** 系統設定 - BIN Range查詢 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_SYS_BIN_RANGE_QUERY)
    @Role(Permission.SYS_BIN_RANGE_QUERY)
    private Boolean sysBinRangeQuery = Boolean.FALSE;

    /** 系統設定 - BIN Range編輯 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_SYS_BIN_RANGE_MODIFY)
    @Role(Permission.SYS_BIN_RANGE_MODIFY)
    private Boolean sysBinRangeModify = Boolean.FALSE;

    /** 系統設定 - BIN Range管理稽核 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_SYS_BIN_RANGE_AUDIT)
    @Role(Permission.SYS_BIN_RANGE_AUDIT)
    private Boolean sysBinRangeAudit = Boolean.FALSE;

    /** 系統設定 - 一般管理模組 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_MODULE_GENERAL_SETTING)
    @Role(Permission.MODULE_GENERAL_SETTING)
    private Boolean moduleGeneralSetting = Boolean.FALSE;

    /** 系統設定 - 一般管理查詢 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_GENERAL_SETTING_QUERY)
    @Role(Permission.GENERAL_SETTING_QUERY)
    private Boolean generalSettingQuery = Boolean.FALSE;

    /** 系統設定 - 一般管理編輯 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_GENERAL_SETTING_MODIFY)
    @Role(Permission.GENERAL_SETTING_MODIFY)
    private Boolean generalSettingModify = Boolean.FALSE;

    /** 系統設定 - 卡組織商標設定模組 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_MODULE_SYS_CARD_LOGO)
    @Role(Permission.MODULE_SYS_CARD_LOGO)
    private Boolean moduleSysCardLogo = Boolean.FALSE;

    /** 系統設定 - 卡組織商標設定查詢 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_SYS_CARD_LOGO_QUERY)
    @Role(Permission.SYS_CARD_LOGO_QUERY)
    private Boolean sysCardLogoQuery = Boolean.FALSE;

    /** 系統設定 - 卡組織商標設定編輯 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_SYS_CARD_LOGO_MODIFY)
    @Role(Permission.SYS_CARD_LOGO_MODIFY)
    private Boolean sysCardLogoModify = Boolean.FALSE;

    /** 系統設定 - 卡組織商標設定稽核 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_SYS_CARD_LOGO_AUDIT)
    @Role(Permission.SYS_CARD_LOGO_AUDIT)
    private Boolean sysCardLogoAudit = Boolean.FALSE;

    /** 系統設定 - 驗證畫面模組 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_MODULE_SYS_CHALLENGE_VIEW)
    @Role(Permission.MODULE_SYS_CHALLENGE_VIEW)
    private Boolean moduleSysChallengeView = Boolean.FALSE;

    /** 系統設定 - 驗證畫面查詢 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_SYS_CHALLENGE_VIEW_QUERY)
    @Role(Permission.SYS_CHALLENGE_VIEW_QUERY)
    private Boolean sysChallengeViewQuery = Boolean.FALSE;

    /** 系統設定 - 驗證畫面編輯 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_SYS_CHALLENGE_VIEW_MODIFY)
    @Role(Permission.SYS_CHALLENGE_VIEW_MODIFY)
    private Boolean sysChallengeViewModify = Boolean.FALSE;

    /** 系統設定 - 驗證畫面稽核 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_SYS_CHALLENGE_VIEW_AUDIT)
    @Role(Permission.SYS_CHALLENGE_VIEW_AUDIT)
    private Boolean sysChallengeViewAudit = Boolean.FALSE;

    /** 系統設定 - 簡訊驗證模板模組 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_MODULE_SYS_CHALLENGE_SMS_MSG)
    @Role(Permission.MODULE_SYS_CHALLENGE_SMS_MSG)
    private Boolean moduleSysChallengeSmsMsg = Boolean.FALSE;

    /** 系統設定 - 簡訊驗證模板查詢 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_SYS_CHALLENGE_SMS_MSG_QUERY)
    @Role(Permission.SYS_CHALLENGE_SMS_MSG_QUERY)
    private Boolean sysChallengeSmsMsgQuery = Boolean.FALSE;

    /** 系統設定 - 簡訊驗證模板編輯 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_SYS_CHALLENGE_SMS_MSG_MODIFY)
    @Role(Permission.SYS_CHALLENGE_SMS_MSG_MODIFY)
    private Boolean sysChallengeSmsMsgModify = Boolean.FALSE;

    /** 系統設定 - 簡訊驗證模板稽核 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_SYS_CHALLENGE_SMS_MSG_AUDIT)
    @Role(Permission.SYS_CHALLENGE_SMS_MSG_AUDIT)
    private Boolean sysChallengeSmsMsgAudit = Boolean.FALSE;

    /** 系統設定 - ACS操作者ID模組 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_MODULE_SYS_ACS_OPERATOR_ID)
    @Role(Permission.MODULE_SYS_ACS_OPERATOR_ID)
    private Boolean moduleSysAcsOperatorId = Boolean.FALSE;

    /** 系統設定 - ACS操作者ID查詢 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_SYS_ACS_OPERATOR_ID_QUERY)
    @Role(Permission.SYS_ACS_OPERATOR_ID_QUERY)
    private Boolean sysAcsOperatorIdQuery = Boolean.FALSE;

    /** 系統設定 - ACS操作者ID編輯 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_SYS_ACS_OPERATOR_ID_MODIFY)
    @Role(Permission.SYS_ACS_OPERATOR_ID_MODIFY)
    private Boolean sysAcsOperatorIdModify = Boolean.FALSE;

    /** 系統設定 - ACS操作者ID稽核 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_SYS_ACS_OPERATOR_ID_AUDIT)
    @Role(Permission.SYS_ACS_OPERATOR_ID_AUDIT)
    private Boolean sysAcsOperatorIdAudit = Boolean.FALSE;

    /** 系統設定 - 連線逾時設定模組 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_MODULE_SYS_TIMEOUT)
    @Role(Permission.MODULE_SYS_TIMEOUT)
    private Boolean moduleSysTimeout = Boolean.FALSE;

    /** 系統設定 - 連線逾時查詢 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_SYS_TIMEOUT_QUERY)
    @Role(Permission.SYS_TIMEOUT_QUERY)
    private Boolean sysTimeoutQuery = Boolean.FALSE;

    /** 系統設定 - 連線逾時編輯 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_SYS_TIMEOUT_MODIFY)
    @Role(Permission.SYS_TIMEOUT_MODIFY)
    private Boolean sysTimeoutModify = Boolean.FALSE;

    /** 系統設定 - 連線逾時設定稽核 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_SYS_TIMEOUT_AUDIT)
    @Role(Permission.SYS_TIMEOUT_AUDIT)
    private Boolean sysTimeoutAudit = Boolean.FALSE;

    /** 系統設定 - 錯誤代碼管理 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_MODULE_SYS_ERROR_CODE)
    @Role(Permission.MODULE_SYS_ERROR_CODE)
    private Boolean moduleSysErrorCode = Boolean.FALSE;

    /** 系統設定 - 錯誤代碼查詢 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_SYS_ERROR_CODE_QUERY)
    @Role(Permission.SYS_ERROR_CODE_QUERY)
    private Boolean sysErrorCodeQuery = Boolean.FALSE;

    /** 系統設定 - 錯誤代碼編輯 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_SYS_ERROR_CODE_MODIFY)
    @Role(Permission.SYS_ERROR_CODE_MODIFY)
    private Boolean sysErrorCodeModify = Boolean.FALSE;

    /** 系統設定 - 錯誤代碼管理稽核 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_SYS_ERROR_CODE_AUDIT)
    @Role(Permission.SYS_ERROR_CODE_AUDIT)
    private Boolean sysErrorCodeAudit = Boolean.FALSE;

    /** 系統設定 - 密鑰管理 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_MODULE_SYS_KEY)
    @Role(Permission.MODULE_SYS_KEY)
    private Boolean moduleSysKey = Boolean.FALSE;

    /** 系統設定 - 密鑰查詢 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_SYS_KEY_QUERY)
    @Role(Permission.SYS_KEY_QUERY)
    private Boolean sysKeyQuery = Boolean.FALSE;

    /** 系統設定 - 密鑰編輯 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_SYS_KEY_MODIFY)
    @Role(Permission.SYS_KEY_MODIFY)
    private Boolean sysKeyModify = Boolean.FALSE;

    /** 系統設定 - 密鑰管理稽核 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_SYS_KEY_AUDIT)
    @Role(Permission.SYS_KEY_AUDIT)
    private Boolean sysKeyAudit = Boolean.FALSE;

    /** 系統設定 - 憑證管理 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_MODULE_CERT)
    @Role(Permission.MODULE_CERT)
    private Boolean moduleCert = Boolean.FALSE;

    /** 系統設定 - 憑證查詢 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_CERT_QUERY)
    @Role(Permission.CERT_QUERY)
    private Boolean certQuery = Boolean.FALSE;

    /** 系統設定 - 憑證編輯 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_CERT_MODIFY)
    @Role(Permission.CERT_MODIFY)
    private Boolean certModify = Boolean.FALSE;

    /** 系統設定 - 憑證編輯 */
    @NonNull
    @Role(Permission.MODULE_CLASSIC_RBA)
    @Column(name = DBKey.COL_USER_GROUP_MODULE_CLASSIC_RBA)
    private Boolean moduleClassicRba = Boolean.FALSE;

    /** 風險管理 - 經典風控查詢 */
    @NonNull
    @Role(Permission.CLASSIC_RBA_QUERY)
    @Column(name = DBKey.COL_USER_GROUP_CLASSIC_RBA_QUERY)
    private Boolean classicRbaQuery = Boolean.FALSE;

    /** 風險管理 - 經典風控修改 */
    @NonNull
    @Role(Permission.CLASSIC_RBA_MODIFY)
    @Column(name = DBKey.COL_USER_GROUP_CLASSIC_RBA_MODIFY)
    private Boolean classicRbaModify = Boolean.FALSE;

    /** 風險管理 - 經典風控稽核 */
    @NonNull
    @Role(Permission.CLASSIC_RBA_AUDIT)
    @Column(name = DBKey.COL_USER_GROUP_CLASSIC_RBA_AUDIT)
    private Boolean classicRbaAudit = Boolean.FALSE;

    /** 系統設定 - 憑證管理稽核 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_CERT_AUDIT)
    @Role(Permission.CERT_AUDIT)
    private Boolean certAudit = Boolean.FALSE;

    /** 多銀行 */
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_ACCESS_MULTI_BANK)
    @Role(Permission.ACCESS_MULTI_BANK)
    private Boolean accessMultiBank = Boolean.FALSE;

    @Column(name = DBKey.COL_AUDIT_STATUS)
    private String auditStatus;

    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_MODULE_PLUGIN_ISSUER_PROPERTY)
    @Role(Permission.MODULE_PLUGIN_ISSUER_PROPERTY)
    private Boolean modulePluginIssuerProperty = Boolean.FALSE;

    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_PLUGIN_ISSUER_PROPERTY_QUERY)
    @Role(Permission.PLUGIN_ISSUER_PROPERTY_QUERY)
    private Boolean pluginIssuerPropertyQuery = Boolean.FALSE;

    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_PLUGIN_ISSUER_PROPERTY_MODIFY)
    @Role(Permission.PLUGIN_ISSUER_PROPERTY_MODIFY)
    private Boolean pluginIssuerPropertyModify = Boolean.FALSE;


    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_MODULE_MIMA_POLICY)
    @Role(Permission.MODULE_MIMA_POLICY)
    private Boolean moduleMimaPolicy = Boolean.FALSE;
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_MIMA_POLICY_QUERY)
    @Role(Permission.MIMA_POLICY_QUERY)
    private Boolean mimaPolicyQuery = Boolean.FALSE;
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_MIMA_POLICY_MODIFY)
    @Role(Permission.MIMA_POLICY_MODIFY)
    private Boolean mimaPolicyModify = Boolean.FALSE;

    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_MODULE_ACQUIRER_BANK)
    @Role(Permission.MODULE_ACQUIRER_BANK)
    private Boolean moduleAcquirerBank = Boolean.FALSE;
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_ACQUIRER_BANK_QUERY)
    @Role(Permission.ACQUIRER_BANK_QUERY)
    private Boolean acquirerBankQuery = Boolean.FALSE;
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_ACQUIRER_BANK_MODIFY)
    @Role(Permission.ACQUIRER_BANK_MODIFY)
    private Boolean acquirerBankModify = Boolean.FALSE;

    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_MODULE_BANK_DATA_KEY)
    @Role(Permission.MODULE_BANK_DATA_KEY)
    private Boolean moduleBankDataKey = Boolean.FALSE;
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_BANK_DATA_KEY_QUERY)
    @Role(Permission.BANK_DATA_KEY_QUERY)
    private Boolean bankDataKeyQuery = Boolean.FALSE;
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_BANK_DATA_KEY_MODIFY)
    @Role(Permission.BANK_DATA_KEY_MODIFY)
    private Boolean bankDataKeyModify = Boolean.FALSE;

    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_MODULE_VELOG)
    @Role(Permission.MODULE_VELOG)
    private Boolean moduleVelog = Boolean.FALSE;
    @NonNull
    @Column(name = DBKey.COL_USER_GROUP_VELOG_QUERY)
    @Role(Permission.VELOG_QUERY)
    private Boolean velogQuery = Boolean.FALSE;

    /**
     * 如果有修改權限相關請記得同步更改以下Class及{@Table user_group}
     * 1. {@link UserGroup} Field
     * 2. {@link UserGroup} All args constructor
     * 3. {@link UserGroup#valueOf(UserGroupDO)}
     * 4. {@link ocean.acs.models.sql_server.entity.UserGroup} Field
     * 5. {@link ocean.acs.models.sql_server.entity.UserGroup} All args constructor
     * 6. {@link ocean.acs.models.sql_server.entity.UserGroup#valueOf(UserGroupDO)
     * 7. {@link DBKey}}
     * 8. {@link UserGroupDO}
     * 9. {@link UserGroupDO#valueOf(UserGroup)}
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
    public UserGroup(Long id, Long issuerBankId, String name, UserGroupType type,
      UserGroupScope scope, Boolean moduleCanSeePan, Boolean canSeePanQuery, Boolean moduleReport, Boolean reportQuery,
      Boolean moduleHealthCheck, Boolean healthCheckQuery, Boolean moduleTx, Boolean txQuery,
      Boolean moduleCard, Boolean cardQuery, Boolean cardModify, Boolean cardAudit,
      Boolean moduleRiskBlackList, Boolean blackListQuery, Boolean blackListModify,
      Boolean blackListAudit, Boolean moduleRiskWhiteList, Boolean whiteListQuery,
      Boolean whiteListModify, Boolean whiteListAudit, Boolean moduleRiskControl,
      Boolean riskControlQuery, Boolean riskControlModify, Boolean riskControlAudit,
      Boolean moduleUserGroup, Boolean userGroupQuery, Boolean userGroupModify,
      Boolean userGroupAudit, Boolean moduleUserUnlock, Boolean unlockQuery,
      Boolean unlockModify, Boolean unlockAudit, Boolean moduleUserAuditLog,
      Boolean auditLogQuery, Boolean moduleBankManage, Boolean bankManageQuery,
      Boolean bankManageModify, Boolean bankManageAudit, Boolean moduleBankLogo,
      Boolean bankLogoQuery, Boolean bankLogoModify, Boolean bankLogoAudit,
      Boolean moduleBankChannel, Boolean bankChannelQuery, Boolean bankChannelModify,
      Boolean bankChannelAudit, Boolean moduleBankFee, Boolean bankFeeQuery,
      Boolean bankFeeModify, Boolean bankFeeAudit, Boolean moduleBankOtpSending,
      Boolean bankOtpSendingQuery, Boolean bankOtpSendingModify, Boolean bankOtpSendingAudit,
      Boolean moduleSysBinRange, Boolean sysBinRangeQuery, Boolean sysBinRangeModify,
      Boolean sysBinRangeAudit, Boolean moduleSysCardLogo, Boolean sysCardLogoQuery,
      Boolean moduleGeneralSetting, Boolean generalSettingQuery, Boolean generalSettingModify,
      Boolean sysCardLogoModify, Boolean sysCardLogoAudit, Boolean moduleSysChallengeView,
      Boolean sysChallengeViewQuery, Boolean sysChallengeViewModify,
      Boolean sysChallengeViewAudit, Boolean moduleSysChallengeSmsMsg,
      Boolean sysChallengeSmsMsgQuery, Boolean sysChallengeSmsMsgModify,
      Boolean sysChallengeSmsMsgAudit, Boolean moduleSysAcsOperatorId,
      Boolean sysAcsOperatorIdQuery, Boolean sysAcsOperatorIdModify,
      Boolean sysAcsOperatorIdAudit, Boolean moduleSysTimeout, Boolean sysTimeoutQuery,
      Boolean sysTimeoutModify, Boolean sysTimeoutAudit, Boolean moduleSysErrorCode,
      Boolean sysErrorCodeQuery, Boolean sysErrorCodeModify, Boolean sysErrorCodeAudit,
      Boolean moduleSysKey, Boolean sysKeyQuery, Boolean sysKeyModify, Boolean sysKeyAudit,
      Boolean moduleCert, Boolean certQuery, Boolean certModify, Boolean moduleClassicRba,
      Boolean classicRbaQuery, Boolean classicRbaModify, Boolean classicRbaAudit,
      Boolean certAudit, Boolean accessMultiBank, String auditStatus,
      Boolean modulePluginIssuerProperty, Boolean pluginIssuerPropertyQuery,
      Boolean pluginIssuerPropertyModify, Boolean moduleMimaPolicy, Boolean mimaPolicyQuery,
      Boolean mimaPolicyModify,
      Boolean moduleAcquirerBank, Boolean acquirerBankQuery, Boolean acquirerBankModify,
      Boolean moduleBankDataKey, Boolean bankDataKeyQuery,
      Boolean bankDataKeyModify, String creator, Long createMillis, String updater,
      Long updateMillis, Boolean deleteFlag, String deleter, Long deleteMillis,
      Boolean moduleVelog, Boolean velogQuery) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.issuerBankId = issuerBankId;
        this.name = name;
        this.type = type;
        this.scope = scope;
        this.moduleCanSeePan = moduleCanSeePan;
        this.canSeePanQuery = canSeePanQuery;
        this.moduleReport = moduleReport;
        this.reportQuery = reportQuery;
        this.moduleHealthCheck = moduleHealthCheck;
        this.healthCheckQuery = healthCheckQuery;
        this.moduleTx = moduleTx;
        this.txQuery = txQuery;
        this.moduleCard = moduleCard;
        this.cardQuery = cardQuery;
        this.cardModify = cardModify;
        this.cardAudit = cardAudit;
        this.moduleRiskBlackList = moduleRiskBlackList;
        this.blackListQuery = blackListQuery;
        this.blackListModify = blackListModify;
        this.blackListAudit = blackListAudit;
        this.moduleRiskWhiteList = moduleRiskWhiteList;
        this.whiteListQuery = whiteListQuery;
        this.whiteListModify = whiteListModify;
        this.whiteListAudit = whiteListAudit;
        this.moduleRiskControl = moduleRiskControl;
        this.riskControlQuery = riskControlQuery;
        this.riskControlModify = riskControlModify;
        this.riskControlAudit = riskControlAudit;
        this.moduleUserGroup = moduleUserGroup;
        this.userGroupQuery = userGroupQuery;
        this.userGroupModify = userGroupModify;
        this.userGroupAudit = userGroupAudit;
        this.moduleUserUnlock = moduleUserUnlock;
        this.unlockQuery = unlockQuery;
        this.unlockModify = unlockModify;
        this.unlockAudit = unlockAudit;
        this.moduleUserAuditLog = moduleUserAuditLog;
        this.auditLogQuery = auditLogQuery;
        this.moduleBankManage = moduleBankManage;
        this.bankManageQuery = bankManageQuery;
        this.bankManageModify = bankManageModify;
        this.bankManageAudit = bankManageAudit;
        this.moduleBankLogo = moduleBankLogo;
        this.bankLogoQuery = bankLogoQuery;
        this.bankLogoModify = bankLogoModify;
        this.bankLogoAudit = bankLogoAudit;
        this.moduleBankChannel = moduleBankChannel;
        this.bankChannelQuery = bankChannelQuery;
        this.bankChannelModify = bankChannelModify;
        this.bankChannelAudit = bankChannelAudit;
        this.moduleBankFee = moduleBankFee;
        this.bankFeeQuery = bankFeeQuery;
        this.bankFeeModify = bankFeeModify;
        this.bankFeeAudit = bankFeeAudit;
        this.moduleBankOtpSending = moduleBankOtpSending;
        this.bankOtpSendingQuery = bankOtpSendingQuery;
        this.bankOtpSendingModify = bankOtpSendingModify;
        this.bankOtpSendingAudit = bankOtpSendingAudit;
        this.moduleSysBinRange = moduleSysBinRange;
        this.sysBinRangeQuery = sysBinRangeQuery;
        this.sysBinRangeModify = sysBinRangeModify;
        this.sysBinRangeAudit = sysBinRangeAudit;
        this.moduleGeneralSetting = moduleGeneralSetting;
        this.generalSettingQuery = generalSettingQuery;
        this.generalSettingModify = generalSettingModify;
        this.moduleSysCardLogo = moduleSysCardLogo;
        this.sysCardLogoQuery = sysCardLogoQuery;
        this.sysCardLogoModify = sysCardLogoModify;
        this.sysCardLogoAudit = sysCardLogoAudit;
        this.moduleSysChallengeView = moduleSysChallengeView;
        this.sysChallengeViewQuery = sysChallengeViewQuery;
        this.sysChallengeViewModify = sysChallengeViewModify;
        this.sysChallengeViewAudit = sysChallengeViewAudit;
        this.moduleSysChallengeSmsMsg = moduleSysChallengeSmsMsg;
        this.sysChallengeSmsMsgQuery = sysChallengeSmsMsgQuery;
        this.sysChallengeSmsMsgModify = sysChallengeSmsMsgModify;
        this.sysChallengeSmsMsgAudit = sysChallengeSmsMsgAudit;
        this.moduleSysAcsOperatorId = moduleSysAcsOperatorId;
        this.sysAcsOperatorIdQuery = sysAcsOperatorIdQuery;
        this.sysAcsOperatorIdModify = sysAcsOperatorIdModify;
        this.sysAcsOperatorIdAudit = sysAcsOperatorIdAudit;
        this.moduleSysTimeout = moduleSysTimeout;
        this.sysTimeoutQuery = sysTimeoutQuery;
        this.sysTimeoutModify = sysTimeoutModify;
        this.sysTimeoutAudit = sysTimeoutAudit;
        this.moduleSysErrorCode = moduleSysErrorCode;
        this.sysErrorCodeQuery = sysErrorCodeQuery;
        this.sysErrorCodeModify = sysErrorCodeModify;
        this.sysErrorCodeAudit = sysErrorCodeAudit;
        this.moduleSysKey = moduleSysKey;
        this.sysKeyQuery = sysKeyQuery;
        this.sysKeyModify = sysKeyModify;
        this.sysKeyAudit = sysKeyAudit;
        this.moduleCert = moduleCert;
        this.certQuery = certQuery;
        this.certModify = certModify;
        this.moduleClassicRba = moduleClassicRba;
        this.classicRbaQuery = classicRbaQuery;
        this.classicRbaModify = classicRbaModify;
        this.classicRbaAudit = classicRbaAudit;
        this.certAudit = certAudit;
        this.accessMultiBank = accessMultiBank;
        this.auditStatus = auditStatus;
        this.modulePluginIssuerProperty = modulePluginIssuerProperty;
        this.pluginIssuerPropertyQuery = pluginIssuerPropertyQuery;
        this.pluginIssuerPropertyModify = pluginIssuerPropertyModify;
        this.moduleMimaPolicy = moduleMimaPolicy;
        this.mimaPolicyQuery = mimaPolicyQuery;
        this.mimaPolicyModify = mimaPolicyModify;
        this.moduleAcquirerBank = moduleAcquirerBank;
        this.acquirerBankQuery = acquirerBankQuery;
        this.acquirerBankModify = acquirerBankModify;
        this.moduleBankDataKey = moduleBankDataKey;
        this.bankDataKeyQuery = bankDataKeyQuery;
        this.bankDataKeyModify = bankDataKeyModify;
        this.moduleVelog = moduleVelog;
        this.velogQuery = velogQuery;
    }

    public Set<String> getPermissionSymbol() throws IllegalAccessException {
        Class<? extends UserGroup> clazz = this.getClass();
        Field[] fields = clazz.getDeclaredFields();

        Set<String> permissionSymbols = new HashSet<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Role.class)
              && ((Boolean) field.get(this)).booleanValue()) {
                Role role = field.getAnnotation(Role.class);
                permissionSymbols.add(role.value().getSymbol());
            }
        }

        return permissionSymbols;
    }

    public Set<String> getPermissionDtoField()
      throws IllegalArgumentException, IllegalAccessException {
        Class<? extends UserGroup> clazz = this.getClass();
        Field[] fields = clazz.getDeclaredFields();

        Set<String> permissionDtoFields = new HashSet<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Role.class)
              && ((Boolean) field.get(this)).booleanValue()) {
                Role role = field.getAnnotation(Role.class);
                permissionDtoFields.add(role.value().getPermissionDtoField());
            }
        }

        return permissionDtoFields;
    }

    /**
     * 如果有修改權限相關請記得同步更改以下Class及{@Table user_group}
     * 1. {@link UserGroup} Field
     * 2. {@link UserGroup} All args constructor
     * 3. {@link UserGroup#valueOf(UserGroupDO)}
     * 4. {@link ocean.acs.models.sql_server.entity.UserGroup} Field
     * 5. {@link ocean.acs.models.sql_server.entity.UserGroup} All args constructor
     * 6. {@link ocean.acs.models.sql_server.entity.UserGroup#valueOf(UserGroupDO)
     * 7. {@link DBKey}}
     * 8. {@link UserGroupDO}
     * 9. {@link UserGroupDO#valueOf(UserGroup)}
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
    public static UserGroup valueOf(UserGroupDO d) {
        UserGroup a = new UserGroup();
        a.setId(d.getId());
        a.setIssuerBankId(d.getIssuerBankId());
        a.setName(d.getName());
        a.setType(d.getType());
        a.setScope(d.getScope());
        a.setModuleCanSeePan(d.getModuleCanSeePan());
        a.setCanSeePanQuery(d.getCanSeePanQuery());
        a.setModuleReport(d.getModuleReport());
        a.setReportQuery(d.getReportQuery());
        a.setModuleHealthCheck(d.getModuleHealthCheck());
        a.setHealthCheckQuery(d.getHealthCheckQuery());
        a.setModuleTx(d.getModuleTx());
        a.setTxQuery(d.getTxQuery());
        a.setModuleCard(d.getModuleCard());
        a.setCardQuery(d.getCardQuery());
        a.setCardModify(d.getCardModify());
        a.setCardAudit(d.getCardAudit());
        a.setModuleRiskBlackList(d.getModuleRiskBlackList());
        a.setBlackListQuery(d.getBlackListQuery());
        a.setBlackListModify(d.getBlackListModify());
        a.setBlackListAudit(d.getBlackListAudit());
        a.setModuleRiskWhiteList(d.getModuleRiskWhiteList());
        a.setWhiteListQuery(d.getWhiteListQuery());
        a.setWhiteListModify(d.getWhiteListModify());
        a.setWhiteListAudit(d.getWhiteListAudit());
        a.setModuleRiskControl(d.getModuleRiskControl());
        a.setRiskControlQuery(d.getRiskControlQuery());
        a.setRiskControlModify(d.getRiskControlModify());
        a.setRiskControlAudit(d.getRiskControlAudit());
        a.setModuleUserGroup(d.getModuleUserGroup());
        a.setUserGroupQuery(d.getUserGroupQuery());
        a.setUserGroupModify(d.getUserGroupModify());
        a.setUserGroupAudit(d.getUserGroupAudit());
        a.setModuleUserUnlock(d.getModuleUserUnlock());
        a.setUnlockQuery(d.getUnlockQuery());
        a.setUnlockModify(d.getUnlockModify());
        a.setUnlockAudit(d.getUnlockAudit());
        a.setModuleUserAuditLog(d.getModuleUserAuditLog());
        a.setAuditLogQuery(d.getAuditLogQuery());
        a.setModuleBankManage(d.getModuleBankManage());
        a.setBankManageQuery(d.getBankManageQuery());
        a.setBankManageModify(d.getBankManageModify());
        a.setBankManageAudit(d.getBankManageAudit());
        a.setModuleBankLogo(d.getModuleBankLogo());
        a.setBankLogoQuery(d.getBankLogoQuery());
        a.setBankLogoModify(d.getBankLogoModify());
        a.setBankLogoAudit(d.getBankLogoAudit());
        a.setModuleBankChannel(d.getModuleBankChannel());
        a.setBankChannelQuery(d.getBankChannelQuery());
        a.setBankChannelModify(d.getBankChannelModify());
        a.setBankChannelAudit(d.getBankChannelAudit());
        a.setModuleBankFee(d.getModuleBankFee());
        a.setBankFeeQuery(d.getBankFeeQuery());
        a.setBankFeeModify(d.getBankFeeModify());
        a.setBankFeeAudit(d.getBankFeeAudit());
        a.setModuleBankOtpSending(d.getModuleBankOtpSending());
        a.setBankOtpSendingQuery(d.getBankOtpSendingQuery());
        a.setBankOtpSendingModify(d.getBankOtpSendingModify());
        a.setBankOtpSendingAudit(d.getBankOtpSendingAudit());
        a.setModuleSysBinRange(d.getModuleSysBinRange());
        a.setSysBinRangeQuery(d.getSysBinRangeQuery());
        a.setSysBinRangeModify(d.getSysBinRangeModify());
        a.setSysBinRangeAudit(d.getSysBinRangeAudit());
        a.setModuleGeneralSetting(d.getModuleGeneralSetting());
        a.setGeneralSettingQuery(d.getGeneralSettingQuery());
        a.setGeneralSettingModify(d.getGeneralSettingModify());
        a.setModuleSysCardLogo(d.getModuleSysCardLogo());
        a.setSysCardLogoQuery(d.getSysCardLogoQuery());
        a.setSysCardLogoModify(d.getSysCardLogoModify());
        a.setSysCardLogoAudit(d.getSysCardLogoAudit());
        a.setModuleSysChallengeView(d.getModuleSysChallengeView());
        a.setSysChallengeViewQuery(d.getSysChallengeViewQuery());
        a.setSysChallengeViewModify(d.getSysChallengeViewModify());
        a.setSysChallengeViewAudit(d.getSysChallengeViewAudit());
        a.setModuleSysChallengeSmsMsg(d.getModuleSysChallengeSmsMsg());
        a.setSysChallengeSmsMsgQuery(d.getSysChallengeSmsMsgQuery());
        a.setSysChallengeSmsMsgModify(d.getSysChallengeSmsMsgModify());
        a.setSysChallengeSmsMsgAudit(d.getSysChallengeSmsMsgAudit());
        a.setModuleSysAcsOperatorId(d.getModuleSysAcsOperatorId());
        a.setSysAcsOperatorIdQuery(d.getSysAcsOperatorIdQuery());
        a.setSysAcsOperatorIdModify(d.getSysAcsOperatorIdModify());
        a.setSysAcsOperatorIdAudit(d.getSysAcsOperatorIdAudit());
        a.setModuleSysTimeout(d.getModuleSysTimeout());
        a.setSysTimeoutQuery(d.getSysTimeoutQuery());
        a.setSysTimeoutModify(d.getSysTimeoutModify());
        a.setSysTimeoutAudit(d.getSysTimeoutAudit());
        a.setModuleSysErrorCode(d.getModuleSysErrorCode());
        a.setSysErrorCodeQuery(d.getSysErrorCodeQuery());
        a.setSysErrorCodeModify(d.getSysErrorCodeModify());
        a.setSysErrorCodeAudit(d.getSysErrorCodeAudit());
        a.setModuleSysKey(d.getModuleSysKey());
        a.setSysKeyQuery(d.getSysKeyQuery());
        a.setSysKeyModify(d.getSysKeyModify());
        a.setSysKeyAudit(d.getSysKeyAudit());
        a.setModuleCert(d.getModuleCert());
        a.setCertQuery(d.getCertQuery());
        a.setCertModify(d.getCertModify());
        a.setModuleClassicRba(d.getModuleClassicRba());
        a.setClassicRbaQuery(d.getClassicRbaQuery());
        a.setClassicRbaModify(d.getClassicRbaModify());
        a.setClassicRbaAudit(d.getClassicRbaAudit());
        a.setCertAudit(d.getCertAudit());
        a.setAccessMultiBank(d.getAccessMultiBank());
        a.setAuditStatus(d.getAuditStatus());
        a.setModulePluginIssuerProperty(d.getModulePluginIssuerProperty());
        a.setPluginIssuerPropertyQuery(d.getPluginIssuerPropertyQuery());
        a.setPluginIssuerPropertyModify(d.getPluginIssuerPropertyModify());
        a.setModuleMimaPolicy(d.getModuleMimaPolicy());
        a.setMimaPolicyQuery(d.getMimaPolicyQuery());
        a.setMimaPolicyModify(d.getMimaPolicyModify());
        a.setModuleAcquirerBank(d.getModuleAcquirerBank());
        a.setAcquirerBankQuery(d.getAcquirerBankQuery());
        a.setAcquirerBankModify(d.getAcquirerBankModify());
        a.setModuleBankDataKey(d.getModuleBankDataKey());
        a.setBankDataKeyQuery(d.getBankDataKeyQuery());
        a.setBankDataKeyModify(d.getBankDataKeyModify());
        a.setModuleVelog(d.getModuleVelog());
        a.setVelogQuery(d.getVelogQuery());
        a.setCreator(d.getCreator());
        a.setCreateMillis(d.getCreateMillis());
        a.setUpdater(d.getUpdater());
        a.setUpdateMillis(d.getUpdateMillis());
        a.setDeleteFlag(d.getDeleteFlag());
        a.setDeleter(d.getDeleter());
        a.setDeleteMillis(d.getDeleteMillis());
        return a;
    }

}
