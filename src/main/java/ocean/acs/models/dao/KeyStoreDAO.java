package ocean.acs.models.dao;

import java.util.Optional;
import ocean.acs.models.data_object.entity.KeyStoreDO;

public interface KeyStoreDAO {

    Optional<KeyStoreDO> findByKeyName(String keyName);
}
