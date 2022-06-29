package ocean.acs.models.dao;

import java.util.List;
import java.util.Optional;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.entity.TransactionLogDO;

public interface TransactionLogDAO {

    Boolean existsByThreeDSServerTransID(String threeDSServerTransID) throws DatabaseException;

    Optional<TransactionLogDO> findByID(Long txLogID) throws DatabaseException;

    Optional<TransactionLogDO> findByAcsTransID(String acsTransID) throws DatabaseException;

    Optional<TransactionLogDO> findByThreeDSServerTransID(String threeDSServerTransID)
            throws DatabaseException;

    TransactionLogDO save(TransactionLogDO transactionLogDO) throws DatabaseException;

    TransactionLogDO saveChallengeFailure(TransactionLogDO transactionLogDO, ResultStatus resultStatus)
            throws DatabaseException;

    void updateThreeDsMethodCollect(String mresTransStatus, String threeDSServerTransID,
            Long ddcaLogId) throws DatabaseException;

    void updateForChallengeRequest(String otpPhoneNumberMask, Long updateMillis, String acsTransID)
            throws DatabaseException;

    void updateNeedResendRReq(Long id, boolean needResendRReq, Long updateMillis,
        String sysUpdater) throws DatabaseException;

    void increaseResendRReqCount(Long id, Long updateMillis, String sysUpdater) throws DatabaseException;

    List<TransactionLogDO> findNeedResendRReqTransactionListInOneDay(Long maxResendRReqTime) throws DatabaseException;

    Integer countByAcctNumberHashAndAresResultReasonCodeIn(String acctNumberHash,
      List<Integer> aresReasonCodeList);

    Optional<TransactionLogDO> findById(Long id);

    List<TransactionLogDO> findByIds(List<Long> ids);

    Double statisticsRResResponseTime(String cardBrand, long startTimeMillis,
      long endTimeMillis);

    Integer countByBlackListIpGroupIdAndAresResultReasonCodeIn(long ipGroupId,
      List<Integer> reasonCodeList);
}
