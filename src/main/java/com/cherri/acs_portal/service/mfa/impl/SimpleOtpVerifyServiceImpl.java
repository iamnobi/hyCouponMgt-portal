package com.cherri.acs_portal.service.mfa.impl;

import com.cherri.acs_portal.dto.mfa.MfaVerifyRequestDto;
import com.cherri.acs_portal.service.mfa.MfaVerifyService;
import java.util.Optional;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import ocean.acs.models.dao.MfaOtpRecordDAO;
import ocean.acs.models.data_object.entity.MfaOtpRecordDO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * SimpleOtpVerifyServiceImpl
 *
 * @author Alan Chen
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "mfa.system", havingValue = "SIMPLE_OTP", matchIfMissing = true)
public class SimpleOtpVerifyServiceImpl implements MfaVerifyService {

    private final int expiredSecond;
    private final MfaOtpRecordDAO mfaOtpRecordDAO;

    @Autowired
    public SimpleOtpVerifyServiceImpl(
      @Value("${mfa.otp.code.expired.second: 900}") int expiredSecond, MfaOtpRecordDAO mfaOtpRecordDAO) {
        this.expiredSecond = expiredSecond;
        this.mfaOtpRecordDAO = mfaOtpRecordDAO;
    }

    @PostConstruct
    public void postConstructor() {
        log.info("MFA System: {}", this.getClass().getSimpleName());
    }

    @Override
    public Boolean verify(MfaVerifyRequestDto requestDto) {
        Optional<MfaOtpRecordDO> otpRecordOptional = findActiveOtpRecordFromDatabase(
          requestDto.getIssuerBankId(), requestDto.getAccount()
        );

        if (!otpRecordOptional.isPresent()) {
            log.warn("[verify] Active OTP record not found from database.");
            return false;
        }
        /* Has OTP Record */
        MfaOtpRecordDO otpRecord = otpRecordOptional.get();

        String dbOtp = otpRecord.getOtp();
        Long expiredTime = otpRecord.getCreateMillis() + getEffectiveTime();
        Long now = System.currentTimeMillis();

        /* Compare Expire Time */
        log.debug("[verify] Current Time: {}, Expire Time: {}", now, expiredTime);
        if (now > expiredTime) {
            log.warn("[verify] This OTP is expired or not active. OTP: {}", dbOtp);
            updateOtpRecord(otpRecord);
            return false;
        }

        /* Compare OTP Code */
        String userOtp = requestDto.getAuthenticationCode();
        log.debug("[verify] UserOTP: {}, DatabaseOTP: {}",
                StringUtils.normalizeSpace(userOtp), StringUtils.normalizeSpace(dbOtp)
        );

        if (userOtp.equals(dbOtp)) {
            updateOtpRecord(otpRecord);
            return true;
        }
        return false;
    }

    private int getEffectiveTime() {
        final int oneSecond = 1000;
        int effectiveTime = oneSecond * expiredSecond;
        log.debug("[getEffectiveTime] OTP effective time is {} sec.", expiredSecond);
        return effectiveTime;
    }

    private Optional<MfaOtpRecordDO> findActiveOtpRecordFromDatabase(long issuerBankId, String account) {
        return mfaOtpRecordDAO.findActiveOtpRecordByIssuerBankIdAndAccount(issuerBankId, account);
    }

    private void updateOtpRecord(MfaOtpRecordDO otpRecord) {
        otpRecord.setIsActive(false);
        mfaOtpRecordDAO.save(otpRecord);
        log.debug("[updateOtpRecord] Update OTP Record active status to false. id: {}, OTP:{}",
          otpRecord.getId(), otpRecord.getOtp());
    }

}
