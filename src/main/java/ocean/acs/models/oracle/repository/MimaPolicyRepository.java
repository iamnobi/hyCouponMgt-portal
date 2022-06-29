package ocean.acs.models.oracle.repository;

import java.util.Optional;
import ocean.acs.models.oracle.entity.MimaPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * MimaPolicyRepository
 *
 * @author Alan Chen
 */
public interface MimaPolicyRepository extends JpaRepository<MimaPolicy, Long> {

    Optional<MimaPolicy> findByIssuerBankId(long issuerBankId);

}
