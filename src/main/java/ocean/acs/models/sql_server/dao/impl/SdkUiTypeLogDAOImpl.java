package ocean.acs.models.sql_server.dao.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.dao.SdkUiTypeLogDAO;
import ocean.acs.models.data_object.entity.SdkUiTypeLogDO;
import ocean.acs.models.sql_server.entity.SdkUiTypeLog;

@Log4j2
@Repository
@AllArgsConstructor
public class SdkUiTypeLogDAOImpl implements SdkUiTypeLogDAO {

    private final ocean.acs.models.sql_server.repository.SdkUiTypeLogRepository repo;

    @Override
    public List<SdkUiTypeLogDO> saveAll(List<SdkUiTypeLogDO> sdkUiTypeLogDoList)
            throws DatabaseException {
        try {
            List<SdkUiTypeLog> sdkUiTypeLogList = sdkUiTypeLogDoList.stream()
                    .map(SdkUiTypeLog::valueOf).collect(Collectors.toList());
            Iterable<SdkUiTypeLog> sdkUiTypeLogIter = repo.saveAll(sdkUiTypeLogList);
            return StreamSupport.stream(sdkUiTypeLogIter.spliterator(), false)
                    .map(SdkUiTypeLogDO::valueOf).collect(Collectors.toList());

        } catch (Exception e) {
            log.error("[saveAll] unknown exception, sdkUiTypeLogs={}", sdkUiTypeLogDoList, e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public List<String> findByAuthLogId(Long authLogId) {
        try {
            return repo.findByAuthLogId(authLogId);
        } catch (Exception e) {
            log.error("[findByAuthLogId] unknown exception, authLogId={}", authLogId, e);
        }
        return Collections.emptyList();
    }

}
