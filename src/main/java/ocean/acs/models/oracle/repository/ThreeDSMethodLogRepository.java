package ocean.acs.models.oracle.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.ThreeDSMethodLog;

@Repository
public interface ThreeDSMethodLogRepository extends CrudRepository<ThreeDSMethodLog, Long> {

    @Query(value = "select td.* from KERNEL_TRANSACTION_LOG tx "
            + "join THREE_D_S_METHOD_LOG td on tx.THREE_D_S_METHOD_LOG_ID = td.ID "
            + "where tx.THREE_D_S_SERVER_TRANS_ID = ?1", nativeQuery = true)
    Optional<ThreeDSMethodLog> findByThreeDSServerTransID(String threeDSServerTransID);
}
