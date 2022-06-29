package ocean.acs.models.dao;

import java.util.Optional;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.entity.DdcaLogDO;

public interface DdcaLogDAO {

    Optional<DdcaLogDO> findByID(Long ddcaLogID) throws DatabaseException;

    Optional<DdcaLogDO> findLatestByPanInfoId(Long panInfoId, Long currentTransactionLogId) throws DatabaseException;

    DdcaLogDO save(DdcaLogDO ddcaLogDO) throws DatabaseException;

    long findLatestResponseTime() throws DatabaseException;

    Optional<DdcaLogDO> findLatestOne() throws DatabaseException;

    Optional<DdcaLogDO> findById(Long ddcaLogId);

}
