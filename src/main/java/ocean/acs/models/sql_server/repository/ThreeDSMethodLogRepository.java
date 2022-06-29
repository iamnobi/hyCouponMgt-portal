package ocean.acs.models.sql_server.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.sql_server.entity.ThreeDSMethodLog;

@Repository
public interface ThreeDSMethodLogRepository extends CrudRepository<ThreeDSMethodLog, Long> {

    @Query(value = "select td.* from kernel_transaction_log tx join three_d_s_method_log td "
            + "on tx.three_d_s_method_log_id = td.id where tx.three_d_s_server_trans_id = ?1",
            nativeQuery = true)
    Optional<ThreeDSMethodLog> findByThreeDSServerTransID(String threeDSServerTransID);
}
