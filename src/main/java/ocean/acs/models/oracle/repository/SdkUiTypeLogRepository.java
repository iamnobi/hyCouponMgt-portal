package ocean.acs.models.oracle.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.SdkUiTypeLog;

@Repository
public interface SdkUiTypeLogRepository extends CrudRepository<SdkUiTypeLog, Long> {

    @Query(value = "select SDK_UI_TYPE from SDK_UI_TYPE_LOG where AUTHENTICATION_LOG_ID = :authLogId",
            nativeQuery = true)
    List<String> findByAuthLogId(@Param("authLogId") Long authLogId);
}
