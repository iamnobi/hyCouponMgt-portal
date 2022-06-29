package ocean.acs.models.dao;

import java.util.List;
import ocean.acs.commons.enumerator.MessageType;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.entity.ResultMeLogDO;

public interface ResultMeLogDAO {

    List<ResultMeLogDO> findByResultLogId(Long resultLogId, MessageType messageType);
    List<ResultMeLogDO> saveAll(List<ResultMeLogDO> resultMeLogDOList) throws DatabaseException;
}
