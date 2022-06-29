package ocean.acs.models.oracle.repository;

import java.util.Optional;
import ocean.acs.models.oracle.entity.VELog;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VELogRepository
    extends CrudRepository<VELog, String>, JpaSpecificationExecutor<VELog> {

  Optional<VELog> findByChAcctIdAndCreateMillisIsAfter(String chAcctId, long createMillisBefore);
}
