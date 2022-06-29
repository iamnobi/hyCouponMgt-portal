package ocean.acs.models.sql_server.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ocean.acs.models.sql_server.entity.DdcaLog;

@Repository
public interface DdcaLogRepository extends JpaRepository<DdcaLog, Long> {

    @Query(value = "select top 1 d.* from ddca_log d join kernel_transaction_log t on t.ddca_log_id = d.id "
            + "where t.pan_info_id = :panInfoId and t.id != :currentTransactionLogId order by t.create_millis desc",
            nativeQuery = true)
    Optional<DdcaLog> findLatestByPanInfoId(@Param("panInfoId") Long panInfoId,
            @Param("currentTransactionLogId") Long currentTransactionLogId);

    Optional<DdcaLog> findFirstByOrderByCreateMillisDesc();

}
