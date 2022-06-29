package ocean.acs.models.sql_server.repository;

import java.util.List;
import ocean.acs.models.sql_server.entity.TransactionAmount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionAmountRepository extends CrudRepository<TransactionAmount, Long> {

    List<TransactionAmount> findByPanInfoIDAndCreateMillisAfter(Long panInfoId, Long createMillis);
}
