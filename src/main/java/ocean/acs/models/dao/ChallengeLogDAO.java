package ocean.acs.models.dao;

import java.util.List;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.entity.ChallengeLogDO;

public interface ChallengeLogDAO {

    List<ChallengeLogDO> findByTxlogId(Long txLogId);

    ChallengeLogDO saveChallenge(ChallengeLogDO challengeLogDO) throws DatabaseException;

    List<ChallengeLogDO> findByTransactionLogID(Long txLogId) throws DatabaseException;

}
