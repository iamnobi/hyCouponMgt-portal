package ocean.acs.models.oracle.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.ChallengeLog;

@Repository
public interface ChallengeLogRepository extends CrudRepository<ChallengeLog, Long> {

    @Query(value = "select * from CHALLENGE_LOG where kernel_transaction_log_id = ?1",
            nativeQuery = true)
    List<ChallengeLog> findByTxLogId(Long txLogId);

    @Query(value = "select ch.* from KERNEL_TRANSACTION_LOG tx "
            + "join CHALLENGE_LOG ch on tx.CHALLENGE_LOG_ID = ch.ID "
            + "where tx.ACS_TRANS_ID = :acsTransID", nativeQuery = true)
    Optional<ChallengeLog> findByAcsTransID(@Param("acsTransID") String acsTransID);

    List<ChallengeLog> findByTransactionLogIDOrderByCreateMillisDesc(Long txLogId);

}
