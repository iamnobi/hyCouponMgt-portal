package ocean.acs.models.sql_server.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.sql_server.entity.ErrorMessageLog;

@Repository
public interface ErrorMessageLogRepository extends CrudRepository<ErrorMessageLog, Long> {

}
