package ocean.acs.models.dao;

import java.util.List;
import java.util.Optional;
import ocean.acs.models.data_object.entity.SubCaCertificateDO;

public interface SubCaCertificateDAO {

    SubCaCertificateDO create(SubCaCertificateDO subCaCertificateDO);

    Optional<SubCaCertificateDO> findById(Long id);

    List<SubCaCertificateDO> findAllById(Iterable<Long> ids);

}
