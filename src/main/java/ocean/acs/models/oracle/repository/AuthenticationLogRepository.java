package ocean.acs.models.oracle.repository;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.AuthenticationLog;

@Repository
public interface AuthenticationLogRepository extends CrudRepository<AuthenticationLog, Long> {

    @Query(value = "select count(id) from AUTHENTICATION_LOG where create_millis between ?1 and ?2",
            nativeQuery = true)
    Long countByCreateMillis(Long startTimeMillis, Long endTimeMillis);

    Optional<AuthenticationLog> findFirstByIssuerBankIdAndAcquirerMerchantIDOrderByCreateMillisDesc(
            Long issuerBankId, String merchantId);

    @Transactional
    @Modifying
    @Query("update AuthenticationLog set cardExpiryDate = null where id = ?1")
    void cleanCardExpireDateById(Long id);

    @Query(value = 
            " select count(a.id) from Authentication_Log a join \n" +
            "     (\n" + 
            "         select t.authentication_log_id, t.create_millis from Kernel_Transaction_Log t  \n" + 
            "         where t.PAN_INFO_ID = :panInfoId\n" + 
            "     ) tcard\n" + 
            " on tcard.authentication_log_id = a.id where tcard.create_millis >:createMillis order by a.create_millis desc",
            nativeQuery = true)
    Integer countByPanInfoIdWithinMillis(@Param("panInfoId") Long panInfoId,
                                                         @Param("createMillis") Long createMillis);

    @Query(value = 
            "        select create_millis from(\n" + 
            "             select create_millis from (\n" + 
            "                     (\n" + 
            "                         select tcard.create_millis, a.trans_status from \n" + 
            "                            (\n" + 
            "                              select t.authentication_log_id , t.create_millis from Kernel_Transaction_Log t\n" + 
            "                              where  t.PAN_INFO_ID =:panInfoId\n" + 
            "                            ) tcard inner join Authentication_Log a on tcard.authentication_log_id = a.id\n" + 
            "                         where  a.trans_status ='Y' \n" + 
            "                     ) \n" + 
            "                     \n" + 
            "                    union all\n" + 
            "                    \n" + 
            "                     (\n" + 
            "                       select tcard.create_millis, r.trans_status from \n" + 
            "                            (\n" + 
            "                              select t.result_log_id , t.create_millis from Kernel_Transaction_Log t\n" + 
            "                              where  t.PAN_INFO_ID =:panInfoId\n" + 
            "                            ) tcard inner join RESULT_LOG r on tcard.result_log_id = r.id \n" + 
            "                         where  r.trans_status ='Y'    \n" + 
            "                     ) \n" + 
            "            ) order by create_millis desc \n" + 
            "        ) where rownum =1\n", nativeQuery = true)
    Long findLatestSuccessMillisByPanInfoId(@Param("panInfoId") Long panInfoId);

}
