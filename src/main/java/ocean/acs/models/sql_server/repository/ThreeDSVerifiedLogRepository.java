package ocean.acs.models.sql_server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.sql_server.entity.ThreeDSVerifiedOperationLog;

@Repository
public interface ThreeDSVerifiedLogRepository
        extends CrudRepository<ThreeDSVerifiedOperationLog, Long> {

    @Query("select o from ThreeDSVerifiedOperationLog o where o.panId = ?1 and o.issuerBankId = ?2 and o.deleteFlag=0")
    Page<ThreeDSVerifiedOperationLog> findByPanIdAndIssuerBankId(Long panId, Long issuerBankId,
            Pageable pageable);
}
