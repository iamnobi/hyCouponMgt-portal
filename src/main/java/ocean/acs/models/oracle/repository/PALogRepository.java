package ocean.acs.models.oracle.repository;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import ocean.acs.models.oracle.entity.PALog;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PALogRepository
    extends CrudRepository<PALog, String>, JpaSpecificationExecutor<PALog> {

  boolean existsByVeLogId(String verificationEnrollLogId);

  Optional<PALog> findByVeLogId(String verificationEnrollLogId);

  @Transactional
  @Modifying
  @Query(
      "update PALog log "
          + "set log.challengeCompleted = true, "
          + "    log.challengeErrorReasonCode = :challengeErrorReasonCode, "
          + "    log.txStatus = 'N',"
          + "    log.updateMillis = :updateMillis, "
          + "    log.sysUpdater = :sysUpdater "
          + "where log.challengeCompleted = false and"
          + "      log.createMillis < :timeoutMillis")
  int updateTimeoutTransactions(
      @Param("timeoutMillis") long timeoutMillis,
      @Param("challengeErrorReasonCode") int challengeErrorReasonCode,
      @Param("updateMillis") long updateMillis,
      @Param("sysUpdater") String sysUpdater);

  List<PALog> findByChallengeCompletedFalseAndCreateMillisLessThan(long timeoutMillis);

  @Query(
      value =
          "select * from PA_LOG a "
              + "where a.PAN_INFO_ID = :panInfoId and a.create_millis >:createMillis "
              + "order by a.create_millis desc",
      nativeQuery = true)
  List<PALog> findByPanInfoIdWithinMillis(@Param("panInfoId") String panInfoId, @Param("createMillis") Long createMillis);

  @Query(
      value =
          "select a.CREATE_MILLIS from PA_LOG a "
              + "where a.PAN_INFO_ID = :panInfoId and a.TX_STATUS = 'Y' and rownum = 1 "
              + "order by a.create_millis desc",
      nativeQuery = true)
  Long findLatestSuccessMillisByPanInfoId(@Param("panInfoId") String panInfoId);
}
