package ocean.acs.models.sql_server.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ocean.acs.models.sql_server.entity.AuditLog;

@Repository
public interface AuditLogRepository
        extends CrudRepository<AuditLog, Long>, JpaSpecificationExecutor<AuditLog> {

    @Query("select al from AuditLog al where al.id in (:idList)")
    List<AuditLog> findByIds(@Param("idList") List<Long> idList);
}
