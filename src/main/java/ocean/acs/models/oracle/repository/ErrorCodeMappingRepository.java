package ocean.acs.models.oracle.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.ErrorCodeMapping;

@Repository
public interface ErrorCodeMappingRepository extends CrudRepository<ErrorCodeMapping, Long> {

    List<ErrorCodeMapping> findByErrorGroupId(Long errGroupId);
}
