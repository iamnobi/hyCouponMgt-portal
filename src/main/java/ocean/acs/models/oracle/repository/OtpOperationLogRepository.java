package ocean.acs.models.oracle.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.OtpOperationLog;

@Repository
public interface OtpOperationLogRepository extends CrudRepository<OtpOperationLog, Long> {

    @Query("select o from OtpOperationLog o where o.panId = ?1 and o.issuerBankId = ?2 and o.deleteFlag = 0")
    Page<OtpOperationLog> findByPanIdAndIssuerBankId(Long panId, Long IssuerBankId,
            Pageable pageable);
}
