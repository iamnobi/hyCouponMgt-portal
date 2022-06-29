package ocean.acs.models.oracle.dao.impl;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.dao.DdcaLogDAO;
import ocean.acs.models.data_object.entity.DdcaLogDO;
import ocean.acs.models.oracle.entity.DdcaLog;

@Log4j2
@Repository
@AllArgsConstructor
public class DdcaLogDAOImpl implements DdcaLogDAO {

    private final ocean.acs.models.oracle.repository.DdcaLogRepository repo;

    @Override
    public Optional<DdcaLogDO> findByID(Long ddcaLogID) throws DatabaseException {
        try {
            return repo.findById(ddcaLogID).map(DdcaLogDO::valueOf);
        } catch (Exception e) {
            log.error("[findByID] system error, ddcaLogID={}", ddcaLogID);
            log.error(e.getMessage(), e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public Optional<DdcaLogDO> findLatestByPanInfoId(Long panInfoId, Long currentTransactionLogId) throws DatabaseException {
        try {
            return repo.findLatestByPanInfoId(panInfoId, currentTransactionLogId).map(DdcaLogDO::valueOf);
        } catch (Exception e) {
            log.error("[findLatestByPanInfoId] system error, panInfoId={}", panInfoId);
            log.error(e.getMessage(), e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public DdcaLogDO save(DdcaLogDO ddcaLogDO) throws DatabaseException {
        try {
            DdcaLog ddcaLog = DdcaLog.valueOf(ddcaLogDO);
            return DdcaLogDO.valueOf(repo.save(ddcaLog));
        } catch (Exception e) {
            log.error("[DdcaLog] save error, ddcaLog={}", ddcaLogDO);
            log.error(e.getMessage(), e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public long findLatestResponseTime() throws DatabaseException {
        return findLatestOne().map(e -> e.getResponseTime()).orElse(0L);
    }

    @Override
    public Optional<DdcaLogDO> findLatestOne() throws DatabaseException {
        try {
            return repo.findFirstByOrderByCreateMillisDesc().map(DdcaLogDO::valueOf);
        } catch (Exception e) {
            log.error("[findLatestOne] error message={}", e.getMessage());
            throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public Optional<DdcaLogDO> findById(Long ddcaLogId) {
        try {
            return repo.findById(ddcaLogId).map(DdcaLogDO::valueOf);
        } catch (Exception e) {
            log.error("[DdcaLogDao] findById error, request params:id={}", ddcaLogId, e);
        }
        return Optional.empty();
    }

}
