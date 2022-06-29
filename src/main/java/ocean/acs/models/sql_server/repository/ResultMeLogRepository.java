package ocean.acs.models.sql_server.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.sql_server.entity.ResultMeLog;

@Repository
public interface ResultMeLogRepository extends CrudRepository<ResultMeLog, Long> {

    @Query(value = "select * from result_me_log where result_log_id = ?1 and message_type = ?2",
            nativeQuery = true)
    List<ResultMeLog> findByResultLogId(Long resultLogId, String messageType);
}
