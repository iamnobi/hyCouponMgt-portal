package ocean.acs.models.sql_server.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.sql_server.entity.BatchImport;

@Repository
public interface BatchRepository
        extends CrudRepository<BatchImport, Long>, JpaSpecificationExecutor<BatchImport> {

    Page<BatchImport> findAll(Pageable pageable);

    Optional<BatchImport> findByIdAndDeleteFlagIsFalse(Long id);

}
