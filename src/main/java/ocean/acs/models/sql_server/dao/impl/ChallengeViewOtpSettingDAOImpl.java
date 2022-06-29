package ocean.acs.models.sql_server.dao.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.dao.ChallengeViewOtpSettingDAO;
import ocean.acs.models.data_object.entity.ChallengeViewOtpSettingDO;
import ocean.acs.models.sql_server.entity.ChallengeViewOtpSetting;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Log4j2
@Repository
@AllArgsConstructor
public class ChallengeViewOtpSettingDAOImpl implements ChallengeViewOtpSettingDAO {

    private final ocean.acs.models.sql_server.repository.ChallengeViewOtpSettingRepository repo;

    @Override
    public List<ChallengeViewOtpSettingDO> findByNotDeleteAndOrderByUpdateMillisAndCreateMillisDes()
            throws DatabaseException {
        try {
            List<ChallengeViewOtpSetting> ChallengeViewOtpSettingList =
                    repo.findByNotDeleteAndOrderByUpdateMillisAndCreateMillisDes();
            return ChallengeViewOtpSettingList.stream().map(ChallengeViewOtpSettingDO::valueOf)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[FindByNotDeleteAndOrderByUpdateMillisAndCreateMillisDes] unknown exception",
                    e);
            log.error(e.getMessage(), e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public Optional<ChallengeViewOtpSettingDO> findById(Long id) {
        return repo.findById(id).map(ChallengeViewOtpSettingDO::valueOf);
    }

    @Override
    public List<ChallengeViewOtpSettingDO> findAll() {
        Iterable<ChallengeViewOtpSetting> challengeViewOtpSettingIter = repo.findAll();
        return StreamSupport.stream(challengeViewOtpSettingIter.spliterator(), false)
                .map(ChallengeViewOtpSettingDO::valueOf).collect(Collectors.toList());
    }

    @Override
    public Optional<ChallengeViewOtpSettingDO> findByIssuerBankId(Long issuerBankId) {
        return repo.findByIssuerBankIdAndDeleteFlagFalse(issuerBankId).map(ChallengeViewOtpSettingDO::valueOf);
    }

    @Override
    public ChallengeViewOtpSettingDO saveOrUpdate(
            ChallengeViewOtpSettingDO challengeViewOtpSettingDO) throws DatabaseException {
        try {
            ChallengeViewOtpSetting challengeViewOtpSetting =
                    ChallengeViewOtpSetting.valueOf(challengeViewOtpSettingDO);
            return ChallengeViewOtpSettingDO.valueOf(repo.save(challengeViewOtpSetting));
        } catch (Exception e) {
            log.error("[saveOrUpdate] unknown exception ", e);
            throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis) {
        //TODO Implement method
        return 0;
    }

}
