package ocean.acs.models.dao;

import java.util.Optional;
import ocean.acs.models.data_object.entity.MfaOtpRecordDO;

/**
 * Mfa Otp Record DAO
 *
 * @author Alan Chen
 */
public interface MfaOtpRecordDAO {

    /**
     * Find OTP Record by ID
     *
     * @param otpRecordId OTP Record Id
     * @return OTP Record
     */
    Optional<MfaOtpRecordDO> findById(long otpRecordId);

    /**
     * Save OTP Record
     *
     * @param mfaOtpRecordDO Will save to database MFA OTP record
     * @return Saved MFA OTP record object
     */
    MfaOtpRecordDO save(MfaOtpRecordDO mfaOtpRecordDO);

    /**
     * Find OTP Code
     * <p>Find OTP code by issuer bank id and account name
     *
     * @param issuerBankId Issuer bank id
     * @param account      User account name
     * @return OTP Record
     */
    Optional<MfaOtpRecordDO> findActiveOtpRecordByIssuerBankIdAndAccount(long issuerBankId, String account);

}
