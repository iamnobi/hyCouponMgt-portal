package ocean.acs.models.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.entity.WhiteListPanDO;
import ocean.acs.models.data_object.portal.DeleteDataDO;
import ocean.acs.models.data_object.portal.PagingResultDO;
import ocean.acs.models.data_object.portal.WhiteListPanCreateDO;
import ocean.acs.models.data_object.portal.WhiteListPanQueryDO;
import ocean.acs.models.data_object.portal.WhiteListQueryResultDO;

public interface WhiteListPanDAO {

    Optional<Long> getWhiteListIdByPanInfoId(Long panInfoId) throws DatabaseException;

    WhiteListPanDO add(WhiteListPanCreateDO createDO, Long panInfoId);

    WhiteListPanDO update(WhiteListPanDO whiteListPanDO);

    Optional<WhiteListPanDO> update(WhiteListPanCreateDO whiteListPanCreateDO);

    PagingResultDO<WhiteListQueryResultDO> query(WhiteListPanQueryDO queryDO);

    Optional<WhiteListPanDO> findById(Long id);

    PagingResultDO<WhiteListQueryResultDO> findByIds(List<Long> ids, Pageable pageable);

    /** Hash卡號是否存在 */
    boolean existsCardNumberHash(Long issuerBankId, String cardNumberHash);

    /** 刪除白名單 */
    Optional<WhiteListPanDO> delete(DeleteDataDO deleteDataDO);
    int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis);

}
