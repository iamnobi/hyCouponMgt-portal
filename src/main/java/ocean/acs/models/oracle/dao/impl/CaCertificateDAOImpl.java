package ocean.acs.models.oracle.dao.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.dao.CaCertificateDAO;
import ocean.acs.models.data_object.entity.CaCertificateDO;
import ocean.acs.models.oracle.entity.CaCertificate;

@Log4j2
@Repository
@AllArgsConstructor
public class CaCertificateDAOImpl implements CaCertificateDAO {

    private final ocean.acs.models.oracle.repository.CaCertificateRepository repo;

    @Override
    public List<CaCertificateDO> findAll() throws DatabaseException {
        try {
            List<CaCertificate> caCertificateList = repo.findAll();
            return caCertificateList.stream().map(CaCertificateDO::valueOf)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[findAll] unknown exception", e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public CaCertificateDO save(CaCertificateDO caCertificateDO) {
        CaCertificate caCertificate = CaCertificate.valueOf(caCertificateDO);
        return CaCertificateDO.valueOf(repo.save(caCertificate));
    }

    @Override
    public boolean delete(CaCertificateDO caCertificateDO) {
        CaCertificate caCertificate = CaCertificate.valueOf(caCertificateDO);
        caCertificate = repo.save(caCertificate);
        return null != caCertificate;
    }

    @Override
    public List<CaCertificateDO> findByCardBrand(String cardBrand) {
        List<CaCertificate> caCertificateList =
                repo.findByCardBrandAndDeleteFlagFalseOrderByExpireMillisDesc(cardBrand);
        return caCertificateList.stream().map(CaCertificateDO::valueOf).collect(Collectors.toList());
    }

    @Override
    public int countByCardBrand(String cardBrand) {
        return repo.countByCardBrandAndDeleteFlagFalse(cardBrand);
    }

    @Override
    public Optional<CaCertificateDO> findById(Long id) {
        Optional<CaCertificate> caCertificateOpt = repo.findByIdAndDeleteFlagFalse(id);
        return caCertificateOpt.map(CaCertificateDO::valueOf);
    }

    @Override
    public Boolean existsByCardBrandAndDeleteFlagFalse(String cardBrand) {
        return repo.existsByCardBrandAndDeleteFlagFalse(cardBrand);
    }

}
