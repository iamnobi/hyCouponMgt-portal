package ocean.acs.models.data_object.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.BrowserType;
import ocean.acs.commons.enumerator.MessageType;
import ocean.acs.commons.enumerator.ResultStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionLogDO {

    private Long id;
    private Long issuerBankId;
    private String acsTransID;
    private String threeDSServerTransID;
    private String threeDSServerOperatorID;
    private String sdkTransID;
    private String dsTransID;
    private String acsIntegratorTransID;
    private Long ddcaLogId;
    private String deviceID;
    private String ip;
    private Long blackListIpGroupId;
    private String notificationURL;
    private String dsURL;
    private String cardBrand;
    private String cardType;
    private Long panInfoId;
    private String deviceChannel;
    private String mresTransStatus;
    private String messageVersion;
    private String messageCategory;
    private String otpPhoneNumber;
    private String authID;
    private Integer challengeAcsCounterAToS;
    @Builder.Default
    private Integer challengeVerifyCount = 0;
    @Builder.Default
    private Integer challengeResendCount = 0;
    private Boolean challengePhoneCorrect;
    private Boolean challengeCompleted;
    private Integer aresResultReasonCode;
    private Integer challengeErrorReasonCode;
    private Integer resultErrorReasonCode;
    private Long attemptSettingID;
    private Long whiteListID;
    private byte[] sdkSessionKey;
    private String threeDSSessionData;
    private Long threeDSMethodLogId;
    private Long authenticationLogID;
    private Long resultLogID;
    private Long errorMessageLogID;
    private Long responseTime;
    private BrowserType browserType;
    private Integer browserMajorVersion;
    private Long smsOtpExpireMillis;
    private Boolean rbaChecked;
    private Boolean rbaPurchaseAmountResult;
    private Boolean rbaCardholderDataResult;
    private Boolean rbaCumulativeAmountResult;
    private String rbaCumulativeAmountLogAmount;
    private String rbaCumulativeAmountLogCount;
    private Boolean rbaCumulativeTransactionResult;
    private Boolean rbaLocationConsistencyResult;
    private Boolean rbaBrowserLanguageResult;
    private Boolean rbaVpnResult;
    private Boolean rbaProxyResult;
    private Boolean rbaPrivateBrowsingResult;
    private Boolean rbaDeviceVariationResult;
    private Boolean rbaMccResult;
    private Boolean rbaRecurringPaymentResult;
    private String sysCreator;
    @Builder.Default
    private Long createMillis = System.currentTimeMillis();
    private String sysUpdater;
    private Long updateMillis;
    private String challengeOtpChannel;
    @Builder.Default
    private Boolean needResendRReq = false;
    @Builder.Default
    private Long resendRReqTime = 0L;

    public static TransactionLogDO valueOf(ocean.acs.models.oracle.entity.TransactionLog e) {
        return new TransactionLogDO(e.getId(), e.getIssuerBankId(), e.getAcsTransID(),
                e.getThreeDSServerTransID(), e.getThreeDSServerOperatorID(), e.getSdkTransID(),
                e.getDsTransID(), e.getAcsIntegratorTransID(), e.getDdcaLogId(), e.getDeviceID(),
                e.getIp(), e.getBlackListIpGroupId(), e.getNotificationURL(), e.getDsURL(),
                e.getCardBrand(), e.getCardType(), e.getPanInfoId(), e.getDeviceChannel(), e.getMresTransStatus(),
                e.getMessageVersion(), e.getMessageCategory(), e.getOtpPhoneNumber(), e.getAuthID(),
                e.getChallengeAcsCounterAToS(), e.getChallengeVerifyCount(),
                e.getChallengeResendCount(), e.getChallengePhoneCorrect(),
                e.getChallengeCompleted(), e.getAresResultReasonCode(),
                e.getChallengeErrorReasonCode(), e.getResultErrorReasonCode(),
                e.getAttemptSettingID(), e.getWhiteListID(), e.getSdkSessionKey(),
                e.getThreeDSSessionData(), e.getThreeDSMethodLogId(), e.getAuthenticationLogID(),
                e.getResultLogID(), e.getErrorMessageLogID(), e.getResponseTime(),
                e.getBrowserType(), e.getBrowserMajorVersion(),e.getSmsOtpExpireMillis(), e.getRbaChecked(),
                e.getRbaPurchaseAmountResult(), e.getRbaCardholderDataResult(),
                e.getRbaCumulativeAmountResult(), e.getRbaCumulativeAmountLogAmount(), e.getRbaCumulativeAmountLogCount(),
                e.getRbaCumulativeTransactionResult(),
                e.getRbaLocationConsistencyResult(), e.getRbaBrowserLanguageResult(),
                e.getRbaVpnResult(), e.getRbaProxyResult(), e.getRbaPrivateBrowsingResult(),
                e.getRbaDeviceVariationResult(), e.getRbaMccResult(),
                e.getRbaRecurringPaymentResult(), e.getSysCreator(), e.getCreateMillis(),
                e.getSysUpdater(), e.getUpdateMillis(), e.getChallengeOtpChannel(), e.getNeedResendRReq(),
                e.getResendRReqTime());
    }

    public static TransactionLogDO valueOf(ocean.acs.models.sql_server.entity.TransactionLog e) {
        return new TransactionLogDO(e.getId(), e.getIssuerBankId(), e.getAcsTransID(),
                e.getThreeDSServerTransID(), e.getThreeDSServerOperatorID(), e.getSdkTransID(),
                e.getDsTransID(), e.getAcsIntegratorTransID(), e.getDdcaLogId(), e.getDeviceID(),
                e.getIp(), e.getBlackListIpGroupId(), e.getNotificationURL(), e.getDsURL(),
                e.getCardBrand(), e.getCardType(), e.getPanInfoId(), e.getDeviceChannel(), e.getMresTransStatus(),
                e.getMessageVersion(), e.getMessageCategory(), e.getOtpPhoneNumber(), e.getAuthID(),
                e.getChallengeAcsCounterAToS(), e.getChallengeVerifyCount(),
                e.getChallengeResendCount(), e.getChallengePhoneCorrect(),
                e.getChallengeCompleted(), e.getAresResultReasonCode(),
                e.getChallengeErrorReasonCode(), e.getResultErrorReasonCode(),
                e.getAttemptSettingID(), e.getWhiteListID(), e.getSdkSessionKey(),
                e.getThreeDSSessionData(), e.getThreeDSMethodLogId(), e.getAuthenticationLogID(),
                e.getResultLogID(), e.getErrorMessageLogID(), e.getResponseTime(),
                e.getBrowserType(), e.getBrowserMajorVersion(),e.getSmsOtpExpireMillis(), e.getRbaChecked(),
                e.getRbaPurchaseAmountResult(), e.getRbaCardholderDataResult(),
                e.getRbaCumulativeAmountResult(), e.getRbaCumulativeAmountLogAmount(), e.getRbaCumulativeAmountLogCount(),
                e.getRbaCumulativeTransactionResult(),
                e.getRbaLocationConsistencyResult(), e.getRbaBrowserLanguageResult(),
                e.getRbaVpnResult(), e.getRbaProxyResult(), e.getRbaPrivateBrowsingResult(),
                e.getRbaDeviceVariationResult(), e.getRbaMccResult(),
                e.getRbaRecurringPaymentResult(), e.getSysCreator(), e.getCreateMillis(),
                e.getSysUpdater(), e.getUpdateMillis(), e.getChallengeOtpChannel(), e.getNeedResendRReq(),
                e.getResendRReqTime());
    }

    public static TransactionLogDO newInstance(String acsTransID, MessageType messageType){
        TransactionLogDO transactionLogDO = new TransactionLogDO();
        transactionLogDO.setSysCreator(messageType.name());
          transactionLogDO.setAcsTransID(acsTransID);
      return transactionLogDO;
  }

    public void setAresResultReasonCode(ResultStatus resultStatus) {
        this.aresResultReasonCode = resultStatus.getCode();
    }

    public void setChallengeErrorReasonCode(ResultStatus resultStatus) {
        this.challengeErrorReasonCode = resultStatus.getCode();
    }

    public void setResultErrorReasonCode(ResultStatus resultStatus) {
        this.resultErrorReasonCode = resultStatus.getCode();
    }


}
