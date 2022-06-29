package ocean.acs.models.sql_server.dao.impl;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import ocean.acs.models.dao.SigningRsaKeyDAO;
import ocean.acs.models.data_object.entity.SigningRsaKeyDO;
import ocean.acs.models.sql_server.entity.SigningRsaKey;

@Repository
@AllArgsConstructor
public class SigningRsaKeyDAOImpl implements SigningRsaKeyDAO {

    private final ocean.acs.models.sql_server.repository.SigningRsaKeyRepository repo;

    @Override
    public SigningRsaKeyDO insert(SigningRsaKeyDO signingRsaKeyDO) {
        SigningRsaKey signingRsaKey = SigningRsaKey.valueOf(signingRsaKeyDO);
        signingRsaKey.setUpdateMillis(Long.valueOf(System.currentTimeMillis()));
        return SigningRsaKeyDO.valueOf(repo.save(signingRsaKey));
    }

    @Override
    public Optional<SigningRsaKeyDO> findById(Long id) {
        return repo.findById(id).map(SigningRsaKeyDO::valueOf);
    }

}
