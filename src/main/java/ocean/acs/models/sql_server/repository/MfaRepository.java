package ocean.acs.models.sql_server.repository;


import java.util.Optional;
import ocean.acs.models.sql_server.entity.MfaInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * MFA Repository
 *
 * @author Alan Chen
 */
@Repository
public interface MfaRepository extends JpaRepository<MfaInfo, Long> {

    Optional<MfaInfo> findByIssuerBankIdAndAccount(long issuerBankId, String account);
}
