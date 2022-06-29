package ocean.acs.models.dao;

import java.util.List;
import ocean.acs.commons.enumerator.MessageType;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.entity.ChallengeLogDO;
import ocean.acs.models.data_object.entity.ChallengeMeLogDO;
import ocean.acs.models.data_object.kernel.CReqDO;
import ocean.acs.models.data_object.kernel.CResDO;

public interface ChallengeMeLogDAO {

    void save(List<ChallengeMeLogDO> challengeMeLogDOList, MessageType messageType) throws DatabaseException;

    List<ChallengeMeLogDO> findByChallengeLogId(Long challengeLogId, MessageType messageType);

}
