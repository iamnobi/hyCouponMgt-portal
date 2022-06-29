package ocean.acs.models.sql_server.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.models.data_object.entity.MfaOtpRecordDO;
import ocean.acs.models.entity.DBKey;

/**
 * Mfa Otp Record
 *
 * @author Alan Chen
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = DBKey.TABLE_MFA_OTP_RECORD, indexes = {
  @Index(name = "idx_mfa_otp_record_1", columnList = "issuer_bank_id, account")
})
public class MfaOtpRecord {

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBKey.COL_MFA_OTP_RECORD_ID)
    private Long id;

    /**
     * 銀行代碼
     */
    @Column(name = DBKey.COL_MFA_OTP_RECORD_ISSUER_BANK_ID)
    private Long issuerBankId;

    /**
     * 帳號
     */
    @Column(name = DBKey.COL_MFA_OTP_RECORD_ACCOUNT)
    private String account;

    /**
     * OTP Code
     */
    @Column(name = DBKey.COL_MFA_OTP_RECORD_OTP)
    private String otp;

    /**
     * 建立時間
     */
    @Column(name = DBKey.COL_MFA_OTP_RECORD_CREATE_MILLIS)
    private Long createMillis;

    /**
     * 是否生效
     */
    @Column(name = DBKey.COL_MFA_OTP_RECORD_IS_ACTIVE)
    private Boolean isActive;

    /**
     * Convert do to entity
     *
     * @param d Data Object
     * @return Entity
     */
    public static MfaOtpRecord valueOf(MfaOtpRecordDO d) {
        MfaOtpRecord e = new MfaOtpRecord();
        if (d.getId() != null) {
            e.setId(d.getId());
        }
        e.setIssuerBankId(d.getIssuerBankId());
        e.setAccount(d.getAccount());
        e.setOtp(d.getOtp());
        e.setCreateMillis(d.getCreateMillis());
        e.setIsActive(d.getIsActive());
        return e;
    }
}
