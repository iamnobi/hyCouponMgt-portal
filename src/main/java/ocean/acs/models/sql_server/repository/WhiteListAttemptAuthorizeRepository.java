package ocean.acs.models.sql_server.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.sql_server.entity.WhiteListAttemptAuthorize;

@Repository
public interface WhiteListAttemptAuthorizeRepository
        extends CrudRepository<WhiteListAttemptAuthorize, String> {
}
