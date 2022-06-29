package ocean.acs.models.oracle.dao.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.MessageType;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.dao.AuthenticationMeLogDAO;
import ocean.acs.models.data_object.entity.AuthenticationMeLogDO;
import ocean.acs.models.oracle.entity.AuthenticationMeLog;

@Log4j2
@Repository
@AllArgsConstructor
public class AuthenticationMeLogDAOImpl implements AuthenticationMeLogDAO {

    private final ocean.acs.models.oracle.repository.AuthenticationMeLogRepository repo;

    @Override
    public List<AuthenticationMeLogDO> findByAuthLogId(Long authLogId, MessageType messageType) {
        try {
            List<AuthenticationMeLog> authMeLogs =
                    repo.findByAuthLogId(authLogId, messageType.name());
            return authMeLogs.stream().map(AuthenticationMeLogDO::valueOf)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[findByAuthLogId] unknown exception, authLogId={}, messageType={}",
                    authLogId, messageType.name(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<AuthenticationMeLogDO> saveAll(List<AuthenticationMeLogDO> authMeLogDOs)
            throws DatabaseException {
        try {
            List<AuthenticationMeLog> entities = authMeLogDOs.stream()
                    .map(AuthenticationMeLog::valueOf).collect(Collectors.toList());

            Iterable<AuthenticationMeLog> iter = repo.saveAll(entities);
            return StreamSupport.stream(iter.spliterator(), false)
                    .map(AuthenticationMeLogDO::valueOf).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[saveAll] system error, meLogs={}", authMeLogDOs, e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

}
