package ocean.acs.models.oracle.dao.impl;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.dao.ThreeDSMethodLogDAO;
import ocean.acs.models.data_object.entity.ThreeDSMethodLogDO;
import ocean.acs.models.oracle.entity.ThreeDSMethodLog;

@Log4j2
@Repository
@AllArgsConstructor
public class ThreeDSMethodLogDAOImpl implements ThreeDSMethodLogDAO {

    private final ocean.acs.models.oracle.repository.ThreeDSMethodLogRepository repo;

    @Override
    public Optional<ThreeDSMethodLogDO> findByThreeDSServerTransID(String threeDSServerTransID)
            throws DatabaseException {
        try {
            return repo.findByThreeDSServerTransID(threeDSServerTransID)
                    .map(ThreeDSMethodLogDO::valueOf);
        } catch (Exception e) {
            log.error("[findByThreeDSServerTransID] unknown exception", e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public ThreeDSMethodLogDO save(String threeDSServerTransID,
            ThreeDSMethodLogDO threeDSMethodLogDO) throws DatabaseException {
        try {
            ThreeDSMethodLog threeDSMethodLog = ThreeDSMethodLog.valueOf(threeDSMethodLogDO);
            return ThreeDSMethodLogDO.valueOf(repo.save(threeDSMethodLog));
        } catch (Exception e) {
            log.error("[save] unknown exception, threeDSMethodLog={}", threeDSMethodLogDO, e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public Optional<ThreeDSMethodLogDO> findById(Long id) {
        try {
            return repo.findById(id).map(ThreeDSMethodLogDO::valueOf);
        } catch (Exception e) {
            log.error("[findById] unknown exception, id={}", id, e);
            return Optional.empty();
        }
    }

}
