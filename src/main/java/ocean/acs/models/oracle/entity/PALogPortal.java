package ocean.acs.models.oracle.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import ocean.acs.models.data_object.kernel.PALogDO;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/** The persistent class for the TRANSACTION_ONE_LOG database table. */
@Entity
@DynamicUpdate
@Log4j2
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "PA_LOG")
public class PALogPortal {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(name = "UUID")
  private String id;

  @Column(name = "ISSUER_BANK_ID")
  private Long issuerBankID;

  @Column(name = "PAN_INFO_ID")
  private Long panInfoID;

  @ManyToOne
  @JoinColumn(name = "PAN_INFO_ID", updatable = false, insertable = false)
  private PanInfo panInfo;

  @Column(name = "BLACK_LIST_IP_GROUP_ID")
  private Long blackListIpGroupId;

  @NonNull
  @Column(name = "VE_LOG_ID")
  private String veLogId;

  @Column(name = "ATTEMPT_SETTING_ID")
  private Long attemptSettingID;

  @Column(name = "WHITE_LIST_ID")
  private Long whiteListID;

  @NonNull
  @Column(name = "CARD_BRAND")
  private String cardBrand;

  @NonNull
  @Column(name = "DEVICE_ID")
  private String deviceID;

  @Column(name = "IP")
  private String ip;

  @Column(name = "BROWSER_USER_AGENT")
  private String browserUserAgent;

  @Column(name = "CHALLENGE_OTP_CHANNEL")
  private String challengeOtpChannel;

  @Column(name = "TERM_URL")
  private String termUrl;

  @Column(name = "MD")
  private String merchantData;

  @Lob
  @Column(name = "PAREQ")
  private String pareqField;

  @Lob
  @Column(name = "PARES")
  private String paresField;

  @Column(name = "MESSAGE_ID")
  private String messageId;

  @Column(name = "VERSION")
  private String version;

  @Column(name = "MERCHANT_ACQ_B_I_N")
  private String merchantAcquirerBIN;

  @Column(name = "MERCHANT_MER_ID")
  private String merchantMerId;

  @Column(name = "MERCHANT_NAME")
  private String merchantName;

  @Column(name = "MERCHANT_COUNTRY")
  private String merchantCountry;

  @Column(name = "MERCHANT_URL")
  private String merchantUrl;

  @Column(name = "PURCHASE_XID")
  private String purchaseXid;

  @Column(name = "PURCHASE_DATE")
  private String purchaseDate;

  @Column(name = "PURCHASE_AMOUNT")
  private String purchaseDisplayAmount;

  @Column(name = "PURCHASE_PURCH_AMOUNT")
  private String purchasePurchAmount;

  @Column(name = "PURCHASE_CURRENCY")
  private String purchaseCurrency;

  @Column(name = "PURCHASE_EXPONENT")
  private String purchaseExponent;

  @Column(name = "PURCHASE_DESC")
  private String purchaseDesc;

  @Column(name = "PURCHASE_RECUR_FREQUENCY")
  private String purchaseRecurFrequency;

  @Column(name = "PURCHASE_RECUR_ENDRECUR")
  private String purchaseRecurEndRecur;

  @Column(name = "PURCHASE_INSTALL")
  private String purchaseInstall;

//  @Column(name = "CH_EXPIRY")
//  private String chExpiry;

  @Column(name = "TX_TIME")
  private String txTime;

  @Column(name = "TX_STATUS")
  private String txStatus;

  @Column(name = "TX_CAVV")
  private String txCavv;

  @Column(name = "TX_ECI")
  private String txEci;

  @Column(name = "IREQ_CODE")
  private String iReqCode;

  @Column(name = "IREQ_DETAIL")
  private String iReqDetail;

  // send & resend 是同個入口 (controller), 所以 challengeResendCount 從 -1 開始
  // pareq -> -1; send otp -> 0; resend-1 -> 1 ...
  @Column(name = "CHALLENGE_RESEND_COUNT")
  @Builder.Default
  private Integer challengeResendCount = -1;

  @Column(name = "CHALLENGE_COMPLETED")
  @Builder.Default
  private Boolean challengeCompleted = false;

  @Column(name = "PARES_RESULT_REASON_CODE")
  private Integer paResErrorReasonCode;

  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(
      name = "PARES_RESULT_REASON_CODE",
      referencedColumnName = "CODE",
      updatable = false,
      insertable = false)
  private ErrorCodeMapping paResErrorReason;

  @Column(name = "CHALLENGE_ERROR_REASON_CODE")
  private Integer challengeErrorReasonCode;

  @ManyToOne
  @JoinColumn(
      name = "CHALLENGE_ERROR_REASON_CODE",
      referencedColumnName = "CODE",
      updatable = false,
      insertable = false)
  private ErrorCodeMapping challengeErrorReason;

  @NonNull
  @Column(name = "SYS_CREATOR")
  private String sysCreator;

  @Column(name = "CREATE_MILLIS")
  @Builder.Default
  private Long createMillis = System.currentTimeMillis();

  @Column(name = "SYS_UPDATER")
  private String sysUpdater;

  @Column(name = "UPDATE_MILLIS")
  private Long updateMillis;

  public static PALogPortal valueOf(PALogDO paLogDO) {
    return PALogPortal.builder()
        .id(paLogDO.getId())
        .issuerBankID(paLogDO.getIssuerBankID())
        .panInfoID(paLogDO.getPanInfoID())
        .blackListIpGroupId(paLogDO.getBlackListIpGroupId())
        .veLogId(paLogDO.getVeLogId())
        .attemptSettingID(paLogDO.getAttemptSettingID())
        .whiteListID(paLogDO.getWhiteListID())
        .cardBrand(paLogDO.getCardBrand())
        .deviceID(paLogDO.getDeviceID())
        .ip(paLogDO.getIp())
        .browserUserAgent(paLogDO.getBrowserUserAgent())
        .challengeOtpChannel(paLogDO.getChallengeOtpChannel())
        .termUrl(paLogDO.getTermUrl())
        .merchantData(paLogDO.getMerchantData())
        .pareqField(paLogDO.getPareqField())
        .paresField(paLogDO.getParesField())
        .messageId(paLogDO.getMessageId())
        .version(paLogDO.getVersion())
        .merchantAcquirerBIN(paLogDO.getMerchantAcquirerBIN())
        .merchantMerId(paLogDO.getMerchantMerId())
        .merchantName(paLogDO.getMerchantName())
        .merchantCountry(paLogDO.getMerchantCountry())
        .merchantUrl(paLogDO.getMerchantUrl())
        .purchaseXid(paLogDO.getPurchaseXid())
        .purchaseDate(paLogDO.getPurchaseDate())
        .purchaseDisplayAmount(paLogDO.getPurchaseDisplayAmount())
        .purchasePurchAmount(paLogDO.getPurchasePurchAmount())
        .purchaseCurrency(paLogDO.getPurchaseCurrency())
        .purchaseExponent(paLogDO.getPurchaseExponent())
        .purchaseDesc(paLogDO.getPurchaseDesc())
        .purchaseRecurFrequency(paLogDO.getPurchaseRecurFrequency())
        .purchaseRecurEndRecur(paLogDO.getPurchaseRecurEndRecur())
        .purchaseInstall(paLogDO.getPurchaseInstall())
        .txTime(paLogDO.getTxTime())
        .txStatus(paLogDO.getTxStatus())
        .txCavv(paLogDO.getTxCavv())
        .txEci(paLogDO.getTxEci())
        .iReqCode(paLogDO.getIReqCode())
        .iReqDetail(paLogDO.getIReqDetail())
        .challengeResendCount(paLogDO.getChallengeResendCount())
        .challengeCompleted(paLogDO.getChallengeCompleted())
        .paResErrorReasonCode(paLogDO.getPaResErrorReasonCode())
        .challengeErrorReasonCode(paLogDO.getChallengeErrorReasonCode())
        .sysCreator(paLogDO.getSysCreator())
        .createMillis(paLogDO.getCreateMillis())
        .sysUpdater(paLogDO.getSysUpdater())
        .updateMillis(paLogDO.getUpdateMillis())
        .build();
  }
}
