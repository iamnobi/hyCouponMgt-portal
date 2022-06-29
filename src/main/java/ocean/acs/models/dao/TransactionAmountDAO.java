package ocean.acs.models.dao;

import java.util.List;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.kernel.TransactionAmountDO;

public interface TransactionAmountDAO {

    void insert(TransactionAmountDO transactionAmountDO) throws DatabaseException;

    List<TransactionAmountDO> findByPanInfoIdWithinMillis(Long panInfoId, Long createMillis)
            throws DatabaseException;
}
