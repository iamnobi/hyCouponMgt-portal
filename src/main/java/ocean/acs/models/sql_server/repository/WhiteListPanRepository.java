package ocean.acs.models.sql_server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ocean.acs.models.sql_server.entity.WhiteListPan;

@Repository
public interface WhiteListPanRepository
        extends CrudRepository<WhiteListPan, Long>, JpaSpecificationExecutor<WhiteListPan> {

    @Query("select w.id from WhiteListPan w where w.panId = :panInfoId and w.deleteFlag = false")
    Page<Long> findIdByPanId(@Param("panInfoId") Long panInfoId, Pageable pageable);
}
