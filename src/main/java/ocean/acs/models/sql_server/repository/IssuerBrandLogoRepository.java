package ocean.acs.models.sql_server.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.sql_server.entity.IssuerBrandLogo;

@Repository
public interface IssuerBrandLogoRepository extends CrudRepository<IssuerBrandLogo, Long> {

    @Query(value = "select * from issuer_brand_logo where issuer_bank_id = ?1 and delete_flag = 0",
            nativeQuery = true)
    Optional<IssuerBrandLogo> findByIssuerBankId(Long issuerBankID);

}
