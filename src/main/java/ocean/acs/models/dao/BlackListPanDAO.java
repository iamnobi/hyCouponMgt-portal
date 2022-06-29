package ocean.acs.models.dao;

import java.util.List;
import java.util.Optional;
import ocean.acs.commons.enumerator.TransStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.entity.BlackListPanDO;
import ocean.acs.models.data_object.entity.PanInfoDO;
import ocean.acs.models.data_object.kernel.BlackListPanAuthStatusDO;
import ocean.acs.models.data_object.portal.BlackListPanQueryDO;
import ocean.acs.models.data_object.portal.BlackListQueryResultDO;
import ocean.acs.models.data_object.portal.PagingResultDO;

public interface BlackListPanDAO {
    boolean existsByPanInfoId(long panInfoId) throws DatabaseException;
    Optional<BlackListPanAuthStatusDO> getLatestByPanInfoId(Long panInfoId)
            throws DatabaseException;

    Optional<BlackListPanDO> add(PanInfoDO panInfo, long batchId, String user,
            TransStatus transStatus);

    Optional<BlackListPanDO> add(BlackListPanDO insertData);

    void saveAll(List<BlackListPanDO> blackListPanList);

    BlackListPanDO saveOrUpdate(BlackListPanDO blackListPanDO) throws DatabaseException;

    List<BlackListQueryResultDO> queryBlackListPan(BlackListPanQueryDO queryDO)
            throws DatabaseException;

    PagingResultDO<BlackListQueryResultDO> queryPaginationBlackListPan(BlackListPanQueryDO queryDO)
            throws DatabaseException;

    List<BlackListQueryResultDO> queryByBlackListId(List<Long> blackListIds)
            throws DatabaseException;

    boolean existsByCardNumberHashAndBatchId(Long issuerBankId, String cardNumberHash, Long batchId)
            throws DatabaseException;

    Optional<BlackListPanDO> findById(Long id) throws DatabaseException;

    boolean updateTransStatusByBlackListPanBatchId(TransStatus transStatus, String updater,
            Long blackListPanBatchID);

    boolean deleteByBlackListPanBatchId(String deleter, long blackListPanBatchID);
    int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis);
    int deleteByPanInfoId(long panInfoId, String deleter, long deleteMillis);
}
