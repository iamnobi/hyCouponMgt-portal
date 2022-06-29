package ocean.acs.models.oracle.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ocean.acs.models.data_object.kernel.VELogDO;

/** The persistent class for the VERIFICATION_ENROLL_LOG database table. */
@Entity
@Data
@NoArgsConstructor
@Table(name = "VE_LOG")
public class VELog {

  @Id
  @Column(name = "UUID")
  private String id;

  @Column(name = "ISSUER_BANK_ID")
  private Long issuerBankID;

  @Column(name = "PAN_INFO_ID")
  private Long panInfoID;

  @Column(name = "CARD_BRAND")
  private String cardBrand;

  /** only exists if CH_ENROLLED != Y (error or bin range not found) */
  @Column(name = "MASK_CARD_NUMBER")
  private String maskCardNumber;

  @Column(name = "MESSAGE_ID")
  private String messageId;

  @Column(name = "VERSION")
  private String version;

  @Column(name = "MERCHANT_ACQ_B_I_N")
  private String merchantAcquirerBIN;

  @Column(name = "MERCHANT_MER_ID")
  private String merchantMerId;

  @Column(name = "BROWSER_DEVICE_CATEGORY")
  private String browserDeviceCategory;

  @Column(name = "BROWSER_ACCEPT")
  private String browserAccept;

  @Column(name = "BROWSER_USER_AGENT")
  private String browserUserAgent;

  @Column(name = "EXTENSIONS")
  private String extensions;

  @Column(name = "CH_ENROLLED")
  private String chEnrolled;

  @Column(name = "CH_ACCT_ID")
  private String chAcctId;

  @Column(name = "URL")
  private String url;

  @Column(name = "ERROR_CODE")
  private String errorCode;

  @Column(name = "ERROR_DETAIL")
  private String errorDetail;

  @NonNull
  @Column(name = "SYS_CREATOR")
  private String sysCreator;

  @Column(name = "CREATE_MILLIS")
  private Long createMillis = System.currentTimeMillis();

  @Column(name = "SYS_UPDATER")
  private String sysUpdater;

  @Column(name = "UPDATE_MILLIS")
  private Long updateMillis;

  public static VELog valueOf(VELogDO veLogDO) {
    VELog veLog = new VELog();
    veLog.setId(veLogDO.getId());
    veLog.setIssuerBankID(veLogDO.getIssuerBankID());
    veLog.setPanInfoID(veLogDO.getPanInfoID());
    veLog.setCardBrand(veLogDO.getCardBrand());
    veLog.setMaskCardNumber(veLogDO.getMaskCardNumber());
    veLog.setMessageId(veLogDO.getMessageId());
    veLog.setVersion(veLogDO.getVersion());
    veLog.setMerchantAcquirerBIN(veLogDO.getMerchantAcquirerBIN());
    veLog.setMerchantMerId(veLogDO.getMerchantMerId());
    veLog.setBrowserDeviceCategory(veLogDO.getBrowserDeviceCategory());
    veLog.setBrowserAccept(veLogDO.getBrowserAccept());
    veLog.setBrowserUserAgent(veLogDO.getBrowserUserAgent());
    veLog.setExtensions(veLogDO.getExtensions());
    veLog.setChEnrolled(veLogDO.getChEnrolled());
    veLog.setChAcctId(veLogDO.getChAcctId());
    veLog.setUrl(veLogDO.getUrl());
    veLog.setErrorCode(veLogDO.getErrorCode());
    veLog.setErrorDetail(veLogDO.getErrorDetail());
    veLog.setSysCreator(veLogDO.getSysCreator());
    veLog.setCreateMillis(veLogDO.getCreateMillis());
    veLog.setSysUpdater(veLogDO.getSysUpdater());
    veLog.setUpdateMillis(veLogDO.getUpdateMillis());
    return veLog;
  }
}
