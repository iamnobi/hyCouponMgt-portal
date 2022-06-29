package ocean.acs.models.sql_server.repository;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ocean.acs.models.data_object.kernel.BlackListPanAuthStatusDO;
import ocean.acs.models.sql_server.entity.BlackListPan;

@Repository
public interface BlackListPanRepository
        extends CrudRepository<BlackListPan, Long>, JpaSpecificationExecutor<BlackListPan> {

    @Query(value = "select new ocean.acs.models.data_object.kernel.BlackListPanAuthStatusDO( " + 
                   "blp.id, blpb.transStatus, blp.createMillis, blpb.updateMillis)\n" + 
                   "from BlackListPan blp\n" + 
                   "left join BatchImport blpb on blpb.id = blp.blackListPanBatchId\n" + 
                   "where blp.panId = :panInfoId and blp.deleteFlag = false and blpb.deleteFlag = false")
    List<BlackListPanAuthStatusDO> findByPanInfoId(@Param("panInfoId") Long panInfoId);

    @Transactional
    @Modifying
    @Query("update from BlackListPan set deleteFlag = 1, deleter = :deleter, deleteMillis = :deleteMillis where id in :ids")
    void deleteByIds(@Param("ids") List<Long> ids, @Param("deleter") String deleter,
            @Param("deleteMillis") long now);

    @Transactional
    @Modifying
    @Query("update from BlackListPan set deleteFlag = 1, deleter = :deleter, deleteMillis = :deleteMillis where blackListPanBatchId = :blackListPanBatchId")
    void deleteByBlackListPanBatchId(@Param("blackListPanBatchId") long blackListPanBatchId,
            @Param("deleter") String deleter, @Param("deleteMillis") long now);

    @Transactional
    @Modifying
    @Query("update from BlackListPan set transStatus = :transStatus, updater = :updater, updateMillis = :updateMillis "
            + "where blackListPanBatchId = :blackListPanBatchId")
    void updateTransStatusByBlackListPanBatchId(@Param("transStatus") String transStatus,
            @Param("updater") String updater, @Param("updateMillis") long now,
            @Param("blackListPanBatchId") Long blackListPanBatchId);
}
