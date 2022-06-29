package ocean.acs.models.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import ocean.acs.commons.enumerator.TransStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.entity.BlackListMerchantUrlDO;
import ocean.acs.models.data_object.kernel.KernelBlackListMerchantUrlDO;
import ocean.acs.models.data_object.portal.BlackListMerchantUrlQueryDO;
import ocean.acs.models.data_object.portal.DeleteDataDO;
import ocean.acs.models.data_object.portal.IdsQueryDO;
import ocean.acs.models.data_object.portal.PagingResultDO;
import ocean.acs.models.data_object.portal.PortalBlackListMerchantUrlDO;

public interface BlackListMerchantUrlDAO {

    List<KernelBlackListMerchantUrlDO> findByIssuerBankID(Long issuerBankID)
            throws DatabaseException;

    void deleteById(DeleteDataDO deleteDO);

    PagingResultDO<BlackListMerchantUrlDO> findByUrlLike(BlackListMerchantUrlQueryDO queryDO);

    List<BlackListMerchantUrlDO> findByIds(IdsQueryDO queryDO, Pageable pageable);

    Optional<BlackListMerchantUrlDO> findById(Long blackListMerchantUrlId);

    BlackListMerchantUrlDO create(PortalBlackListMerchantUrlDO createDO, TransStatus transStatus);

    BlackListMerchantUrlDO update(BlackListMerchantUrlDO updateDO);

    boolean isUrlExist(Long issuerBankId, String url);

    void updateTransStatusByIssuerBankId(Long issuerBankId, TransStatus transStatus,
            String updater);
    int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis);

}
