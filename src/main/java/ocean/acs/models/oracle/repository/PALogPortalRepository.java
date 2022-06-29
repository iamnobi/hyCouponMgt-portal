package ocean.acs.models.oracle.repository;

import ocean.acs.models.oracle.entity.PALogPortal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PALogPortalRepository
    extends JpaRepository<PALogPortal, String>, JpaSpecificationExecutor<PALogPortal> {
}
