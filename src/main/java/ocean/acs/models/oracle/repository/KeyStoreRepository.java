package ocean.acs.models.oracle.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.KeyStore;

@Repository
public interface KeyStoreRepository extends CrudRepository<KeyStore, String> {

    Optional<KeyStore> findByKeyName(String keyName);
}
