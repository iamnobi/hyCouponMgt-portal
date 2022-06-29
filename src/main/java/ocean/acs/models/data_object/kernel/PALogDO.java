package ocean.acs.models.data_object.kernel;

import lombok.Builder;
import lombok.Data;
import ocean.acs.models.oracle.entity.PALog;

@Data
@Builder
public class PALogDO {
  private String id;
  private Long issuerBankID;
  private Long panInfoID;
  private Long blackListIpGroupId;
  private String veLogId;
  private Long attemptSettingID;
  private Long whiteListID;
  private String cardBrand;
  private String deviceID;
  private String ip;
  private String browserUserAgent;
  private String challengeOtpChannel;
  private String termUrl;
  private String merchantData;
  private String pareqField;
  private String paresField;
  private String messageId;
  private String version;
  private String merchantAcquirerBIN;
  private String merchantMerId;
  private String merchantName;
  private String merchantCountry;
  private String merchantUrl;
  private String purchaseXid;
  private String purchaseDate;
  private String purchaseDisplayAmount;
  private String purchasePurchAmount;
  private String purchaseCurrency;
  private String purchaseExponent;
  private String purchaseDesc;
  private String purchaseRecurFrequency;
  private String purchaseRecurEndRecur;
  private String purchaseInstall;
  private String txTime;
  private String txStatus;
  private String txCavv;
  private String txEci;
  private String iReqCode;
  private String iReqDetail;
  // send & resend 是同個入口 (controller), 所以 challengeResendCount 從 -1 開始
  // pareq -> -1; send otp -> 0; resend-1 -> 1 ...
  @Builder.Default
  private Integer challengeResendCount = -1;
  @Builder.Default
  private Boolean challengeCompleted = false;
  private Integer paResErrorReasonCode;
  private Integer challengeErrorReasonCode;
  private String sysCreator;
  @Builder.Default
  private Long createMillis = System.currentTimeMillis();
  private String sysUpdater;
  private Long updateMillis;

  public static PALogDO valueOf(PALog paLog) {
    return PALogDO.builder()
        .id(paLog.getId())
        .issuerBankID(paLog.getIssuerBankID())
        .panInfoID(paLog.getPanInfoID())
        .blackListIpGroupId(paLog.getBlackListIpGroupId())
        .veLogId(paLog.getVeLogId())
        .attemptSettingID(paLog.getAttemptSettingID())
        .whiteListID(paLog.getWhiteListID())
        .cardBrand(paLog.getCardBrand())
        .deviceID(paLog.getDeviceID())
        .ip(paLog.getIp())
        .browserUserAgent(paLog.getBrowserUserAgent())
        .challengeOtpChannel(paLog.getChallengeOtpChannel())
        .termUrl(paLog.getTermUrl())
        .merchantData(paLog.getMerchantData())
        .pareqField(paLog.getPareqField())
        .paresField(paLog.getParesField())
        .messageId(paLog.getMessageId())
        .version(paLog.getVersion())
        .merchantAcquirerBIN(paLog.getMerchantAcquirerBIN())
        .merchantMerId(paLog.getMerchantMerId())
        .merchantName(paLog.getMerchantName())
        .merchantCountry(paLog.getMerchantCountry())
        .merchantUrl(paLog.getMerchantUrl())
        .purchaseXid(paLog.getPurchaseXid())
        .purchaseDate(paLog.getPurchaseDate())
        .purchaseDisplayAmount(paLog.getPurchaseDisplayAmount())
        .purchasePurchAmount(paLog.getPurchasePurchAmount())
        .purchaseCurrency(paLog.getPurchaseCurrency())
        .purchaseExponent(paLog.getPurchaseExponent())
        .purchaseDesc(paLog.getPurchaseDesc())
        .purchaseRecurFrequency(paLog.getPurchaseRecurFrequency())
        .purchaseRecurEndRecur(paLog.getPurchaseRecurEndRecur())
        .purchaseInstall(paLog.getPurchaseInstall())
        .txTime(paLog.getTxTime())
        .txStatus(paLog.getTxStatus())
        .txCavv(paLog.getTxCavv())
        .txEci(paLog.getTxEci())
        .iReqCode(paLog.getIReqCode())
        .iReqDetail(paLog.getIReqDetail())
        .challengeResendCount(paLog.getChallengeResendCount())
        .challengeCompleted(paLog.getChallengeCompleted())
        .paResErrorReasonCode(paLog.getPaResErrorReasonCode())
        .challengeErrorReasonCode(paLog.getChallengeErrorReasonCode())
        .sysCreator(paLog.getSysCreator())
        .createMillis(paLog.getCreateMillis())
        .sysUpdater(paLog.getSysUpdater())
        .updateMillis(paLog.getUpdateMillis())
        .build();
  }
}
