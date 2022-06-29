package ocean.acs.models.sql_server.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.sql_server.entity.ErrorCodeMapping;

@Repository
public interface ErrorCodeMappingRepository extends CrudRepository<ErrorCodeMapping, Long> {

    List<ErrorCodeMapping> findByErrorGroupId(Long errGroupId);
}
