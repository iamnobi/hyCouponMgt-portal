package ocean.acs.models.sql_server.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ocean.acs.models.sql_server.entity.TransactionLog;

@Repository
public interface TransactionLogRepository extends CrudRepository<TransactionLog, Long> {

    Optional<TransactionLog> findByThreeDSServerTransID(String threeDSServerTransID);

    Optional<TransactionLog> findByAcsTransID(String acsTransID);

    Boolean existsByThreeDSServerTransIDAndThreeDSMethodLogIdIsNotNull(String threeDSServerTransID);

    @Transactional
    @Modifying
    @Query("update TransactionLog txLog set " + "txLog.mresTransStatus = :mresTransStatus, "
            + "txLog.ddcaLogId = :ddcaLogId, " + "txLog.sysUpdater = :updater, "
            + "txLog.updateMillis = :updateMillis "
            + "where threeDSServerTransID = :threeDSServerTransID")
    void updateMResStatus(@Param("mresTransStatus") String mresTransStatus,
            @Param("ddcaLogId") Long ddcaLogId, @Param("updater") String updater,
            @Param("updateMillis") Long updateMillis,
            @Param("threeDSServerTransID") String threeDSServerTransID);

    @Transactional
    @Modifying
    @Query("update TransactionLog txLog set " + "txLog.otpPhoneNumber = :otpPhoneNumberMask, "
            + "txLog.updateMillis = :updateMillis " + "where txLog.acsTransID = :acsTransID")
    void updateForChallengeRequest(@Param("otpPhoneNumberMask") String otpPhoneNumberMask,
            @Param("updateMillis") Long updateMillis, @Param("acsTransID") String acsTransID);

    @Transactional
    @Modifying
    @Query(
        "update TransactionLog txLog set "
            + "txLog.needResendRReq = :needResendRReq, "
            + "txLog.updateMillis = :updateMillis, "
            + "txLog.sysUpdater = :sysUpdater "
            + "where txLog.id = :id")
    void updateNeedResendRReq(
        @Param("id") Long id,
        @Param("needResendRReq") boolean needResendRReq,
        @Param("updateMillis") Long updateMillis,
        @Param("sysUpdater") String sysUpdater);

    @Transactional
    @Modifying
    @Query(
        "update TransactionLog txLog set "
            + "txLog.resendRReqTime = txLog.resendRReqTime + 1, "
            + "txLog.updateMillis = :updateMillis, "
            + "txLog.sysUpdater = :sysUpdater "
            + "where txLog.id = :id")
    void increaseResendRReqCount(
        @Param("id") Long id,
        @Param("updateMillis") Long updateMillis,
        @Param("sysUpdater") String sysUpdater);

    List<TransactionLog> findByNeedResendRReqIsTrueAndCreateMillisAfterAndResendRReqTimeLessThan(
        Long createMillis, Long maxResendRReqTime);
    List<TransactionLog> findByNeedResendRReqIsTrueAndCreateMillisAfter(Long createMillis);

    Optional<TransactionLog> findById(Long id);

    List<TransactionLog> findByIdIn(List<Long> ids);

    @Query(value = "select count(tx.id) from kernel_transaction_log tx\n"
            + "join pan_info pan on tx.pan_info_id = pan.id\n"
            + "where pan.card_number_hash = ?1 and tx.ares_result_reason_code in (?2)",
            nativeQuery = true)
    Integer countByAcctNumberHashAndAresResultReasonCodeIn(String acctNumberHash,
            List<Integer> reasonCodeList);

    Integer countByDeviceIDAndAresResultReasonCodeIn(String deviceId, List<Integer> reasonCodeList);

    Integer countByBlackListIpGroupIdAndAresResultReasonCodeIn(Long blackListIpGroupId,
            List<Integer> reasonCodeList);

}
