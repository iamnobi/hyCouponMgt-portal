package ocean.acs.models.oracle.dao.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import ocean.acs.models.dao.CertificateDAO;
import ocean.acs.models.data_object.entity.CertificateDO;
import ocean.acs.models.oracle.entity.Certificate;

@Repository
@AllArgsConstructor
public class CertificateDAOImpl implements CertificateDAO {

    private final ocean.acs.models.oracle.repository.CertificateRepository repo;

    @Override
    public CertificateDO create(CertificateDO certificateDO) {
        Certificate certificate = Certificate.valueOf(certificateDO);
        return CertificateDO.valueOf(repo.save(certificate));
    }

    @Override
    public Optional<CertificateDO> findById(Long id) {
        Optional<Certificate> certificateOpt = repo.findById(id);
        return certificateOpt.map(CertificateDO::valueOf);
    }

    @Override
    public List<CertificateDO> findAllById(Iterable<Long> ids) {
        List<Certificate> certificateList = repo.findAllById(ids);
        return certificateList.stream().map(CertificateDO::valueOf).collect(Collectors.toList());
    }

}
