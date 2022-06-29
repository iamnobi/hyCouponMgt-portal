package ocean.acs.models.dao;

import java.util.Optional;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.entity.ThreeDSMethodLogDO;

public interface ThreeDSMethodLogDAO {

    Optional<ThreeDSMethodLogDO> findByThreeDSServerTransID(String threeDSServerTransID)
            throws DatabaseException;

    ThreeDSMethodLogDO save(String threeDSServerTransID, ThreeDSMethodLogDO threeDSMethodLogDO)
            throws DatabaseException;

    Optional<ThreeDSMethodLogDO> findById(Long id);

}
