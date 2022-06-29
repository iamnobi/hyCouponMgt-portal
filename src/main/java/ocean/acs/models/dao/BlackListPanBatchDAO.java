package ocean.acs.models.dao;

import java.util.List;
import java.util.Optional;
import ocean.acs.commons.enumerator.TransStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.NoSuchDataException;
import ocean.acs.models.data_object.entity.BatchImportDO;

public interface BlackListPanBatchDAO {

    /** 取得單筆新增的batchId */
    Long getSingleAddedBatchId(Long issuerBankId) throws DatabaseException, NoSuchDataException;

    List<BatchImportDO> findBatchNameList(Long issuerBankId) throws DatabaseException;

    Optional<String> findBatchNameById(Long id);

    void updateTransStatusByIssuerBankIdAndBatchName(Long issuerBankId, String batchName,
            TransStatus transStatus) throws DatabaseException;
}
