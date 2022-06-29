package ocean.acs.models.oracle.repository;

import java.util.Optional;
import ocean.acs.models.oracle.entity.BankDataKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * BankDataKeyRepository
 *
 * @author Alan Chen
 */
@Repository
public interface BankDataKeyRepository extends JpaRepository<BankDataKey, Long> {

    Optional<BankDataKey> findByIssuerBankId(long issuerBankId);

}
