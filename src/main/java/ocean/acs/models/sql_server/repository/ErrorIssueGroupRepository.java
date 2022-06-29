package ocean.acs.models.sql_server.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.sql_server.entity.ErrorIssueGroup;

@Repository
public interface ErrorIssueGroupRepository
        extends CrudRepository<ErrorIssueGroup, Long>, JpaSpecificationExecutor<ErrorIssueGroup> {

    List<ErrorIssueGroup> findByDeleteFlagIsFalse();

    Optional<ErrorIssueGroup> findByIdAndDeleteFlagIsFalse(Long groupId);

}
