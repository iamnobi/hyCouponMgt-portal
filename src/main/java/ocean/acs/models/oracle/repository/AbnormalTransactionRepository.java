package ocean.acs.models.oracle.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.AbnormalTransaction;

@Repository
public interface AbnormalTransactionRepository extends CrudRepository<AbnormalTransaction, Long>,
        JpaSpecificationExecutor<AbnormalTransaction> {

    Optional<AbnormalTransaction> findByYearAndMonthAndDayOfMonthAndIssuerBankIdAndMerchantId(int year,
            int month, int day, Long issuerBankId, String merchantId);
}
