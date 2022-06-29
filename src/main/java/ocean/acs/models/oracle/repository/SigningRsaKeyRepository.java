package ocean.acs.models.oracle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.SigningRsaKey;

@Repository
public interface SigningRsaKeyRepository extends JpaRepository<SigningRsaKey, Long> {

}
