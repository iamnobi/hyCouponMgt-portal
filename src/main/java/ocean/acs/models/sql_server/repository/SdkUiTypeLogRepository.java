package ocean.acs.models.sql_server.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ocean.acs.models.sql_server.entity.SdkUiTypeLog;

@Repository
public interface SdkUiTypeLogRepository extends CrudRepository<SdkUiTypeLog, Long> {

    @Query(value = "select sdk_ui_type from sdk_ui_type_log where authentication_log_id = :authLogId",
            nativeQuery = true)
    List<String> findByAuthLogId(@Param("authLogId") Long authLogId);
}
