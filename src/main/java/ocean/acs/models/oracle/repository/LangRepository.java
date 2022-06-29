package ocean.acs.models.oracle.repository;

import ocean.acs.models.oracle.entity.Lang;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LangRepository extends CrudRepository<Lang, Long> {}
