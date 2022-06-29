package ocean.acs.models.oracle.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.ChallengeSelectInfoLog;

@Repository
public interface ChallengeSelectInfoLogRepository
        extends CrudRepository<ChallengeSelectInfoLog, Long> {

    @Query(value = "select * from CHALLENGE_SELECT_INFO_LOG where CHALLENGE_LOG_ID=?1",
            nativeQuery = true)
    List<ChallengeSelectInfoLog> findByChallengeLogId(Long challengeLogId);
}
