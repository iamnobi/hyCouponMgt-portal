package ocean.acs.models.dao;

import java.util.Optional;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.entity.ErrorMessageLogDO;
import ocean.acs.models.data_object.entity.TransactionLogDO;
import ocean.acs.models.data_object.kernel.ErrorDO;

public interface ErrorMessageLogDAO {

    ErrorMessageLogDO save(ErrorMessageLogDO errorMessageLog) throws DatabaseException;

    Optional<ErrorMessageLogDO> findById(Long id);

    ErrorMessageLogDO saveError(TransactionLogDO transactionLogDO, ErrorDO error) throws DatabaseException;

}
