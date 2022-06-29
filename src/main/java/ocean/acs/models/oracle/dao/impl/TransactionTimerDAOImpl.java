package ocean.acs.models.oracle.dao.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.enumerator.TimerType;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.dao.TransactionTimerDAO;
import ocean.acs.models.data_object.entity.TransactionTimerDO;
import ocean.acs.models.oracle.entity.TransactionTimer;

@Log4j2
@Repository
@AllArgsConstructor
public class TransactionTimerDAOImpl implements TransactionTimerDAO {

    private final ocean.acs.models.oracle.repository.TransactionTimerRepository repo;

    public boolean existsByTransactionLogIDAndTimerType(Long txLogID, TimerType timerType) throws DatabaseException {
        try {
            Boolean result = repo.existsByTransactionLogIDAndTimerType(txLogID, timerType.getCode());
            return result == null ? false : result;
        } catch (Exception e) {
            log.error(
                    "[existsByTransactionLogIDAndTimerType] system error, txLogID={}, timerType={}",
                    txLogID,
                    timerType,
                    e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public Optional<TransactionTimerDO> findTransactionTimeout(Long txLogID, TimerType timerType)
            throws DatabaseException {
        try {
            return repo.findByTransactionLogIDAndTimerType(txLogID, timerType.getCode())
                    .map(TransactionTimerDO::valueOf);
        } catch (Exception e) {
            log.error("[findTransactionTimeout] unknown exception, txLogID={}, timerType={}",
                    txLogID, timerType, e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public Optional<TransactionTimerDO> findTransactionTimeoutByTxLogID(Long txLogID,
            TimerType timerType) throws DatabaseException {
        try {
            return repo.findByTransactionLogIDAndTimerType(txLogID, timerType.getCode())
                    .map(TransactionTimerDO::valueOf);
        } catch (Exception e) {
            log.error(
                    "[findTransactionTimeoutByTxLogID] unknown exception, txLogID={}, timerType={}",
                    txLogID, timerType, e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public List<TransactionTimerDO> getTransactionTimeouts(TimerType timerType)
            throws DatabaseException {
        long now = System.currentTimeMillis();
        try {
            return repo.findByTimerTypeAndExpireMillisLessThanEqual(timerType.getCode(), now)
                    .stream().map(TransactionTimerDO::valueOf).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[getTransactionTimeouts] unknown exception, timerType={}", timerType, e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public TransactionTimerDO saveTimeoutTimer(TransactionTimerDO transactionTimerDO)
            throws DatabaseException {
        try {
            TransactionTimer transactionTimer = TransactionTimer.valueOf(transactionTimerDO);
            return TransactionTimerDO.valueOf(repo.save(transactionTimer));
        } catch (Exception e) {
            log.error("[saveTimeoutTimer] unknown exception, transactionTimer={}",
                    transactionTimerDO, e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public void updateExpireMillis(Long extendExpireMillis, Long txLogID) throws DatabaseException {
        try {
            repo.updateExpireMillisByTxLogID(extendExpireMillis, txLogID);
        } catch (Exception e) {
            log.error("[updateExpireMillis] unknown exception, extendExpireMillis={}, txLogID={}",
                    extendExpireMillis, txLogID, e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public void deleteTimerByTxLogID(Long txLogID) throws DatabaseException {
        try {
            repo.deleteByTxLogId(txLogID);
        } catch (Exception e) {
            log.error("[deleteTimerByTxLogID] unknown exception, txLogID={}", txLogID, e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public void deleteTimerByTxLogIdAndTimerType(Long txLogID, TimerType timerType)
            throws DatabaseException {
        try {
            repo.deleteByTxLogIdAndTimerType(txLogID, timerType.getCode());
        } catch (Exception e) {
            log.error("[deleteTimerByTxLogID] unknown exception, txLogID={}", txLogID, e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public void deleteTimerList(List<TransactionTimerDO> timerList) throws DatabaseException {
        try {
            List<TransactionTimer> transactionTimerList =
                    timerList.stream().map(TransactionTimer::valueOf).collect(Collectors.toList());
            repo.deleteAll(transactionTimerList);
        } catch (Exception e) {
            log.error("[deleteTimerList] unknown exception, timerList={}", timerList, e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

}
