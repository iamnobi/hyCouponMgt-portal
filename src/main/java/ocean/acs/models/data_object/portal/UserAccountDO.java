package ocean.acs.models.data_object.portal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ocean.acs.models.data_object.entity.OperatorInfoDO;

/**
 * UserAccountDO
 *
 * @author Alan Chen
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountDO extends OperatorInfoDO {

    private Long id;
    private Long issuerBankId;
    private String account;
    private String encryptedPassword;
    private String username;
    private String phone;
    private String email;
    private String department;
    private String ext;
    private Integer tryFailCount = 0;
    private Integer forgetMimaCount = 0;
    private Integer sendOtpCount = 0;
    private Integer verifyOtpCount = 0;
    private Long lastLoginMillis;
    private String auditStatus;

    public UserAccountDO(Long id, Long issuerBankId, String account, String password,
      String username, String phone, String email, String department,
      String ext, Integer tryFailCount, Integer forgetMimaCount, Integer sendOtpCount, Integer verifyOtpCount, Long lastLoginMillis,
      String auditStatus, String creator, Long createMillis,
      String updater, Long updateMillis, Boolean deleteFlag, String deleter,
      Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.issuerBankId = issuerBankId;
        this.account = account;
        this.encryptedPassword = password;
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

    public static UserAccountDO valueOf(ocean.acs.models.oracle.entity.UserAccount e) {
        UserAccountDO userAccountDo = new UserAccountDO();
        userAccountDo.setId(e.getId());
        userAccountDo.setIssuerBankId(e.getIssuerBankId());
        userAccountDo.setAccount(e.getAccount());
        userAccountDo.setEncryptedPassword(e.getPassword());
        userAccountDo.setUsername(e.getUsername());
        userAccountDo.setPhone(e.getPhone());
        userAccountDo.setEmail(e.getEmail());
        userAccountDo.setDepartment(e.getDepartment());
        userAccountDo.setExt(e.getExt());
        userAccountDo.setTryFailCount(e.getTryFailCount());
        userAccountDo.setForgetMimaCount(e.getForgetMimaCount());
        userAccountDo.setSendOtpCount(e.getSendOtpCount());
        userAccountDo.setVerifyOtpCount(e.getVerifyOtpCount());
        userAccountDo.setLastLoginMillis(e.getLastLoginMillis());
        userAccountDo.setAuditStatus(e.getAuditStatus());
        userAccountDo.setCreator(e.getCreator());
        userAccountDo.setCreateMillis(e.getCreateMillis());
        userAccountDo.setUpdater(e.getUpdater());
        userAccountDo.setUpdateMillis(e.getUpdateMillis());
        userAccountDo.setDeleteFlag(e.getDeleteFlag());
        userAccountDo.setDeleter(e.getDeleter());
        userAccountDo.setDeleteMillis(e.getDeleteMillis());
        return userAccountDo;
    }

    public static UserAccountDO valueOf(ocean.acs.models.sql_server.entity.UserAccount e) {
        UserAccountDO userAccountDo = new UserAccountDO();
        userAccountDo.setId(e.getId());
        userAccountDo.setIssuerBankId(e.getIssuerBankId());
        userAccountDo.setAccount(e.getAccount());
        userAccountDo.setEncryptedPassword(e.getPassword());
        userAccountDo.setUsername(e.getUsername());
        userAccountDo.setPhone(e.getPhone());
        userAccountDo.setEmail(e.getEmail());
        userAccountDo.setDepartment(e.getDepartment());
        userAccountDo.setExt(e.getExt());
        userAccountDo.setTryFailCount(e.getTryFailCount());
        userAccountDo.setForgetMimaCount(e.getForgetMimaCount());
        userAccountDo.setSendOtpCount(e.getSendOtpCount());
        userAccountDo.setVerifyOtpCount(e.getVerifyOtpCount());
        userAccountDo.setLastLoginMillis(e.getLastLoginMillis());
        userAccountDo.setAuditStatus(e.getAuditStatus());
        userAccountDo.setCreator(e.getCreator());
        userAccountDo.setCreateMillis(e.getCreateMillis());
        userAccountDo.setUpdater(e.getUpdater());
        userAccountDo.setUpdateMillis(e.getUpdateMillis());
        userAccountDo.setDeleteFlag(e.getDeleteFlag());
        userAccountDo.setDeleter(e.getDeleter());
        userAccountDo.setDeleteMillis(e.getDeleteMillis());
        return userAccountDo;
    }

    /**
     * For add user account
     *
     * @param d {@link IssuerBankAdminDO}
     * @return
     */
    public static UserAccountDO valueOf(IssuerBankAdminDO d) {
        UserAccountDO a = new UserAccountDO();
        a.setId(d.getId());
        a.setIssuerBankId(d.getIssuerBankId());
        a.setAccount(d.getAccount());
        a.setEncryptedPassword(d.getEncryptedPassword());
        a.setPhone(d.getPhone());
        a.setEmail(d.getEmail());
        a.setDepartment(d.getDepartment());
        a.setExt(d.getExt());
        a.setTryFailCount(0);
        a.setForgetMimaCount(0);
        a.setSendOtpCount(0);
        a.setVerifyOtpCount(0);
        a.setLastLoginMillis(null);
        if (d.getAuditStatus() != null) {
            a.setAuditStatus(d.getAuditStatus().getSymbol());
        }
        a.setCreator(d.getUser());
        a.setCreateMillis(System.currentTimeMillis());
        return a;
    }

}
