package ocean.acs.models.sql_server.repository;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ocean.acs.models.data_object.kernel.KernelBlackListMerchantUrlDO;
import ocean.acs.models.sql_server.entity.BlackListMerchantUrl;

@Repository
public interface BlackListMerchantUrlRepository extends CrudRepository<BlackListMerchantUrl, Long>,
        JpaSpecificationExecutor<BlackListMerchantUrl> {

    @Query("select new ocean.acs.models.data_object.kernel.KernelBlackListMerchantUrlDO(b.url, b.transStatus) "
            + "from BlackListMerchantUrl b where b.issuerBankId = ?1 and b.deleteFlag=false")
    List<KernelBlackListMerchantUrlDO> findAllByIssuerBankID(Long issuerBankID);

    @Query("select b from BlackListMerchantUrl b where b.id in ?1 and b.deleteFlag=0")
    List<BlackListMerchantUrl> findByIdIn(List<Long> ids, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update BlackListMerchantUrl m set m.deleteFlag = 1, m.deleter = :deleter, m.deleteMillis = :deleteMillis where m.id in :id")
    void deleteById(@Param("id") Long id, @Param("deleter") String deleter,
            @Param("deleteMillis") long now);

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM BlackListMerchantUrl m WHERE m.issuerBankId = ?1 and m.url = ?2 and deleteFlag=0")
    boolean isUrlExist(Long issuerBankId, String url);


    @Transactional
    @Modifying
    @Query("update BlackListMerchantUrl m set m.transStatus = :transStatus, m.updater = :updater, m.updateMillis = :updateMillis "
            + "where m.issuerBankId = :issuerBankId and m.deleteFlag = 0")
    void updateTransStatusByIssuerBankId(@Param("issuerBankId") Long issuerBankId,
            @Param("transStatus") String transStatus, @Param("updater") String updater,
            @Param("updateMillis") Long updateMillis);
}
