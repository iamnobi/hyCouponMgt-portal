package ocean.acs.models.dao;

import java.util.List;
import java.util.Optional;
import ocean.acs.commons.enumerator.TransStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.entity.BlackListDeviceInfoDO;
import ocean.acs.models.data_object.portal.BlackListDeviceIdQueryDO;
import ocean.acs.models.data_object.portal.ComplexBlackListDeviceDO;
import ocean.acs.models.data_object.portal.IdsQueryDO;
import ocean.acs.models.data_object.portal.PagingResultDO;

public interface BlackListDeviceInfoDAO {

    Optional<BlackListDeviceInfoDO> findByIssuerBankIdAndDeviceId(Long issuerBankId,
            String deviceId) throws DatabaseException;

    BlackListDeviceInfoDO save(BlackListDeviceInfoDO blackListDeviceDO);

    List<BlackListDeviceInfoDO> saveAll(List<BlackListDeviceInfoDO> deviceInfoDOs);

    boolean existsByIssuerBankIdAndDeviceIDAndNotDelete(Long issuerBankId, String deviceID) throws DatabaseException;

    Optional<BlackListDeviceInfoDO> findByIdAndNotDelete(Long id);

    List<ComplexBlackListDeviceDO> findByKernelTransactionLogIds(List<Long> txLogIDs);

    PagingResultDO<ComplexBlackListDeviceDO> query(BlackListDeviceIdQueryDO queryDO);

    List<ComplexBlackListDeviceDO> findByIds(IdsQueryDO queryDO);

    void updateTransStatusAndUpdaterByIssuerBankId(Long issuerBankId, TransStatus transStatus,
            String updater);

    void deleteByIds(Long id, String deleter);
    int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis);

}
