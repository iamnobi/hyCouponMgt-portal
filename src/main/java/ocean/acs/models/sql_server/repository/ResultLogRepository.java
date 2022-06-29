package ocean.acs.models.sql_server.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.sql_server.entity.ResultLog;

@Repository
public interface ResultLogRepository extends CrudRepository<ResultLog, Long> {

    Boolean existsByIdAndTransStatusAndTransStatusReason(Long resultLogID, String transStatus,
            String transStatusReason);
}
