package ocean.acs.models.dao;

import java.util.List;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.entity.ChallengeSelectInfoLogDO;

public interface ChallengeSelectInfoLogDAO {

    List<ChallengeSelectInfoLogDO> saveAll(List<ChallengeSelectInfoLogDO> list)
            throws DatabaseException;

    List<ChallengeSelectInfoLogDO> findByChallengeLogId(Long challengeLogId);

}
