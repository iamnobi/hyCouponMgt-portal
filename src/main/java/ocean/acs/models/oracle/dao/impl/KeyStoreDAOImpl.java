package ocean.acs.models.oracle.dao.impl;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import ocean.acs.models.dao.KeyStoreDAO;
import ocean.acs.models.data_object.entity.KeyStoreDO;

@Repository
@AllArgsConstructor
public class KeyStoreDAOImpl implements KeyStoreDAO {

    private final ocean.acs.models.oracle.repository.KeyStoreRepository repo;

    @Override
    public Optional<KeyStoreDO> findByKeyName(String keyName) {
        return repo.findByKeyName(keyName).map(KeyStoreDO::valueOf);
    }

}
