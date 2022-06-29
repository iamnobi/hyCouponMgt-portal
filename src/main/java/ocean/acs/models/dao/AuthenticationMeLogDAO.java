package ocean.acs.models.dao;

import java.util.List;
import ocean.acs.commons.enumerator.MessageType;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.entity.AuthenticationMeLogDO;

public interface AuthenticationMeLogDAO {

    List<AuthenticationMeLogDO> findByAuthLogId(Long authLogId, MessageType messageType);

    List<AuthenticationMeLogDO> saveAll(List<AuthenticationMeLogDO> authMeLogDOs) throws DatabaseException;

}
