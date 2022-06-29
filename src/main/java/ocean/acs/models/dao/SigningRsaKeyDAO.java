package ocean.acs.models.dao;

import java.util.Optional;
import ocean.acs.models.data_object.entity.SigningRsaKeyDO;

public interface SigningRsaKeyDAO {

    SigningRsaKeyDO insert(SigningRsaKeyDO signingRsaKeyDO);

    Optional<SigningRsaKeyDO> findById(Long id);

}
