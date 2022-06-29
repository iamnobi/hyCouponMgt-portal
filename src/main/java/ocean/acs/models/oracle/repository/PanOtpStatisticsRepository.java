package ocean.acs.models.oracle.repository;

import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.PanOtpStatistics;

@Repository
public interface PanOtpStatisticsRepository extends CrudRepository<PanOtpStatistics, Long> {

    @Query("select p from PanOtpStatistics p where p.panInfoId=?1 and deleteFlag=0")
    Optional<PanOtpStatistics> findByPanInfoId(Long panInfoId);

    @Transactional
    @Modifying
    @Query("update PanOtpStatistics p set p.verifyOtpCount = p.verifyOtpCount + 1, p.updater = :updater, p.updateMillis = :updateMillis where p.panInfoId = :panInfoId")
    void updateVerifyOtpCount(@Param("panInfoId") Long panInfoId, @Param("updater") String updater,
            @Param("updateMillis") Long updateMillis);

    @Transactional
    @Modifying
    @Query("update PanOtpStatistics p set p.verifyOtpCount = 0, p.updater = :updater, p.updateMillis = :updateMillis where p.panInfoId = :panInfoId")
    void initVerifyOtpCount(@Param("panInfoId") Long panInfoId, @Param("updater") String updater,
            @Param("updateMillis") Long updateMillis);

    @Query("select case when count(p.id)> 0 then true else false end from PanOtpStatistics p where p.panInfoId = ?1 and verifyOtpCount >= ?2 and deleteFlag = 0")
    boolean isOtpLock(Long panInfoId, int maxOtpVerifyCount);

    boolean existsByPanInfoIdAndDeleteFlagFalse(Long panInfoId);

    @Transactional
    @Modifying
    @Query(
        value =
            "update PAN_OTP_STATISTICS b set b.DELETE_FLAG = 1, b.DELETER = :deleter, b.DELETE_MILLIS = :deleteMillis\n"
                + "WHERE exists (select ID from PAN_INFO p where p.ISSUER_BANK_ID = :issuerBankId and b.PAN_INFO_ID = p.id and p.delete_flag = 0)",
        nativeQuery = true)
    int deleteByIssuerBankId(
        @Param("issuerBankId") Long issuerBankId,
        @Param("deleter") String deleter,
        @Param("deleteMillis") long deleteMillis);

}
