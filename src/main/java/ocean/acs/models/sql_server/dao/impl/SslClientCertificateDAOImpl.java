package ocean.acs.models.sql_server.dao.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.Activity;
import ocean.acs.commons.enumerator.KeyStatus;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.dao.SslCertificateDAO;
import ocean.acs.models.data_object.entity.SslClientCertificateDO;
import ocean.acs.models.sql_server.entity.SslClientCertificate;

@Log4j2
@Repository
@AllArgsConstructor
public class SslClientCertificateDAOImpl implements SslCertificateDAO {

    private final ocean.acs.models.sql_server.repository.SslClientCertificateRepository repo;

    @Override
    public List<SslClientCertificateDO> findAll() throws DatabaseException {
        try {
            return repo.findAll().stream().map(SslClientCertificateDO::valueOf)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[findAll] unknown exception", e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public SslClientCertificateDO save(SslClientCertificateDO activitySslCertDO) {
        SslClientCertificate activitySslCert = SslClientCertificate.valueOf(activitySslCertDO);
        return SslClientCertificateDO.valueOf(repo.save(activitySslCert));
    }

    @Override
    public int deleteByCardBrand(String cardBrandName) {
        return repo.deleteByCardBrand(cardBrandName);
    }

    @Override
    public Optional<SslClientCertificateDO> findUnDeleteByActivityAndCardBrandAndKeyStatus(
            String cardBrandName, KeyStatus keyStatus) {
        return repo.findUnDeleteByActivityAndCardBrandAndKeyStatus(Activity.DISABLED.ordinal(),
                cardBrandName, keyStatus.getCode()).map(SslClientCertificateDO::valueOf);
    }

    @Override
    public boolean deleteByActivityAndCardBrandAndKeyStatus(String cardBrandName) {
        int updateCount = repo.deleteByActivityAndCardBrandAndKeyStatus(Activity.DISABLED.ordinal(),
                cardBrandName, KeyStatus.PROCESSING.ordinal());
        return updateCount == 1;
    }

    @Override
    public List<SslClientCertificateDO> findUnDeleteByCardBrand(String cardBrandName) {
        List<SslClientCertificate> sslClientCertificateList =
                repo.findUnDeleteByCardBrand(cardBrandName);
        return sslClientCertificateList.stream().map(SslClientCertificateDO::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<SslClientCertificateDO> findUnDeleteByActivityAndCardBrand(
            String cardBrandName) {
        return repo.findUnDeleteByActivityAndCardBrand(Activity.ENABLED.ordinal(), cardBrandName)
                .map(SslClientCertificateDO::valueOf);
    }

    @Override
    public Optional<SslClientCertificateDO> findById(Long id) {
        return repo.findById(id).map(SslClientCertificateDO::valueOf);
    }

}
