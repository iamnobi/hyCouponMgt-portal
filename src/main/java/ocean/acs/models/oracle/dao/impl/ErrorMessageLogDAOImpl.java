package ocean.acs.models.oracle.dao.impl;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.dao.ErrorMessageLogDAO;
import ocean.acs.models.data_object.entity.ErrorMessageLogDO;
import ocean.acs.models.data_object.entity.TransactionLogDO;
import ocean.acs.models.data_object.kernel.ErrorDO;
import ocean.acs.models.oracle.entity.ErrorMessageLog;
import ocean.acs.models.oracle.entity.TransactionLog;

@Log4j2
@Repository
@AllArgsConstructor
public class ErrorMessageLogDAOImpl implements ErrorMessageLogDAO {

    private final ocean.acs.models.oracle.repository.ErrorMessageLogRepository repo;
    private final ocean.acs.models.oracle.repository.TransactionLogRepository transactionLogRepository;

    @Override
    public ErrorMessageLogDO save(ErrorMessageLogDO errorMessageLogDO) throws DatabaseException {
        try {
            ErrorMessageLog errorMessageLog = ErrorMessageLog.valueOf(errorMessageLogDO);
            return ErrorMessageLogDO.valueOf(repo.save(errorMessageLog));
        } catch (Exception e) {
            log.error("[save] unknown exception, errorMessageLog={}", errorMessageLogDO, e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public Optional<ErrorMessageLogDO> findById(Long id) {
        try {
            return repo.findById(id).map(ErrorMessageLogDO::valueOf);
        } catch (Exception e) {
            log.error("[findById] unknown exception, id={}", id, e);
        }
        return Optional.empty();
    }

    @Override
    public ErrorMessageLogDO saveError(TransactionLogDO transactionLogDO, ErrorDO errorDO)
            throws DatabaseException {
        ErrorMessageLog errorMessageLog;
        try {
            if (transactionLogDO.getErrorMessageLogID() == null) {
                log.info("[saveError] Save new ErrorMessageLog");
                errorMessageLog = ErrorMessageLog.valueOf(errorDO);
            } else {
                log.info("[saveError] Update existing ErrorMessageLog, id = {}",
                        transactionLogDO.getErrorMessageLogID());
                errorMessageLog =
                        findById(transactionLogDO.getErrorMessageLogID()).map(oldMessageLog -> {
                            log.info("[saveError] oldMessageLog: {}", oldMessageLog);
                            return ErrorMessageLog.updateValueOf(errorDO, oldMessageLog);
                        }).orElseGet(() -> {
                            log.info("[saveError] oldMessageLog not found");
                            return ErrorMessageLog.valueOf(errorDO);
                        });
            }
            errorMessageLog = repo.save(errorMessageLog);

            transactionLogDO.setErrorMessageLogID(errorMessageLog.getId());
            TransactionLog transactionLog = TransactionLog.valueOf(transactionLogDO);
            transactionLogRepository.save(transactionLog);

            return ErrorMessageLogDO.valueOf(errorMessageLog);
        } catch (Exception e) {
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

}
