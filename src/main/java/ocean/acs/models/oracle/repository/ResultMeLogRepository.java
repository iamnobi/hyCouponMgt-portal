package ocean.acs.models.oracle.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.ResultMeLog;

@Repository
public interface ResultMeLogRepository extends CrudRepository<ResultMeLog, Long> {

    @Query(value = "select * from RESULT_ME_LOG where RESULT_LOG_ID=?1 and MESSAGE_TYPE=?2",
            nativeQuery = true)
    List<ResultMeLog> findByResultLogId(Long resultLogId, String messageType);
}
