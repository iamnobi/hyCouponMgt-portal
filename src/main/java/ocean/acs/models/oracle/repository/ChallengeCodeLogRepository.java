package ocean.acs.models.oracle.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.ChallengeCodeLog;

@Repository
public interface ChallengeCodeLogRepository extends CrudRepository<ChallengeCodeLog, Long> {

    @Query(value = "select * from CHALLENGE_CODE_LOG where CHALLENGE_LOG_ID=?1", nativeQuery = true)
    List<ChallengeCodeLog> findByChallengeLogId(Long challengeLogId);
}
