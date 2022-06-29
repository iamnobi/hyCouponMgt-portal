package ocean.acs.models.sql_server.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.sql_server.entity.IssuerHandingFee;

@Repository
public interface IssuerHandingFeeRepository extends CrudRepository<IssuerHandingFee, Long> {

    @Query(value = "select * from issuer_handing_fee where issuer_bank_id = ?1 and delete_flag = 0",
            nativeQuery = true)
    Optional<IssuerHandingFee> getByIssuerBankId(Long issuerBankId);

    List<IssuerHandingFee> findByIssuerBankIdIn(Set<Long> issuerBankIdSet);

}
