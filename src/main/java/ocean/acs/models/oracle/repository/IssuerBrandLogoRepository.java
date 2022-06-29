package ocean.acs.models.oracle.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.IssuerBrandLogo;

@Repository
public interface IssuerBrandLogoRepository extends CrudRepository<IssuerBrandLogo, Long> {

    @Query(value = "select * from ISSUER_BRAND_LOGO where issuer_bank_id = ?1 and delete_flag = 0 ",
            nativeQuery = true)
    Optional<IssuerBrandLogo> findByIssuerBankId(Long issuerBankID);

}
