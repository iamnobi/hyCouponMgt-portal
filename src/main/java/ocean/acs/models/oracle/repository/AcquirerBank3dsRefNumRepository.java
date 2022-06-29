package ocean.acs.models.oracle.repository;

import ocean.acs.models.oracle.entity.AcquirerBank3dsRefNum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcquirerBank3dsRefNumRepository extends JpaRepository<AcquirerBank3dsRefNum, Long> {}
