package ocean.acs.models.oracle.dao.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import ocean.acs.models.dao.RootCaCertificateDAO;
import ocean.acs.models.data_object.entity.RootCaCertificateDO;
import ocean.acs.models.oracle.entity.RootCaCertificate;

@Repository
@AllArgsConstructor
public class RootCaCertificateDAOImpl implements RootCaCertificateDAO {

    private final ocean.acs.models.oracle.repository.RootCaCertificateRepository repo;

    @Override
    public RootCaCertificateDO create(RootCaCertificateDO rootCaCertificateDO) {
        RootCaCertificate rootCaCertificate = RootCaCertificate.valueOf(rootCaCertificateDO);
        return RootCaCertificateDO.valueOf(repo.save(rootCaCertificate));
    }

    @Override
    public Optional<RootCaCertificateDO> findById(Long id) {
        return repo.findById(id).map(RootCaCertificateDO::valueOf);
    }

    @Override
    public List<RootCaCertificateDO> findAllById(Iterable<Long> ids) {
        return repo.findAllById(ids).stream().map(RootCaCertificateDO::valueOf)
                .collect(Collectors.toList());
    }

}
