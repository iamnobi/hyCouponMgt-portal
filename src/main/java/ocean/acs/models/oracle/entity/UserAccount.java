package ocean.acs.models.oracle.entity;

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
import ocean.acs.models.data_object.portal.IssuerBankRelativeData;
import ocean.acs.models.data_object.portal.UserAccountDO;
import ocean.acs.models.entity.DBKey;
import ocean.acs.models.entity.OperatorInfo;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

@DynamicUpdate
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = DBKey.TABLE_USER_ACCOUNT)
public class UserAccount extends OperatorInfo implements IssuerBankRelativeData {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "user_account_seq_generator",
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
        @org.hibernate.annotations.Parameter(name = "sequence_name",
          value = "USER_ACCOUNT_ID_SEQ"),
        @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_account_seq_generator")
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
        UserAccount entity = new UserAccount();
        entity.setId(d.getId());
        entity.setPassword(d.getEncryptedPassword());
        entity.setIssuerBankId(d.getIssuerBankId());
        entity.setAccount(d.getAccount());
        entity.setUsername(d.getUsername());
        entity.setDepartment(d.getDepartment());
        entity.setPhone(d.getPhone());
        entity.setExt(d.getExt());
        entity.setEmail(d.getEmail());
        entity.setTryFailCount(d.getTryFailCount());
        entity.setForgetMimaCount(d.getForgetMimaCount());
        entity.setSendOtpCount(d.getSendOtpCount());
        entity.setVerifyOtpCount(d.getVerifyOtpCount());
        entity.setLastLoginMillis(d.getLastLoginMillis());
        entity.setCreator(d.getCreator());
        entity.setCreateMillis(d.getCreateMillis());
        entity.setUpdater(d.getUpdater());
        entity.setUpdateMillis(d.getUpdateMillis());
        entity.setDeleteFlag(d.getDeleteFlag());
        entity.setDeleter(d.getDeleter());
        entity.setDeleteMillis(d.getDeleteMillis());
        entity.setAuditStatus(d.getAuditStatus());
        return entity;
    }
}
