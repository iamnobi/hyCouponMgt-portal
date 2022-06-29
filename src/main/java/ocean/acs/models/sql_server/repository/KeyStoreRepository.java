package ocean.acs.models.sql_server.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.sql_server.entity.KeyStore;

@Repository
public interface KeyStoreRepository extends CrudRepository<KeyStore, String> {

    Optional<KeyStore> findByKeyName(String keyName);
}
