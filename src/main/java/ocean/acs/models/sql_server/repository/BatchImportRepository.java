package ocean.acs.models.sql_server.repository;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ocean.acs.commons.constant.PortalEnvironmentConstants;
import ocean.acs.models.sql_server.entity.BatchImport;

@Repository
public interface BatchImportRepository extends CrudRepository<BatchImport, Long> {

    @Query("select b.batchName from BatchImport b where id = ?1 and deleteFlag = 0")
    Optional<String> findBatchNameById(Long batchId);

    @Query(value = "select b from BatchImport b where b.issuerBankId = ?1 and batchName='"
            + PortalEnvironmentConstants.DEFAULT_BLACK_LIST_PAN_BATCH_NAME + "' and b.deleteFlag = 0")
    Optional<BatchImport> getSingleAddedBatchId(Long issuerBankId);

    @Query(value = "select b from BatchImport b where b.issuerBankId = ?1 and b.deleteFlag = 0")
    List<BatchImport> findBatchNames(Long issuerBankId);

    @Transactional
    @Modifying
    @Query("update BatchImport b set b.transStatus = :transStatus, b.updateMillis = :updateMillis where  b.issuerBankId = :issuerBankId and b.batchName = :batchName and b.deleteFlag = 0")
    void updateTransStatusByIssuerBankIdAndBatchName(@Param("issuerBankId") Long issuerBankId,
            @Param("batchName") String batchName, @Param("transStatus") String transStatus,
            @Param("updateMillis") long updateMillis);
}
