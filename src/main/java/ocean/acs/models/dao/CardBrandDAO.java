package ocean.acs.models.dao;

import java.util.List;
import java.util.Optional;
import ocean.acs.models.data_object.entity.CardBrandDO;
import ocean.acs.models.data_object.portal.CardBrandSigningCertificateConfigDO;

public interface CardBrandDAO {

    List<CardBrandDO> findCardBrandList();

    Optional<CardBrandDO> findByName(String name);

    List<CardBrandSigningCertificateConfigDO> findCardBrandSigningCertificateConfig();
}
