package ocean.acs.models.sql_server.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.sql_server.entity.IssuerLogo;

@Repository
public interface IssuerLogoRepository extends CrudRepository<IssuerLogo, String> {

    @Override
    @Query("select i from IssuerLogo i where deleteFlag = 0")
    List<IssuerLogo> findAll();

    @Query(value = "select * from issuer_logo where id = ?1", nativeQuery = true)
    Optional<IssuerLogo> findById(Long id);

    @Query(value = "select * from issuer_logo where issuer_bank_id = ?1 and delete_flag = 0",
            nativeQuery = true)
    Optional<IssuerLogo> findByIssuerBankId(Long issuerBankId);

    @Query(value = "select top 1 * from issuer_logo where issuer_bank_id = ?1 and delete_flag = 0 order by id desc",
            nativeQuery = true)
    Optional<IssuerLogo> findTopOneByIssuerBankIdAndNotDelete(Long issuerBankId);

}
