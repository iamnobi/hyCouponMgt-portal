package ocean.acs.models.sql_server.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ocean.acs.models.data_object.portal.IssuerBankAdminDO;
import ocean.acs.models.data_object.portal.UserAccountDO;
import ocean.acs.models.entity.DBKey;
import ocean.acs.models.entity.OperatorInfo;
import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = DBKey.TABLE_USER_ACCOUNT)
public class UserAccount extends OperatorInfo {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBKey.COL_USER_ACCOUNT_ID)
    private Long id;

    @NonNull
    @Column(name = DBKey.COL_USER_ACCOUNT_ISSUER_BANK_ID)
    private Long issuerBankId;

    @NonNull
    @Column(name = DBKey.COL_USER_ACCOUNT_ACCOUNT)
    private String account;

    @Column(name = "password")
    private String password;

    @Column(name = DBKey.COL_USER_ACCOUNT_CHIP_CARD_NUMBER)
    private String chipCardNumber;

    @NonNull
    @Column(name = DBKey.COL_USER_ACCOUNT_USERNAME)
    private String username;

    @Column(name = DBKey.COL_USER_ACCOUNT_PHONE)
    private String phone;

    @Column(name = DBKey.COL_USER_ACCOUNT_EMAIL)
    private String email;

    @Column(name = DBKey.COL_USER_ACCOUNT_DEPARTMENT)
    private String department;

    @Column(name = DBKey.COL_USER_ACCOUNT_EXT)
    private String ext;

    @Column(name = DBKey.COL_USER_ACCOUNT_TRY_FAIL_COUNT)
    private Integer tryFailCount;

    @Column(name = DBKey.COL_USER_ACCOUNT_FORGET_MIMA_COUNT)
    private Integer forgetMimaCount;

    @Column(name = DBKey.COL_USER_ACCOUNT_SEND_OTP_COUNT)
    private Integer sendOtpCount;

    @Column(name = DBKey.COL_USER_ACCOUNT_VERIFY_OTP_COUNT)
    private Integer verifyOtpCount;

    @Column(name = DBKey.COL_USER_ACCOUNT_LAST_LOGIN_MILLIS)
    private Long lastLoginMillis;

    @Column(name = DBKey.COL_AUDIT_STATUS)
    private String auditStatus;

    public UserAccount(Long id, Long issuerBankId, String account, String password,
      String username, String phone, String email, String department,
      String ext, Integer tryFailCount, Integer forgetMimaCount, Integer sendOtpCount, Integer verifyOtpCount, Long lastLoginMillis,
      String auditStatus, String creator, Long createMillis,
      String updater, Long updateMillis, Boolean deleteFlag, String deleter,
      Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.issuerBankId = issuerBankId;
        this.account = account;
        this.password = password;
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.department = department;
        this.ext = ext;
        this.tryFailCount = tryFailCount;
        this.forgetMimaCount = forgetMimaCount;
        this.sendOtpCount = sendOtpCount;
        this.verifyOtpCount = verifyOtpCount;
        this.lastLoginMillis = lastLoginMillis;
        this.auditStatus = auditStatus;
    }

    public static UserAccount valueOf(UserAccountDO d) {
        return new UserAccount(d.getId(), d.getIssuerBankId(), d.getAccount(),
          d.getEncryptedPassword(),
          d.getUsername(), d.getPhone(), d.getEmail(),
          d.getDepartment(), d.getExt(), d.getTryFailCount(), d.getForgetMimaCount(),
          d.getSendOtpCount(), d.getVerifyOtpCount(),
          d.getLastLoginMillis(), d.getAuditStatus(),
          d.getCreator(), d.getCreateMillis(), d.getUpdater(), d.getUpdateMillis(),
          d.getDeleteFlag(), d.getDeleter(), d.getDeleteMillis());
    }
}
