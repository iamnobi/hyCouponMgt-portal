package ocean.acs.models.sql_server.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ocean.acs.commons.enumerator.BrowserType;
import ocean.acs.models.data_object.entity.TransactionLogDO;
import ocean.acs.models.entity.DBKey;
import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = DBKey.TABLE_KERNEL_TRANSACTION_LOG)
public class TransactionLog implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String TRANS_STATUS_3DS_METHOD_REQ = "R";
    public static final String TRANS_STATUS_3DS_METHOD_COLLECT = "C";
    public static final String TRANS_STATUS_3DS_METHOD_ERROR = "E";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_ID)
    private Long id;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_ISSUER_BANK_ID)
    private Long issuerBankId;

    @NonNull
    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_ACS_TRANS_ID)
    private String acsTransID;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_THREE_D_S_SERVER_TRANS_ID)
    private String threeDSServerTransID;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_THREE_D_S_SERVER_OPERATOR_ID)
    private String threeDSServerOperatorID;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_SDK_TRANS_ID)
    private String sdkTransID;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_DS_TRANS_ID)
    private String dsTransID;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_ACS_INTEGRATOR_TRANS_ID)
    private String acsIntegratorTransID;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_DDCA_LOG_ID)
    private Long ddcaLogId;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_DEVICE_ID)
    private String deviceID;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_IP)
    private String ip;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_BLACK_LIST_IP_GROUP_ID)
    private Long blackListIpGroupId;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_NOTIFICATION_URL)
    private String notificationURL;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_DS_URL)
    private String dsURL;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_CARD_BRAND)
    private String cardBrand;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_CARD_TYPE)
    private String cardType;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_PAN_INFO_ID)
    private Long panInfoId;

    // @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_ACCT_NUMBER)
    // private String acctNumber;
    //
    // @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_ACCT_NUMBER_HASH)
    // private String acctNumberHash;
    //
    // @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_ACCT_NUMBER_EN)
    // private String acctNumberEn;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_DEVICE_CHANNEL)
    private String deviceChannel;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_MRES_TRANS_STATUS)
    private String mresTransStatus;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_MESSAGE_VERSION)
    private String messageVersion;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_MESSAGE_CATEGORY)
    private String messageCategory;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_OTP_PHONE_NUMBER)
    private String otpPhoneNumber;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_AUTH_ID)
    private String authID;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_CHALLENGE_ACS_COUNTER_A_TO_S)
    private Integer challengeAcsCounterAToS;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_CHALLENGE_VERIFY_COUNT)
    private Integer challengeVerifyCount = 0;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_CHALLENGE_RESEND_COUNT)
    private Integer challengeResendCount = 0;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_CHALLENGE_PHONE_CORRECT)
    private Boolean challengePhoneCorrect;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_CHALLENGE_COMPLETED)
    private Boolean challengeCompleted;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_ARES_RESULT_REASON_CODE)
    private Integer aresResultReasonCode;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_CHALLENGE_ERROR_REASON_CODE)
    private Integer challengeErrorReasonCode;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_RESULT_ERROR_REASON_CODE)
    private Integer resultErrorReasonCode;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_ATTEMPT_SETTING_ID)
    private Long attemptSettingID;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_WHITE_LIST_ID)
    private Long whiteListID;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_SDK_SESSION_KEY)
    private byte[] sdkSessionKey;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_THREE_D_S_SESSION_DATA)
    private String threeDSSessionData;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_THREE_D_S_METHOD_LOG_ID)
    private Long threeDSMethodLogId;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_AUTHENTICATION_LOG_ID)
    private Long authenticationLogID;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_RESULT_LOG_ID)
    private Long resultLogID;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_ERROR_MESSAGE_LOG_ID)
    private Long errorMessageLogID;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_RESPONSE_TIME)
    private Long responseTime;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_BROWSER_TYPE)
    @Enumerated(EnumType.STRING)
    private BrowserType browserType;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_BROWSER_MAJOR_VERSION)
    private Integer browserMajorVersion;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_SMS_OTP_EXPIRE_MILLIS)
    private Long smsOtpExpireMillis;

    // 以下 RBA 相關欄位, null表示開關未被用戶開啟 , 1 表示 frictionless, 0 表示challenge
    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_RBA_CHECKED)
    private Boolean rbaChecked;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_RBA_PURCHASE_AMOUNT_RES)
    private Boolean rbaPurchaseAmountResult;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_RBA_CARDHOLDER_DATA_RES)
    private Boolean rbaCardholderDataResult;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_RBA_CUMULATIVE_AMOUNT_RES)
    private Boolean rbaCumulativeAmountResult;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_RBA_CUMULATIVE_AMOUNT_LOG_AMOUNT)
    private String rbaCumulativeAmountLogAmount;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_RBA_CUMULATIVE_AMOUNT_LOG_COUNT)
    private String rbaCumulativeAmountLogCount;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_RBA_CUMULATIVE_TX_RES)
    private Boolean rbaCumulativeTransactionResult;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_RBA_LOCATION_CONSISTENCY_RES)
    private Boolean rbaLocationConsistencyResult;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_RBA_BROWSER_LANGUAGE_RES)
    private Boolean rbaBrowserLanguageResult;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_RBA_VPN_RES)
    private Boolean rbaVpnResult;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_RBA_PROXY_RES)
    private Boolean rbaProxyResult;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_RBA_PRIVATE_BROWSING_RES)
    private Boolean rbaPrivateBrowsingResult;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_RBA_DEVICE_VARIATION_RES)
    private Boolean rbaDeviceVariationResult;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_RBA_MCC_RES)
    private Boolean rbaMccResult;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_RBA_RECURRING_PAYMENT_RES)
    private Boolean rbaRecurringPaymentResult;

    @NonNull
    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_SYS_CREATOR)
    private String sysCreator;

    @NonNull
    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_CREATE_MILLIS)
    private Long createMillis;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_SYS_UPDATER)
    private String sysUpdater;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_UPDATE_MILLIS)
    private Long updateMillis;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_CHALLENGE_OTP_CHANNEL)
    private String challengeOtpChannel;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_NEED_RESEND_RREQ)
    private Boolean needResendRReq = false;

    @Column(name = DBKey.COL_KERNEL_TRANSACTION_LOG_RESEND_RREQ_TIME)
    private Long resendRReqTime = 0L;

    public static TransactionLog valueOf(TransactionLogDO d) {
        TransactionLog a = new TransactionLog();
        a.setId(d.getId());
        a.setIssuerBankId(d.getIssuerBankId());
        a.setAcsTransID(d.getAcsTransID());
        a.setThreeDSServerTransID(d.getThreeDSServerTransID());
        a.setThreeDSServerOperatorID(d.getThreeDSServerOperatorID());
        a.setSdkTransID(d.getSdkTransID());
        a.setDsTransID(d.getDsTransID());
        a.setAcsIntegratorTransID(d.getAcsIntegratorTransID());
        a.setDdcaLogId(d.getDdcaLogId());
        a.setDeviceID(d.getDeviceID());
        a.setIp(d.getIp());
        a.setBlackListIpGroupId(d.getBlackListIpGroupId());
        a.setNotificationURL(d.getNotificationURL());
        a.setDsURL(d.getDsURL());
        a.setCardBrand(d.getCardBrand());
        a.setCardType(d.getCardType());
        a.setPanInfoId(d.getPanInfoId());
        a.setDeviceChannel(d.getDeviceChannel());
        a.setMresTransStatus(d.getMresTransStatus());
        a.setMessageVersion(d.getMessageVersion());
        a.setMessageCategory(d.getMessageCategory());
        a.setOtpPhoneNumber(d.getOtpPhoneNumber());
        a.setAuthID(d.getAuthID());
        a.setChallengeAcsCounterAToS(d.getChallengeAcsCounterAToS());
        a.setChallengeVerifyCount(d.getChallengeVerifyCount());
        a.setChallengeResendCount(d.getChallengeResendCount());
        a.setChallengePhoneCorrect(d.getChallengePhoneCorrect());
        a.setChallengeCompleted(d.getChallengeCompleted());
        a.setAresResultReasonCode(d.getAresResultReasonCode());
        a.setChallengeErrorReasonCode(d.getChallengeErrorReasonCode());
        a.setResultErrorReasonCode(d.getResultErrorReasonCode());
        a.setAttemptSettingID(d.getAttemptSettingID());
        a.setWhiteListID(d.getWhiteListID());
        a.setSdkSessionKey(d.getSdkSessionKey());
        a.setThreeDSSessionData(d.getThreeDSSessionData());
        a.setThreeDSMethodLogId(d.getThreeDSMethodLogId());
        a.setAuthenticationLogID(d.getAuthenticationLogID());
        a.setResultLogID(d.getResultLogID());
        a.setErrorMessageLogID(d.getErrorMessageLogID());
        a.setResponseTime(d.getResponseTime());
        a.setBrowserType(d.getBrowserType());
        a.setBrowserMajorVersion(d.getBrowserMajorVersion());
        a.setSmsOtpExpireMillis(d.getSmsOtpExpireMillis());
        a.setRbaChecked(d.getRbaChecked());
        a.setRbaPurchaseAmountResult(d.getRbaPurchaseAmountResult());
        a.setRbaCardholderDataResult(d.getRbaCardholderDataResult());
        a.setRbaCumulativeAmountResult(d.getRbaCumulativeAmountResult());
        a.setRbaCumulativeAmountLogAmount(d.getRbaCumulativeAmountLogAmount());
        a.setRbaCumulativeAmountLogCount(d.getRbaCumulativeAmountLogCount());
        a.setRbaCumulativeTransactionResult(d.getRbaCumulativeTransactionResult());
        a.setRbaLocationConsistencyResult(d.getRbaLocationConsistencyResult());
        a.setRbaBrowserLanguageResult(d.getRbaBrowserLanguageResult());
        a.setRbaVpnResult(d.getRbaVpnResult());
        a.setRbaProxyResult(d.getRbaProxyResult());
        a.setRbaPrivateBrowsingResult(d.getRbaPrivateBrowsingResult());
        a.setRbaDeviceVariationResult(d.getRbaDeviceVariationResult());
        a.setRbaMccResult(d.getRbaMccResult());
        a.setRbaRecurringPaymentResult(d.getRbaRecurringPaymentResult());
        a.setSysCreator(d.getSysCreator());
        a.setCreateMillis(d.getCreateMillis());
        a.setSysUpdater(d.getSysUpdater());
        a.setUpdateMillis(d.getUpdateMillis());
        a.setNeedResendRReq(d.getNeedResendRReq());
        a.setResendRReqTime(d.getResendRReqTime());
        return a;
    }

}
