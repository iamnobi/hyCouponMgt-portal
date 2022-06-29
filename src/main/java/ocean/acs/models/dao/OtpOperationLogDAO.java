package ocean.acs.models.dao;

import java.util.Optional;
import ocean.acs.models.data_object.entity.OtpOperationLogDO;
import ocean.acs.models.data_object.portal.HolderIdQueryDO;
import ocean.acs.models.data_object.portal.PagingResultDO;

public interface OtpOperationLogDAO {

    Optional<OtpOperationLogDO> findById(Long id);

    OtpOperationLogDO save(OtpOperationLogDO otpOperationLogDO);

    PagingResultDO<OtpOperationLogDO> getByPanIdAndIssuerBankId(HolderIdQueryDO queryDO);
    int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis);

}
