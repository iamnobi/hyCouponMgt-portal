package ocean.acs.models.dao;

import java.util.Optional;
import ocean.acs.models.data_object.entity.ThreeDSVerifiedOperationLogDO;
import ocean.acs.models.data_object.portal.PageQueryDO;
import ocean.acs.models.data_object.portal.PagingResultDO;
import ocean.acs.models.data_object.portal.PortalThreeDSVerifiedLogDO;

public interface ThreeDSVerifiedDAO {

    Optional<ThreeDSVerifiedOperationLogDO> findById(Long id);

    Optional<ThreeDSVerifiedOperationLogDO> save(
            ThreeDSVerifiedOperationLogDO threeDSVerifiedOperationLogDO);

    PagingResultDO<PortalThreeDSVerifiedLogDO> getInfoByCardHolderIdAndIssuerBankId(Long panId,
            Long issuerBankId, PageQueryDO queryDO);
    int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis);

}
