package ocean.acs.models.sql_server.repository;

import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ocean.acs.models.data_object.kernel.WhiteListAttemptPanAuthStatusDO;
import ocean.acs.models.sql_server.entity.WhiteListAttemptSetting;

@Repository
public interface WhiteListAttemptSettingRepository
        extends PagingAndSortingRepository<WhiteListAttemptSetting, Long> {

    @Query("select new ocean.acs.models.data_object.kernel.WhiteListAttemptPanAuthStatusDO"
            + "(a.id, a.permittedTimes, a.permittedQuota, a.attemptExpiredTime , a.currency, a.amount) "
            + "from WhiteListAttemptSetting a where a.panId = :panInfoId and deleteFlag = false")
    Page<WhiteListAttemptPanAuthStatusDO> findByPanId(@Param("panInfoId") Long panInfoId,
            Pageable pageable);

    @Transactional
    @Modifying
    @Query("update WhiteListAttemptSetting a set a.permittedQuota = :permittedQuota,"
            + " a.updater = :updater, a.updateMillis = :updateMillis where a.id = :id")
    void updatePermittedQuota(@Param("permittedQuota") Integer permittedQuota,
            @Param("updater") String updater, @Param("updateMillis") long updateMillis,
            @Param("id") Long whiteListPanAttemptID);

    @Query("select s from PanInfo p join WhiteListAttemptSetting s on p.id = s.panId "
            + "where p.issuerBankId = :issuerBankId and s.panId=:panId and p.deleteFlag=0 and s.deleteFlag=0 ")
    Page<WhiteListAttemptSetting> findByIssuerBankIdAndPanId(
            @Param("issuerBankId") Long issuerBankId, @Param("panId") Long panId,
            Pageable pageable);

    @Query(value = "select sum(tries_permitted) open_auth_sum from white_list_attempt_setting ws "
            + "join pan_info pan on ws.pan_id = pan.id where pan.issuer_bank_id = ?1 and "
            + "ws.create_millis between ?2 and ?3 and ws.delete_flag = 0 and pan.delete_flag = 0",
            nativeQuery = true)
    Integer sumByTriesPermitted(Long issuerBankId, long startMillis, long endMillis);

    @Query(value = "select sum(tries_quota) open_auth_sum from white_list_attempt_setting ws "
            + "join pan_info pan on ws.pan_id = pan.id where pan.issuer_bank_id = ?1 and "
            + "ws.create_millis between ?2 and ?3 and ws.delete_flag = 0 and pan.delete_flag = 0",
            nativeQuery = true)
    Integer sumByTriesQuota(Long issuerBankId, long startMillis, long endMillis);

    @Query("select w from WhiteListAttemptSetting w where panId=?1 and deleteFlag = 0")
    Optional<WhiteListAttemptSetting> findByPanId(Long panId);

}
