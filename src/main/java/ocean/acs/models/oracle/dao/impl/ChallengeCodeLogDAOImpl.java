package ocean.acs.models.oracle.dao.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.MessageType;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.enumerator.VerifyStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.dao.ChallengeCodeLogDAO;
import ocean.acs.models.data_object.entity.ChallengeCodeLogDO;
import ocean.acs.models.oracle.entity.ChallengeCodeLog;

@Log4j2
@Repository
@AllArgsConstructor
public class ChallengeCodeLogDAOImpl implements ChallengeCodeLogDAO {

    private final ocean.acs.models.oracle.repository.ChallengeCodeLogRepository repo;

    @Override
    public ChallengeCodeLogDO save(long challengeLogId, String authCode, String authID,
            VerifyStatus verifyStatus) throws DatabaseException {
        ChallengeCodeLog challengeCodeLog = new ChallengeCodeLog();
        challengeCodeLog.setChallengeLogID(challengeLogId);
        challengeCodeLog.setAuthID(authID == null ? " " : authID);
        challengeCodeLog.setSysCreator(MessageType.CReq.name());
        try {
            challengeCodeLog.setVerifyCode(authCode);
            challengeCodeLog.setVerifyStatus(verifyStatus.getCode());
            challengeCodeLog = repo.save(challengeCodeLog);
            return ChallengeCodeLogDO.valueOf(challengeCodeLog);
        } catch (Exception e) {
            log.error(
                    "[save] unknown exception, challengeLogId={}, authCode={}, authID={}, verifyStatus={}",
                    challengeLogId, authCode, authID, verifyStatus, e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public List<ChallengeCodeLogDO> findByChallengeLogId(Long challengeLogId) {
        try {
            return repo.findByChallengeLogId(challengeLogId).stream()
                    .map(ChallengeCodeLogDO::valueOf).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[findByChallengeLogId] unknown exception, challengeLogId={}", challengeLogId,
                    e);
            return Collections.emptyList();
        }
    }

}
