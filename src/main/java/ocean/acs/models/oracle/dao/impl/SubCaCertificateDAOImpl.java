package ocean.acs.models.oracle.dao.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import ocean.acs.models.dao.SubCaCertificateDAO;
import ocean.acs.models.data_object.entity.SubCaCertificateDO;
import ocean.acs.models.oracle.entity.SubCaCertificate;

@Repository
@AllArgsConstructor
public class SubCaCertificateDAOImpl implements SubCaCertificateDAO {

    private final ocean.acs.models.oracle.repository.SubCaCertificateRepository repo;

    @Override
    public SubCaCertificateDO create(SubCaCertificateDO subCaCertificateDO) {
        SubCaCertificate subCaCertificate = SubCaCertificate.valueOf(subCaCertificateDO);
        return SubCaCertificateDO.valueOf(repo.save(subCaCertificate));
    }

    @Override
    public Optional<SubCaCertificateDO> findById(Long id) {
        return repo.findById(id).map(SubCaCertificateDO::valueOf);
    }

    @Override
    public List<SubCaCertificateDO> findAllById(Iterable<Long> ids) {
        return repo.findAllById(ids).stream().map(SubCaCertificateDO::valueOf)
                .collect(Collectors.toList());
    }

}
