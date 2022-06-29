package ocean.acs.models.sql_server.repository;

import ocean.acs.models.sql_server.entity.PanInfo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface PanInfoRepository extends CrudRepository<PanInfo, Long> {

    @Query("select p from PanInfo p where cardNumberHash = ?1 and deleteFlag=0 order by updateMillis, createMillis desc")
    List<PanInfo> findByCardNumberHash(String cardNumberHash);

    List<PanInfo> findByIssuerBankIdAndCardNumberHashAndDeleteFlagOrderByUpdateMillisDescCreateMillisDesc(
            Long issuerBankId, String cardNumberHash, boolean flag);

    @Query(value = "select count(id) from pan_info where id = ?1 and issuer_bank_id = ?2 and delete_flag = 0",
            nativeQuery = true)
    int countsByIdAndIssuerBankId(Long id, Long issuerBankId);

    Optional<PanInfo> findByIdAndDeleteFlagFalse(Long id);

    @Transactional
    @Modifying
    @Query(
        "update PanInfo p\n"
            + "set p.previousTransactionSuccess  = :transactionSuccess,\n"
            + "    p.previousTransactionDeviceId = :deviceId,\n"
            + "    p.updater                     = :updater,\n"
            + "    p.updateMillis                = :updateMillis\n"
            + "where p.id = :id")
    void updatePreviousTransactionInfo(
        @Param("id") Long id,
        @Param("transactionSuccess") boolean transactionSuccess,
        @Param("deviceId") String deviceId,
        @Param("updater") String updater,
        @Param("updateMillis") long updateMillis);

    @Transactional
    @Modifying
    @Query(
        "update PanInfo p\n"
            + "set p.previousTransactionSuccess = true,\n"
            + "    p.updater                    = :updater,\n"
            + "    p.updateMillis               = :updateMillis\n"
            + "where p.id = :id")
    void updatePreviousTransactionInfoSuccess(
        @Param("id") Long id,
        @Param("updater") String updater,
        @Param("updateMillis") long updateMillis);

    @Transactional
    @Modifying
    @Query(
            "update PanInfo p\n"
                    + "set p.cardStatus = :cardStatus,\n"
                    + "    p.updater                    = :updater,\n"
                    + "    p.updateMillis               = :updateMillis\n"
                    + "where p.id = :id")
    void updateCardStatus(
            @Param("id") Long id,
            @Param("cardStatus") String cardStatus,
            @Param("updater") String updater,
            @Param("updateMillis") long updateMillis);
}
