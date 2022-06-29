package ocean.acs.models.sql_server.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ocean.acs.models.sql_server.entity.ChallengeLog;

@Repository
public interface ChallengeLogRepository extends CrudRepository<ChallengeLog, Long> {

    @Query(value = "select * from challenge_log where kernel_transaction_log_id = ?1",
            nativeQuery = true)
    List<ChallengeLog> findByTxLogId(Long txLogId);

    @Query(value = "select ch.* from kernel_transaction_log tx join challenge_log ch on tx.challenge_log_id = ch.id "
            + "where tx.acs_trans_id = :acsTransID", nativeQuery = true)
    Optional<ChallengeLog> findByAcsTransID(@Param("acsTransID") String acsTransID);

    List<ChallengeLog> findByTransactionLogIDOrderByCreateMillisDesc(Long txLogId);

}
