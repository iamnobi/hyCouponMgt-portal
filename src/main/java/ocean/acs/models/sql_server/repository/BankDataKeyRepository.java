package ocean.acs.models.sql_server.repository;

import java.util.Optional;
import ocean.acs.models.sql_server.entity.BankDataKey;
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
