package ocean.acs.models.sql_server.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.sql_server.entity.ChallengeCodeLog;

@Repository
public interface ChallengeCodeLogRepository extends CrudRepository<ChallengeCodeLog, Long> {

    @Query(value = "select * from challenge_code_log where challenge_log_id = ?1",
            nativeQuery = true)
    List<ChallengeCodeLog> findByChallengeLogId(Long challengeLogId);
}
