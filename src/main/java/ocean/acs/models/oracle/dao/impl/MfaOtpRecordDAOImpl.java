package ocean.acs.models.oracle.dao.impl;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import ocean.acs.models.dao.MfaOtpRecordDAO;
import ocean.acs.models.data_object.entity.MfaOtpRecordDO;
import ocean.acs.models.oracle.entity.MfaOtpRecord;
import ocean.acs.models.oracle.repository.MfaOtpRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * MfaOtpRecordDAOImpl
 *
 * @author Alan Chen
 */
@Slf4j
@Service
public class MfaOtpRecordDAOImpl implements MfaOtpRecordDAO {

    private final MfaOtpRecordRepository repository;

    @Autowired
    public MfaOtpRecordDAOImpl(MfaOtpRecordRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<MfaOtpRecordDO> findById(long otpRecordId) {
        return repository.findById(otpRecordId).map(MfaOtpRecordDO::valueOf);
    }

    @Override
    public MfaOtpRecordDO save(MfaOtpRecordDO mfaOtpRecordDO) {
        MfaOtpRecord e = repository.save(MfaOtpRecord.valueOf(mfaOtpRecordDO));
        return MfaOtpRecordDO.valueOf(e);
    }

    @Override
    public Optional<MfaOtpRecordDO> findActiveOtpRecordByIssuerBankIdAndAccount(long issuerBankId, String account) {
        Optional<MfaOtpRecord> mfaOtpRecordOptional = repository
          .findTop1ByIssuerBankIdAndAccountAndIsActiveTrueOrderByCreateMillisDesc(issuerBankId, account);
        return mfaOtpRecordOptional.map(MfaOtpRecordDO::valueOf);
    }
}
