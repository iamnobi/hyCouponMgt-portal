package ocean.acs.models.oracle.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.MessageType;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.dao.ChallengeMeLogDAO;
import ocean.acs.models.data_object.entity.ChallengeLogDO;
import ocean.acs.models.data_object.entity.ChallengeMeLogDO;
import ocean.acs.models.data_object.kernel.CReqDO;
import ocean.acs.models.data_object.kernel.CResDO;
import ocean.acs.models.data_object.kernel.MessageExtensionDO;
import ocean.acs.models.oracle.entity.ChallengeLog;
import ocean.acs.models.oracle.entity.ChallengeMeLog;

@Log4j2
@Repository
@AllArgsConstructor
public class ChallengeMeLogDAOImpl implements ChallengeMeLogDAO {

    private final ocean.acs.models.oracle.repository.ChallengeMeLogRepository repo;
    private final ObjectMapper objectMapper;

    @Override
    public void save(List<ChallengeMeLogDO> challengeMeLogDOList, MessageType messageType) throws DatabaseException {
        // NOTE: ChallengeMeLog 需要加密儲存，因此沒有用到所以不儲存
//        try {
//            if (challengeMeLogDOList == null || challengeMeLogDOList.isEmpty()) {
//                return;
//            }
//            List<ChallengeMeLog> challengeMeLogs = new ArrayList<>();
//
//            for (ChallengeMeLogDO challengeMeLogDO : challengeMeLogDOList){
//                ChallengeMeLog challengeMeLog = createChallengeMeLog(challengeMeLogDO);
//                challengeMeLog.setMessageType(messageType.name());
//                challengeMeLogs.add(challengeMeLog);
//            }
//            repo.saveAll(challengeMeLogs);
//        } catch (Exception e) {
//            log.error("[saveCReq] unknown exception", e);
//            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
//        }
    }

//    @Override
//    public void saveCRes(ChallengeMeLogDO challengeLogDO) throws DatabaseException {
//        try {
//            List<MessageExtensionDO> messageExtensions = cRes.getMessageExtension();
//            ChallengeLog challengeLog = ChallengeLog.valueOf(challengeLogDO);
//            doSave(challengeLog, messageExtensions, MessageType.CRes);
//        } catch (Exception e) {
//            log.error("[saveCReq] system error, challengeLog={}, CRes={}", challengeLogDO, cRes, e);
//            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
//        }
//    }
//
//    @Override
//    public void saveCReq(ChallengeMeLogDO challengeMeLogDO) throws DatabaseException {
//        try {
//            List<MessageExtensionDO> messageExtensions =
//                    (List<MessageExtensionDO>) creq.getMessageExtension();
//            ChallengeLog challengeLog = ChallengeLog.valueOf(challengeLogDO);
//            doSave(challengeLog, messageExtensions, MessageType.CReq);
//        } catch (Exception e) {
//            log.error("[saveCReq] unknown exception, challengeLog={}, cReqBase={}", challengeLogDO,
//                    creq, e);
//            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
//        }
//    }

//    @Override
//    public void saveCRes(ChallengeMeLogDO challengeLogDO) throws DatabaseException {
//        try {
//            List<MessageExtensionDO> messageExtensions = cRes.getMessageExtension();
//            ChallengeLog challengeLog = ChallengeLog.valueOf(challengeLogDO);
//            doSave(challengeLog, messageExtensions, MessageType.CRes);
//        } catch (Exception e) {
//            log.error("[saveCReq] system error, challengeLog={}, CRes={}", challengeLogDO, cRes, e);
//            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
//        }
//    }

    @Override
    public List<ChallengeMeLogDO> findByChallengeLogId(Long challengeLogId,
            MessageType messageType) {
        try {
            List<ChallengeMeLog> challengeMeLogList =
                    repo.findByChallengeLogId(challengeLogId, messageType.name());
            return challengeMeLogList.stream().map(ChallengeMeLogDO::valueOf)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[findByChallengeLogId] unknown exception, challengeLogId={}, messageType={}",
                    challengeLogId, messageType.name(), e);
            return Collections.emptyList();
        }
    }

}
