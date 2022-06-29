package ocean.acs.models.sql_server.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.sql_server.entity.AuthenticationMeLog;

@Repository
public interface AuthenticationMeLogRepository extends CrudRepository<AuthenticationMeLog, Long> {

    @Query(value = "select * from authentication_me_log where authentication_log_id = ?1 and message_type = ?2",
            nativeQuery = true)
    List<AuthenticationMeLog> findByAuthLogId(Long authLogId, String messageType);

}
