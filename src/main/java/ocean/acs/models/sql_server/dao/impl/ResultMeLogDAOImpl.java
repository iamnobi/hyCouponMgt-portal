package ocean.acs.models.sql_server.dao.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.sql_server.entity.ResultMeLog;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.MessageType;
import ocean.acs.models.dao.ResultMeLogDAO;
import ocean.acs.models.data_object.entity.ResultMeLogDO;

@Log4j2
@Repository
@AllArgsConstructor
public class ResultMeLogDAOImpl implements ResultMeLogDAO {

    private final ocean.acs.models.sql_server.repository.ResultMeLogRepository repo;

    @Override
    public List<ResultMeLogDO> findByResultLogId(Long resultLogId, MessageType messageType) {
        try {
            return repo.findByResultLogId(resultLogId, messageType.name()).stream()
                    .map(ResultMeLogDO::valueOf).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[findByResultLogId] unknown exception, resultLogId={}, messageType={}",
                    resultLogId, messageType.name(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<ResultMeLogDO> saveAll(List<ResultMeLogDO> resultMeLogDOList)
        throws DatabaseException {
        try {
            List<ResultMeLog> entities = resultMeLogDOList.stream()
                .map(ResultMeLog::valueOf).collect(Collectors.toList());

            Iterable<ResultMeLog> iter = repo.saveAll(entities);
            return StreamSupport.stream(iter.spliterator(), false)
                .map(ResultMeLogDO::valueOf).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[saveAll] system error, meLogs={}", resultMeLogDOList, e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

}
