package ocean.acs.models.dao;

import java.util.List;
import java.util.Optional;
import ocean.acs.models.data_object.entity.CertificateDO;

public interface CertificateDAO {

    CertificateDO create(CertificateDO certificateDO);

    Optional<CertificateDO> findById(Long id);

    List<CertificateDO> findAllById(Iterable<Long> ids);

}
