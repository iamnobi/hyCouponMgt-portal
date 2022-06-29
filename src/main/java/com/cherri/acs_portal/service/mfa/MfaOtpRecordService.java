package com.cherri.acs_portal.service.mfa;

import ocean.acs.models.dao.MfaOtpRecordDAO;
import ocean.acs.models.data_object.entity.MfaOtpRecordDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * MfaOtpRecordService
 *
 * @author Alan Chen
 */
@Service
public class MfaOtpRecordService {

    private final MfaOtpRecordDAO mfaOtpRecordDAO;

    @Autowired
    public MfaOtpRecordService(MfaOtpRecordDAO mfaOtpRecordDAO) {
        this.mfaOtpRecordDAO = mfaOtpRecordDAO;
    }

    public void save(long issuerBankId, String account, String otp) {
        MfaOtpRecordDO d = new MfaOtpRecordDO();
        d.setIssuerBankId(issuerBankId);
        d.setAccount(account);
        d.setOtp(otp);
        d.setCreateMillis(System.currentTimeMillis());
        d.setIsActive(true);
        mfaOtpRecordDAO.save(d);
    }
}
