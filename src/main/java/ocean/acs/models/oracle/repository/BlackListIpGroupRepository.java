package ocean.acs.models.oracle.repository;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.BlackListIpGroup;

@Repository
public interface BlackListIpGroupRepository
        extends CrudRepository<BlackListIpGroup, Long>, JpaSpecificationExecutor<BlackListIpGroup> {

    @Query(value = "select b from BlackListIpGroup b where b.id in ?1 and b.deleteFlag=0")
    List<BlackListIpGroup> findByIdIn(List<Long> ids, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "update BlackListIpGroup i "
            + "set i.deleteFlag = 1, i.deleter = :deleter, i.deleteMillis = :deleteMillis "
            + "where i.id = :id")
    void deleteByIds(@Param("id") Long id, @Param("deleter") String deleter,
            @Param("deleteMillis") long now);

    @Query("select i from BlackListIpGroup i where i.issuerBankId = ?1 and i.deleteFlag = 0")
    List<BlackListIpGroup> findByIssuerBankId(Long issuerBankId);

    @Transactional
    @Modifying
    @Query(value = "update BlackListIpGroup m "
            + "set m.transStatus = :transStatus, m.updater = :updater, m.updateMillis = :updateMillis "
            + "where m.issuerBankId = :issuerBankId and m.deleteFlag = 0")
    void updateTransStatusByIssuerBankId(@Param("issuerBankId") Long issuerBankId,
            @Param("transStatus") String transStatus, @Param("updater") String updater,
            @Param("updateMillis") Long updateMillis);
}
