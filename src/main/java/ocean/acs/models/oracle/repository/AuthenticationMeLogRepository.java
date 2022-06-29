package ocean.acs.models.oracle.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.AuthenticationMeLog;

@Repository
public interface AuthenticationMeLogRepository extends CrudRepository<AuthenticationMeLog, Long> {

    @Query(value = "select * from AUTHENTICATION_ME_LOG where AUTHENTICATION_LOG_ID=?1 and MESSAGE_TYPE=?2",
            nativeQuery = true)
    List<AuthenticationMeLog> findByAuthLogId(Long authLogId, String messageType);
}
