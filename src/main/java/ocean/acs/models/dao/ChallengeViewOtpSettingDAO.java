package ocean.acs.models.dao;

import java.util.List;
import java.util.Optional;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.entity.ChallengeViewOtpSettingDO;

public interface ChallengeViewOtpSettingDAO {

    List<ChallengeViewOtpSettingDO> findByNotDeleteAndOrderByUpdateMillisAndCreateMillisDes()
            throws DatabaseException;

    Optional<ChallengeViewOtpSettingDO> findById(Long id);

    List<ChallengeViewOtpSettingDO> findAll();

    Optional<ChallengeViewOtpSettingDO> findByIssuerBankId(Long issuerBankId);

    ChallengeViewOtpSettingDO saveOrUpdate(ChallengeViewOtpSettingDO challengeViewOtpSettingDO)
            throws DatabaseException;

    int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis);

}
