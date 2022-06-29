package ocean.acs.models.dao;

import java.util.Optional;
import org.springframework.data.domain.Page;
import ocean.acs.models.data_object.entity.BatchImportDO;
import ocean.acs.models.data_object.portal.BatchQueryDO;
import ocean.acs.models.data_object.portal.PagingResultDO;

public interface BatchImportDAO {

    Page<BatchImportDO> query(int pageIndex, int pageSize);

    BatchImportDO add(BatchImportDO batchImportDO);

    PagingResultDO<BatchImportDO> queryPanBlackListBatch(BatchQueryDO queryDO);

    Optional<BatchImportDO> getById(long id);

    BatchImportDO update(BatchImportDO batchImportDO);
    int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis);

}
