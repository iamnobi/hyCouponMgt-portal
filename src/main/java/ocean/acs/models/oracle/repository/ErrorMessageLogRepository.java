package ocean.acs.models.oracle.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.ErrorMessageLog;

@Repository
public interface ErrorMessageLogRepository extends CrudRepository<ErrorMessageLog, Long> {

}
