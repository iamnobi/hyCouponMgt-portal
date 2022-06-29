package ocean.acs.models.sql_server.repository;

import java.util.Optional;
import ocean.acs.models.sql_server.entity.MfaOtpRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * MfaOtpRecordRepository
 *
 * @author Alan Chen
 */
@Repository
public interface MfaOtpRecordRepository extends JpaRepository<MfaOtpRecord, Long> {

    Optional<MfaOtpRecord> findTop1ByIssuerBankIdAndAccountAndIsActiveTureOrderByCreateMillisDesc(long issuerBankId,
      String account);
}
