package ocean.acs.models.oracle.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.ChallengeMeLog;

@Repository
public interface ChallengeMeLogRepository extends CrudRepository<ChallengeMeLog, Long> {

    @Query(value = "select * from CHALLENGE_ME_LOG where CHALLENGE_LOG_ID=?1 and MESSAGE_TYPE=?2",
            nativeQuery = true)
    List<ChallengeMeLog> findByChallengeLogId(Long authLogId, String messageType);
}
