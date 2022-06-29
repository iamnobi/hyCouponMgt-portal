package ocean.acs.models.oracle.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.IssuerHandingFee;

@Repository
public interface IssuerHandingFeeRepository extends CrudRepository<IssuerHandingFee, Long> {

    @Query(value = "select * from ISSUER_HANDING_FEE where issuer_bank_id = ?1 and delete_flag = 0 ",
            nativeQuery = true)
    Optional<IssuerHandingFee> getByIssuerBankId(Long issuerBankId);

    List<IssuerHandingFee> findByIssuerBankIdIn(Set<Long> issuerBankIdSet);

}
