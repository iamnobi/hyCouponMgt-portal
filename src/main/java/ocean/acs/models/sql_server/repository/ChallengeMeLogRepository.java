package ocean.acs.models.sql_server.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.sql_server.entity.ChallengeMeLog;

@Repository
public interface ChallengeMeLogRepository extends CrudRepository<ChallengeMeLog, Long> {

    @Query(value = "select * from challenge_me_log where challenge_log_id = ?1 and message_type = ?2",
            nativeQuery = true)
    List<ChallengeMeLog> findByChallengeLogId(Long authLogId, String messageType);
}
