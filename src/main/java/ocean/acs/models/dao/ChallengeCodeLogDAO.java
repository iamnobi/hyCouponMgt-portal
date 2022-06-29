package ocean.acs.models.dao;

import java.util.List;
import ocean.acs.commons.enumerator.VerifyStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.entity.ChallengeCodeLogDO;

public interface ChallengeCodeLogDAO {

    ChallengeCodeLogDO save(long challengeLogId, String authCode, String authID,
            VerifyStatus verifyStatus) throws DatabaseException;

    List<ChallengeCodeLogDO> findByChallengeLogId(Long challengeLogId);

}
