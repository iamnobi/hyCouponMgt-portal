package ocean.acs.models.dao;

import java.util.Optional;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.entity.ResultLogDO;

public interface ResultLogDAO {

    Boolean isTransactionTimeout(Long resultLogID) throws DatabaseException;

    Optional<ResultLogDO> findById(Long id);

    ResultLogDO save(ResultLogDO resultLogDO) throws DatabaseException;

}
