package ocean.acs.models.sql_server.dao.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.enumerator.TransStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.NoSuchDataException;
import ocean.acs.models.dao.BlackListPanBatchDAO;
import ocean.acs.models.data_object.entity.BatchImportDO;
import ocean.acs.models.sql_server.entity.BatchImport;

@Log4j2
@Repository
@AllArgsConstructor
public class BlackListPanBatchDAOImpl implements BlackListPanBatchDAO {

    private final ocean.acs.models.sql_server.repository.BatchImportRepository repo;

    @Override
    public Long getSingleAddedBatchId(Long issuerBankId)
            throws DatabaseException, NoSuchDataException {
        try {
            return repo.getSingleAddedBatchId(issuerBankId).map(BatchImport::getId)
                    .orElseThrow(() -> new NoSuchDataException(
                            "SingleAddedBatchId not found by IssuerBankId=" + issuerBankId));
        } catch (Exception e) {
            log.error("[getSingleAddedBatchId] unknown exception, issuerBankId={}", issuerBankId,
                    e);
            throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public List<BatchImportDO> findBatchNameList(Long issuerBankId) throws DatabaseException {
        try {
            return repo.findBatchNames(issuerBankId).stream().map(BatchImportDO::valueOf)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[findBatchNameList] unknown exception, issuerBankId={}", issuerBankId, e);
            throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public Optional<String> findBatchNameById(Long id) {
        return repo.findBatchNameById(id);
    }

    @Override
    public void updateTransStatusByIssuerBankIdAndBatchName(Long issuerBankId, String batchName,
            TransStatus transStatus) throws DatabaseException {
        try {
            repo.updateTransStatusByIssuerBankIdAndBatchName(issuerBankId, batchName,
                    transStatus.getCode(), System.currentTimeMillis());
        } catch (Exception e) {
            log.error(
                    "[updateTransStatusByIssuerBankIdAndBatchName] unknown exception, issuerBankId={}, batchName={}, transStatus={}",
                    issuerBankId, batchName, transStatus.getCode(), e);
            throw new DatabaseException(ResultStatus.DB_SAVE_ERROR, e.getMessage());
        }
    }

}
