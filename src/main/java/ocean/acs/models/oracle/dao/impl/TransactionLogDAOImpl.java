package ocean.acs.models.oracle.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.MessageType;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.dao.TransactionLogDAO;
import ocean.acs.models.data_object.entity.TransactionLogDO;
import ocean.acs.models.oracle.entity.TransactionLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
@AllArgsConstructor
public class TransactionLogDAOImpl implements TransactionLogDAO {

    public static final String LAST_RRES_RESPONSE_TIME_SQL =
            "select RESPONSE_TIME from KERNEL_TRANSACTION_LOG \n"
                    + "where card_brand = :cardBrand and create_millis between :startTimeMillis and :endTimeMillis";

    private final ocean.acs.models.oracle.repository.TransactionLogRepository repo;
    private final NamedParameterJdbcTemplate npJdbcTemplate;

    @Override
    public Boolean existsByThreeDSServerTransID(String threeDSServerTransID)
            throws DatabaseException {
        try {
            return repo.existsByThreeDSServerTransIDAndThreeDSMethodLogIdIsNotNull(
                    threeDSServerTransID);
        } catch (Exception e) {
            log.error("[existsByThreeDSServerTransID] unknown exception", e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public Optional<TransactionLogDO> findByID(Long txLogID) throws DatabaseException {
        try {
            return repo.findById(txLogID).map(TransactionLogDO::valueOf);
        } catch (Exception e) {
            log.error("[findByID] unknown exception, txLogID={}", txLogID, e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public Optional<TransactionLogDO> findByAcsTransID(String acsTransID) throws DatabaseException {
        if (StringUtils.isBlank(acsTransID)) {
            return Optional.empty();
        }

        try {
            return repo.findByAcsTransID(acsTransID).map(TransactionLogDO::valueOf);
        } catch (Exception e) {
            log.error("[findByAcsTransID] unknown exception, acsTransID={}", acsTransID, e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public Optional<TransactionLogDO> findByThreeDSServerTransID(String threeDSServerTransID)
            throws DatabaseException {
        if (StringUtils.isBlank(threeDSServerTransID)) {
            return Optional.empty();
        }

        try {
            return repo.findByThreeDSServerTransID(threeDSServerTransID)
                    .map(TransactionLogDO::valueOf);
        } catch (Exception e) {
            log.error("[findByThreeDSServerTransID] unknown exception", e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public TransactionLogDO save(TransactionLogDO transactionLogDO) throws DatabaseException {
        try {
            TransactionLog transactionLog = TransactionLog.valueOf(transactionLogDO);
            return TransactionLogDO.valueOf(repo.save(transactionLog));
        } catch (Exception e) {
            log.error("[save] unknown exception, txLog={}", transactionLogDO, e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public TransactionLogDO saveChallengeFailure(TransactionLogDO transactionLogDO,
            ResultStatus resultStatus) throws DatabaseException {
        try {
            TransactionLog transactionLog = TransactionLog.valueOf(transactionLogDO);
            transactionLog.setChallengeCompleted(false);
            transactionLog.setChallengeErrorReasonCode(resultStatus.getCode());
            transactionLog.setSysUpdater(MessageType.CRes.name());
            transactionLog = repo.save(transactionLog);
            return TransactionLogDO.valueOf(transactionLog);
        } catch (Exception e) {
            log.error("[saveChallengeFailure] unknown exception, txLog={}, resultStatus={}",
                    transactionLogDO, resultStatus.name(), e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public void updateThreeDsMethodCollect(String mresTransStatus, String threeDSServerTransID,
            Long ddcaLogId) throws DatabaseException {
        try {
            repo.updateMResStatus(mresTransStatus, ddcaLogId, MessageType.ThreeDSMethod.name(),
                    System.currentTimeMillis(), threeDSServerTransID);
        } catch (Exception e) {
            log.error("[updateForChallengeRequest] unknown exception, mresTransStatus={}",
                    mresTransStatus, e);
            log.error(e.getMessage(), e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public void updateForChallengeRequest(String otpPhoneNumberMask, Long updateMillis,
            String acsTransID) throws DatabaseException {
        try {
            repo.updateForChallengeRequest(otpPhoneNumberMask, updateMillis, acsTransID);
        } catch (Exception e) {
            log.error(
                    "[updateForChallengeRequest] unknown exception, otpPhoneNumberMask={}, updateMillis={}, acsTransID={}",
                    otpPhoneNumberMask, updateMillis, acsTransID, e);
            log.error(e.getMessage(), e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

      @Override
      public void updateNeedResendRReq(
          Long id, boolean needResendRReq, Long updateMillis, String sysUpdater)
          throws DatabaseException {
        try {
          repo.updateNeedResendRReq(id, needResendRReq, updateMillis, sysUpdater);
        } catch (Exception e) {
          log.error("[updateNeedResendRReq] unknown exception", e);
          throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
      }

      @Override
      public void increaseResendRReqCount(Long id, Long updateMillis, String sysUpdater)
          throws DatabaseException {
        try {
          repo.increaseResendRReqCount(id, updateMillis, sysUpdater);
        } catch (Exception e) {
          log.error("[increaseResendRReqCount] unknown exception", e);
          throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
      }

      @Override
      public List<TransactionLogDO> findNeedResendRReqTransactionListInOneDay(Long maxResendRReqTime)
          throws DatabaseException {
        try {
          List<TransactionLog> transactionLogList;
          if (maxResendRReqTime == -1L) {
            // -1 -> 無限, 不加 ResendRReqTime 查詢條件
            transactionLogList =
                repo.findByNeedResendRReqIsTrueAndCreateMillisAfter(
                    System.currentTimeMillis() - 24 * 60 * 60 * 1000);
          } else {
            transactionLogList =
                repo.findByNeedResendRReqIsTrueAndCreateMillisAfterAndResendRReqTimeLessThan(
                    System.currentTimeMillis() - 24 * 60 * 60 * 1000, maxResendRReqTime);
          }
          return transactionLogList.stream()
              .map(TransactionLogDO::valueOf)
              .collect(Collectors.toList());
        } catch (Exception e) {
          log.error("[findNeedResendRReqTransactionListInOneDay] unknown exception", e);
          throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
      }

    @Override
    public Integer countByAcctNumberHashAndAresResultReasonCodeIn(String acctNumberHash,
            List<Integer> aresReasonCodeList) {
        return repo.countByAcctNumberHashAndAresResultReasonCodeIn(acctNumberHash,
                aresReasonCodeList);
    }

    @Override
    public Optional<TransactionLogDO> findById(Long id) {
        return repo.findById(id).map(TransactionLogDO::valueOf);
    }

    @Override
    public List<TransactionLogDO> findByIds(List<Long> ids) {
        return repo.findByIdIn(ids).stream().map(TransactionLogDO::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    public Double statisticsRResResponseTime(String cardBrand, long startTimeMillis,
            long endTimeMillis) {
        Map<String, Object> params = new HashMap<>();
        params.put("cardBrand", cardBrand);
        params.put("startTimeMillis", startTimeMillis);
        params.put("endTimeMillis", endTimeMillis);

        List<Long> resultList = npJdbcTemplate.query(LAST_RRES_RESPONSE_TIME_SQL, params,
          (rs, rowNum) -> rs.getLong("RESPONSE_TIME"));
        if (resultList.isEmpty()) {
            return 0.0;
        }
        resultList = resultList.stream().filter(Objects::nonNull).collect(Collectors.toList());
        long total = resultList.size();
        long sum = resultList.stream().mapToLong(Long::longValue).sum();
        return (double) sum / total;
    }

    @Override
    public Integer countByBlackListIpGroupIdAndAresResultReasonCodeIn(long ipGroupId,
      List<Integer> reasonCodeList) {
        return repo.countByBlackListIpGroupIdAndAresResultReasonCodeIn(ipGroupId, reasonCodeList);
    }

}
