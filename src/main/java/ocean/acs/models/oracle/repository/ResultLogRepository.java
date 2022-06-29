package ocean.acs.models.oracle.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.ResultLog;

@Repository
public interface ResultLogRepository extends CrudRepository<ResultLog, Long> {

    Boolean existsByIdAndTransStatusAndTransStatusReason(Long resultLogID, String transStatus,
            String transStatusReason);
}
