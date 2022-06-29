package ocean.acs.models.dao;

import java.util.List;
import java.util.Optional;
import ocean.acs.commons.enumerator.KeyStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.entity.SslClientCertificateDO;

public interface SslCertificateDAO {

    List<SslClientCertificateDO> findAll() throws DatabaseException;

    SslClientCertificateDO save(SslClientCertificateDO activitySslCertDO);

    int deleteByCardBrand(String cardBrandName);

    Optional<SslClientCertificateDO> findUnDeleteByActivityAndCardBrandAndKeyStatus(
            String cardBrandName, KeyStatus keyStatus);

    boolean deleteByActivityAndCardBrandAndKeyStatus(String cardBrandName);

    List<SslClientCertificateDO> findUnDeleteByCardBrand(String cardBrandName);

    Optional<SslClientCertificateDO> findUnDeleteByActivityAndCardBrand(String cardBrandName);

    Optional<SslClientCertificateDO> findById(Long id);

}
