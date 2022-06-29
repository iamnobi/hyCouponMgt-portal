package ocean.acs.models.sql_server.repository;

import java.util.Optional;
import ocean.acs.models.sql_server.entity.MimaRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * MimaRecordRepository
 *
 * @author Alan Chen
 */
public interface MimaRecordRepository extends JpaRepository<MimaRecord, Long>,
  JpaSpecificationExecutor<MimaRecord> {

    Optional<MimaRecord> findTop1ByIssuerBankIdAndAccountOrderByCreateTimeDesc(
      long issuerBankId, String account);

}
