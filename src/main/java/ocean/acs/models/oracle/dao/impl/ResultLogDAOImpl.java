package ocean.acs.models.oracle.dao.impl;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.enumerator.TransStatus;
import ocean.acs.commons.enumerator.TransStatusReason;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.dao.ResultLogDAO;
import ocean.acs.models.data_object.entity.ResultLogDO;
import ocean.acs.models.oracle.entity.ResultLog;

@Log4j2
@Repository
@AllArgsConstructor
public class ResultLogDAOImpl implements ResultLogDAO {

    private final ocean.acs.models.oracle.repository.ResultLogRepository repo;

    @Override
    public Boolean isTransactionTimeout(Long resultLogID) throws DatabaseException {
        try {
            String transStatusCode = TransStatus.NotAuthenticated.getCode();
            String transStatusReasonCode =
                    TransStatusReason.TRANSACTION_TIMED_OUT_AT_THE_ACS.getCode();
            return repo.existsByIdAndTransStatusAndTransStatusReason(resultLogID, transStatusCode,
                    transStatusReasonCode);
        } catch (Exception e) {
            log.error("[isTransactionTimeout] unknown exception, resultLogID={}", resultLogID, e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public Optional<ResultLogDO> findById(Long id) {
        try {
            return repo.findById(id).map(ResultLogDO::valueOf);
        } catch (Exception e) {
            log.error("[findById] unknown exception, id={}", id, e);
        }
        return Optional.empty();
    }

    @Override
    public ResultLogDO save(ResultLogDO resultLogDO) throws DatabaseException {
        try {
            ResultLog resultLog = ResultLog.valueOf(resultLogDO);
            return ResultLogDO.valueOf(repo.save(resultLog));
        } catch (Exception e) {
            log.error("[save] unknown exception, resultLog={}", resultLogDO, e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

}
