package ocean.acs.models.dao;

import java.util.List;
import java.util.Optional;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.entity.ChallengeViewMessageDO;

public interface ChallengeViewMessageDAO {

    List<ChallengeViewMessageDO> findAll() throws DatabaseException;

    Optional<ChallengeViewMessageDO> findById(Long id);

    List<ChallengeViewMessageDO> findByIssuerBankId(Long issuerBankId);

    List<ChallengeViewMessageDO> findByIssuerBankIdAndLanguageCode(Long issuerBankId, String languageCode);

    ChallengeViewMessageDO saveOrUpdate(ChallengeViewMessageDO challengeViewMessagesDO)
            throws DatabaseException;

    int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis);

}
