package ocean.acs.models.dao;

import java.util.List;
import java.util.Optional;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.entity.CaCertificateDO;

public interface CaCertificateDAO {
    
    List<CaCertificateDO> findAll() throws DatabaseException;

    CaCertificateDO save(CaCertificateDO caCertificateDO);

    boolean delete(CaCertificateDO caCertificateDO);

    List<CaCertificateDO> findByCardBrand(String cardBrand);

    int countByCardBrand(String cardBrand);

    Optional<CaCertificateDO> findById(Long id);

    Boolean existsByCardBrandAndDeleteFlagFalse(String cardBrand);

}
