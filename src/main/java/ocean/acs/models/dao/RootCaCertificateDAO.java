package ocean.acs.models.dao;

import java.util.List;
import java.util.Optional;
import ocean.acs.models.data_object.entity.RootCaCertificateDO;

public interface RootCaCertificateDAO {

    RootCaCertificateDO create(RootCaCertificateDO rootCaCertificateDO);

    Optional<RootCaCertificateDO> findById(Long id);

    List<RootCaCertificateDO> findAllById(Iterable<Long> ids);

}
