package ocean.acs.models.oracle.repository;

import ocean.acs.models.oracle.entity.VELogPortal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface VELogPortalRepository
    extends JpaRepository<VELogPortal, String>, JpaSpecificationExecutor<VELogPortal> {

}
