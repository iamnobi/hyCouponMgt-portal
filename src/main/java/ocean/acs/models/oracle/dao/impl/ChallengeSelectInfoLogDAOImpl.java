package ocean.acs.models.oracle.dao.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.dao.ChallengeSelectInfoLogDAO;
import ocean.acs.models.data_object.entity.ChallengeSelectInfoLogDO;
import ocean.acs.models.oracle.entity.ChallengeSelectInfoLog;

@Log4j2
@Repository
@AllArgsConstructor
public class ChallengeSelectInfoLogDAOImpl implements ChallengeSelectInfoLogDAO {

    private final ocean.acs.models.oracle.repository.ChallengeSelectInfoLogRepository repo;

    @Override
    public List<ChallengeSelectInfoLogDO> saveAll(List<ChallengeSelectInfoLogDO> list)
            throws DatabaseException {
        try {
            List<ChallengeSelectInfoLog> challengeSelectInfoLogList =
                    list.stream().map(ChallengeSelectInfoLog::valueOf).collect(Collectors.toList());
            Iterable<ChallengeSelectInfoLog> challengeSelectInfoLogIter =
                    repo.saveAll(challengeSelectInfoLogList);
            return StreamSupport.stream(challengeSelectInfoLogIter.spliterator(), false)
                    .map(ChallengeSelectInfoLogDO::valueOf).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[saveAll] unknown exception", e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public List<ChallengeSelectInfoLogDO> findByChallengeLogId(Long challengeLogId) {
        try {
            List<ChallengeSelectInfoLog> challengeSelectInfoLogList =
                    repo.findByChallengeLogId(challengeLogId);
            return challengeSelectInfoLogList.stream().map(ChallengeSelectInfoLogDO::valueOf)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[findByChallengeLogId] unknown exception, challengeLogId={}", challengeLogId,
                    e);
        }
        return Collections.emptyList();
    }

}
