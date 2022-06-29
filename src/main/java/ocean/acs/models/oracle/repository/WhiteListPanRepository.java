package ocean.acs.models.oracle.repository;

import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.WhiteListPan;

@Repository
public interface WhiteListPanRepository
        extends CrudRepository<WhiteListPan, Long>, JpaSpecificationExecutor<WhiteListPan> {

    @Query("select w.id from WhiteListPan w where w.panId = :panInfoId and w.deleteFlag = false")
    Page<Long> findIdByPanId(@Param("panInfoId") Long panInfoId, Pageable pageable);

    @Transactional
    @Modifying
    @Query(
        value =
            "update WHITE_LIST_PAN b set b.DELETE_FLAG = 1, b.DELETER = :deleter, b.DELETE_MILLIS = :deleteMillis\n"
                + "WHERE exists (select ID from PAN_INFO p where p.ISSUER_BANK_ID = :issuerBankId and b.PAN_ID = p.id and p.delete_flag = 0)",
        nativeQuery = true)
    int deleteByIssuerBankId(
        @Param("issuerBankId") Long issuerBankId,
        @Param("deleter") String deleter,
        @Param("deleteMillis") long deleteMillis);
}
