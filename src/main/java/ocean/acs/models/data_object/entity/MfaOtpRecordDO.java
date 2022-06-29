package ocean.acs.models.data_object.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Mfa Otp Record DO
 *
 * @author Alan Chen
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MfaOtpRecordDO {

    private Long id;
    /**
     * 銀行代碼
     */
    private Long issuerBankId;

    /**
     * 帳號
     */
    private String account;

    /**
     * OTP Code
     */
    private String otp;

    /**
     * 建立時間
     */
    private Long createMillis;

    /**
     * 是否生效
     */
    private Boolean isActive;

    public static MfaOtpRecordDO valueOf(ocean.acs.models.oracle.entity.MfaOtpRecord e) {
        MfaOtpRecordDO d = new MfaOtpRecordDO();
        d.setId(e.getId());
        d.setIssuerBankId(e.getIssuerBankId());
        d.setAccount(e.getAccount());
        d.setOtp(e.getOtp());
        d.setCreateMillis(e.getCreateMillis());
        return d;
    }

    public static MfaOtpRecordDO valueOf(ocean.acs.models.sql_server.entity.MfaOtpRecord e) {
        MfaOtpRecordDO d = new MfaOtpRecordDO();
        d.setId(e.getId());
        d.setIssuerBankId(e.getIssuerBankId());
        d.setAccount(e.getAccount());
        d.setOtp(e.getOtp());
        d.setCreateMillis(e.getCreateMillis());
        return d;
    }
}
