package ocean.acs.models.oracle.dao.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.dao.TransactionAmountDAO;
import ocean.acs.models.data_object.kernel.TransactionAmountDO;
import ocean.acs.models.oracle.entity.TransactionAmount;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
@AllArgsConstructor
public class TransactionAmountDAOImpl implements TransactionAmountDAO {
    
    private final ocean.acs.models.oracle.repository.TransactionAmountRepository repo;

    @Override
    public void insert(TransactionAmountDO transactionAmountDO) throws DatabaseException {
        try {
            TransactionAmount transactionAmount = TransactionAmount.valueOf(transactionAmountDO);
            repo.save(transactionAmount);
        } catch (Exception e) {
            log.error("[insert] error saving transactionAmountDO={}", transactionAmountDO);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public List<TransactionAmountDO> findByPanInfoIdWithinMillis(Long panInfoId, Long createMillis)
            throws DatabaseException {

        try {
            return repo
                    .findByPanInfoIDAndCreateMillisAfter(panInfoId, createMillis)
                    .stream()
                    .map(TransactionAmountDO::valueOf)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error(
                    "[findByPanInfoIdWithinMillis] error. panInfoId={}, createMillis={}",
                    panInfoId,
                    createMillis);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }
}
