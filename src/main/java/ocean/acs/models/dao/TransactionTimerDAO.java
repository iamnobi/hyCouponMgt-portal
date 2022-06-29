package ocean.acs.models.dao;

import java.util.List;
import java.util.Optional;
import ocean.acs.commons.enumerator.TimerType;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.entity.TransactionTimerDO;

public interface TransactionTimerDAO {

    boolean existsByTransactionLogIDAndTimerType(Long txLogID, TimerType timerType) throws DatabaseException;

    Optional<TransactionTimerDO> findTransactionTimeout(Long txLogID, TimerType timerType)
            throws DatabaseException;

    Optional<TransactionTimerDO> findTransactionTimeoutByTxLogID(Long txLogID, TimerType timerType)
            throws DatabaseException;

    List<TransactionTimerDO> getTransactionTimeouts(TimerType timerType) throws DatabaseException;

    TransactionTimerDO saveTimeoutTimer(TransactionTimerDO transactionTimerDO)
            throws DatabaseException;

    void updateExpireMillis(Long extendExpireMillis, Long txLogID) throws DatabaseException;

    void deleteTimerByTxLogID(Long txLogID) throws DatabaseException;

    void deleteTimerByTxLogIdAndTimerType(Long txLogID, TimerType timerType)
            throws DatabaseException;

    void deleteTimerList(List<TransactionTimerDO> timerList) throws DatabaseException;

}
