package ocean.acs.models.oracle.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.AuditLog;

@Repository
public interface AuditLogRepository
        extends CrudRepository<AuditLog, Long>, JpaSpecificationExecutor<AuditLog> {

    @Query(value = "select * from audit_log where id in :idList", nativeQuery = true)
    List<AuditLog> findByIds(@Param("idList") List<Long> idList);
}
