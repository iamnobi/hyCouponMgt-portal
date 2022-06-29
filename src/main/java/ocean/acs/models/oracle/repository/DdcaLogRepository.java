package ocean.acs.models.oracle.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.DdcaLog;

@Repository
public interface DdcaLogRepository extends JpaRepository<DdcaLog, Long> {

    @Query(value = "select * from (select * from DDCA_LOG d join KERNEL_Transaction_Log t on t.ddca_log_id = d.id\n"
            + "   where t.pan_Info_Id=:panInfoId and t.ID != :currentTransactionLogId\n"
            + "   order by t.create_millis desc) where rownum = 1",
            nativeQuery = true)
    Optional<DdcaLog> findLatestByPanInfoId(@Param("panInfoId") Long panInfoId, @Param("currentTransactionLogId") Long currentTransactionLogId);

    Optional<DdcaLog> findFirstByOrderByCreateMillisDesc();

}
