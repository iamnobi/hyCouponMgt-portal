package ocean.acs.models.oracle.dao.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.dao.ChallengeLogDAO;
import ocean.acs.models.data_object.entity.ChallengeLogDO;
import ocean.acs.models.oracle.entity.ChallengeLog;

@Log4j2
@Repository
@AllArgsConstructor
public class ChallengeLogDAOImpl implements ChallengeLogDAO {

    private final ocean.acs.models.oracle.repository.ChallengeLogRepository repo;

    @Override
    public List<ChallengeLogDO> findByTxlogId(Long txLogId) {
        try {
            List<ChallengeLog> challengeLogList = repo.findByTxLogId(txLogId);
            return challengeLogList.stream().map(ChallengeLogDO::valueOf)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[findByTxlogId] unknown exception, txLogId={}", txLogId, e);
        }
        return Collections.emptyList();
    }

    @Override
    public ChallengeLogDO saveChallenge(ChallengeLogDO challengeLogDO) throws DatabaseException {
        try {
            ChallengeLog challengeLog = ChallengeLog.valueOf(challengeLogDO);
            challengeLog = repo.save(challengeLog);
            return ChallengeLogDO.valueOf(challengeLog);
        } catch (Exception e) {
            log.error("[saveChallenge] unknown exception, challengeLog={}", challengeLogDO, e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public List<ChallengeLogDO> findByTransactionLogID(Long txLogId) throws DatabaseException {
        try {
            List<ChallengeLog> challengeLogList =
                    repo.findByTransactionLogIDOrderByCreateMillisDesc(txLogId);
            return challengeLogList.stream().map(ChallengeLogDO::valueOf)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[findByTransactionLogID] unknown exception, txLogId={}", txLogId, e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

}
