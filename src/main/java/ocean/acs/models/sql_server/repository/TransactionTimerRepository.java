package ocean.acs.models.sql_server.repository;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ocean.acs.models.sql_server.entity.TransactionTimer;

@Repository
public interface TransactionTimerRepository extends CrudRepository<TransactionTimer, Long> {

    Boolean existsByTransactionLogIDAndTimerType(Long txLogId, Integer timerType);

    Optional<TransactionTimer> findByTransactionLogIDAndTimerType(Long txLogId, Integer timerTypeCode);

    List<TransactionTimer> findByTimerTypeAndExpireMillisLessThanEqual(Integer timerTypeCode, Long now);

    @Transactional
    @Modifying
    @Query("update from TransactionTimer set expireMillis = :expireMillis where transactionLogID = :transactionLogID")
    void updateExpireMillisByTxLogID(@Param("expireMillis") Long expireMillis,
            @Param("transactionLogID") Long txLogId);

    @Transactional
    @Modifying
    @Query("delete from TransactionTimer t where t.transactionLogID = :transactionLogID")
    void deleteByTxLogId(@Param("transactionLogID") Long txLogId);

    @Transactional
    @Modifying
    @Query("delete from TransactionTimer t where t.transactionLogID = ?1 and t.timerType = ?2")
    void deleteByTxLogIdAndTimerType(Long txLogId, Integer timerTypeCode);

}
