package ocean.acs.models.oracle.repository;

import ocean.acs.models.oracle.entity.PreparationLog;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PReqRecordRepository
        extends PagingAndSortingRepository<PreparationLog, Long>, JpaSpecificationExecutor<PreparationLog> {
}
