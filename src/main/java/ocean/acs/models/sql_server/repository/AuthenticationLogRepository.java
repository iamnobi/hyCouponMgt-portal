package ocean.acs.models.sql_server.repository;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ocean.acs.models.sql_server.entity.AuthenticationLog;

@Repository
public interface AuthenticationLogRepository extends CrudRepository<AuthenticationLog, Long> {

    @Query("select count(al) from AuthenticationLog al where al.createMillis between :startTimeMillis and :endTimeMillis")
    Long countByCreateMillis(@Param("startTimeMillis") Long startTimeMillis,
            @Param("endTimeMillis") Long endTimeMillis);

    Optional<AuthenticationLog> findFirstByIssuerBankIdAndAcquirerMerchantIDOrderByCreateMillisDesc(
            Long issuerBankId, String merchantId);

    @Transactional
    @Modifying
    @Query("update AuthenticationLog set cardExpiryDate = null where id = ?1")
    void cleanCardExpireDateById(Long id);

    @Query(value = "select count(a.id) from authentication_log a join (\n" +
                   "    select t.authentication_log_id, t.create_millis from kernel_transaction_log t where t.pan_info_id = :panInfoId\n" + 
                   ") tcard on tcard.authentication_log_id = a.id where tcard.create_millis > :createMillis",
            nativeQuery = true)
    Integer countByPanInfoIdWithinMillis(@Param("panInfoId") Long panInfoId,
                                                         @Param("createMillis") Long createMillis);

    @Query(value = "select top 1 create_millis from (\n" + 
                   "    select tcard.create_millis, a.trans_status from (\n" + 
                   "        select t.authentication_log_id , t.create_millis from kernel_transaction_log t where t.pan_info_id = :panInfoId\n" + 
                   "    ) tcard inner join authentication_log a on tcard.authentication_log_id = a.id where a.trans_status = 'Y' \n" + 
                   "    union all\n" + 
                   "    select tcard.create_millis, r.trans_status from (\n" + 
                   "        select t.result_log_id , t.create_millis from kernel_transaction_log t where t.pan_info_id = :panInfoId\n" + 
                   "    ) tcard inner join result_log r on tcard.result_log_id = r.id where r.trans_status = 'Y'\n" + 
                   ") uniontable order by create_millis desc", nativeQuery = true)
    Long findLatestSuccessMillisByPanInfoId(@Param("panInfoId") Long panInfoId);

}
