package ocean.acs.models.oracle.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/** The persistent class for the VERIFICATION_ENROLL_LOG database table. */
@Entity
@Data
@NoArgsConstructor
@Table(name = "VE_LOG")
public class VELogPortal {

  @Id
  @Column(name = "UUID")
  private String id;

  @OneToOne(mappedBy = "veLog")
  private PALogId paLog;

  /**
   * chEnrolled = N 時，issuerBankID = 956
   */
  @Column(name = "ISSUER_BANK_ID")
  private String issuerBankID;

  /**
   * chEnrolled = N 時，issuerBankInfo = null
   */
  @ManyToOne
  @JoinColumn(name = "ISSUER_BANK_ID", updatable = false, insertable = false)
  @NotFound(action= NotFoundAction.IGNORE)
  private IssuerBank issuerBank;

  /**
   * chEnrolled = N 時，panInfoID = null
   */
  @Column(name = "PAN_INFO_ID")
  private String panInfoID;

  /**
   * chEnrolled = N 時，panInfo = null
   */
  @ManyToOne
  @JoinColumn(name = "PAN_INFO_ID", updatable = false, insertable = false)
  private PanInfo panInfo;

  @NonNull
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
}
