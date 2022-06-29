package ocean.acs.models.oracle.repository;

import java.util.Optional;
import ocean.acs.models.oracle.entity.MfaOtpRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * MfaOtpRecordRepository
 *
 * @author Alan Chen
 */
@Repository
public interface MfaOtpRecordRepository extends JpaRepository<MfaOtpRecord, Long> {

    Optional<MfaOtpRecord> findTop1ByIssuerBankIdAndAccountAndIsActiveTrueOrderByCreateMillisDesc(long issuerBankId,
      String account);
}
