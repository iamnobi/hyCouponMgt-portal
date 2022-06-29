package ocean.acs.models.sql_server.repository;

import java.util.Optional;
import ocean.acs.models.sql_server.entity.MimaPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * MimaPolicyRepository
 *
 * @author Alan Chen
 */
public interface MimaPolicyRepository extends JpaRepository<MimaPolicy, Long> {

    Optional<MimaPolicy> findByIssuerBankId(long issuerBankId);

}
