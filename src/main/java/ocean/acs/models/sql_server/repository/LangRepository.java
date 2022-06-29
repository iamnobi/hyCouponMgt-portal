package ocean.acs.models.sql_server.repository;

import ocean.acs.models.sql_server.entity.Lang;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LangRepository extends CrudRepository<Lang, Long> {}
