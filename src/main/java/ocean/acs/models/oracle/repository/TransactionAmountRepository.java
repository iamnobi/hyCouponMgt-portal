package ocean.acs.models.oracle.repository;

import java.util.List;
import ocean.acs.models.oracle.entity.TransactionAmount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionAmountRepository extends CrudRepository<TransactionAmount, Long> {
    List<TransactionAmount> findByPanInfoIDAndCreateMillisAfter(
            Long panInfoId, Long createMillis);
}
