package ocean.acs.models.sql_server.dao.impl;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.models.dao.MfaDAO;
import ocean.acs.models.sql_server.entity.MfaInfo;
import ocean.acs.models.sql_server.repository.MfaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * MfaDAOImpl
 *
 * @author Alan Chen
 */
@Slf4j
@Service
public class MfaDAOImpl implements MfaDAO {

    private final MfaRepository mfaRepository;

    @Autowired
    public MfaDAOImpl(MfaRepository mfaRepository) {
        this.mfaRepository = mfaRepository;
    }

    @Override
    public boolean isRegistered(long issuerBankId, String account) {
        boolean isRegistered = mfaRepository
          .findByIssuerBankIdAndAccount(issuerBankId, account)
          .isPresent();
        log.debug("[MfaDao][isRegistered] isRegistered: {}", isRegistered);
        return isRegistered;
    }

    @Override
    public String findSecretKey(long issuerBankId, String account) {
        Optional<MfaInfo> mfaInfoOptional = mfaRepository
          .findByIssuerBankIdAndAccount(issuerBankId, account);
        if (mfaInfoOptional.isPresent()) {
            return mfaInfoOptional.get().getSecretKey().trim();
        }
        throw new OceanException("SecretKey not found!!");
    }

    @Override
    public void update(long issuerBankId, String account, String secretKey) {
        log.info("[MfaDao][update] Update secret key in database!!");
        Optional<MfaInfo> mfaInfoOptional = mfaRepository
          .findByIssuerBankIdAndAccount(issuerBankId, account);

        MfaInfo mfaInfo = mfaInfoOptional.orElseGet(MfaInfo::new);
        mfaInfo.setIssuerBankId(issuerBankId);
        mfaInfo.setAccount(account);
        mfaInfo.setSecretKey(secretKey);
        mfaRepository.save(mfaInfo);
    }

    @Override
    public void save(long issuerBankId, String account, String secretKey) {
        MfaInfo mfaInfo = new MfaInfo();
        mfaInfo.setIssuerBankId(issuerBankId);
        mfaInfo.setAccount(account);
        mfaInfo.setSecretKey(secretKey);
        mfaRepository.save(mfaInfo);
    }
}

