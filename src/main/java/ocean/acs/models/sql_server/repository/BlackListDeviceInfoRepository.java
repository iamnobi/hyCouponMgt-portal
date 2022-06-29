package ocean.acs.models.sql_server.repository;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ocean.acs.models.sql_server.entity.BlackListDeviceInfo;

@Repository
public interface BlackListDeviceInfoRepository extends CrudRepository<BlackListDeviceInfo, Long>,
        JpaSpecificationExecutor<BlackListDeviceInfo> {

    boolean existsByIssuerBankIdAndDeviceIDAndDeleteFlag(Long issuerBankId, String deviceID, Boolean deleteFlag);

    @Query("select b from BlackListDeviceInfo b where b.id in ?1 and b.deleteFlag = false")
    List<BlackListDeviceInfo> findByIdIn(List<Long> ids, Pageable pageable);

    @Query("select bldi from BlackListDeviceInfo bldi where bldi.id = :id and bldi.deleteFlag = false")
    Optional<BlackListDeviceInfo> findByIdAndNotDelete(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query("update BlackListDeviceInfo bldi set bldi.transStatus = :transStatus, bldi.updater = :updater, "
            + "bldi.updateMillis = :updateMillis where bldi.issuerBankId = :issuerBankId and bldi.deleteFlag = false")
    void updateTransStatusAndUpdaterAndUpdateMillisByIssuerBankIdAndNotDelete(
            @Param("issuerBankId") Long issuerBankId, @Param("transStatus") String transStatus,
            @Param("updater") String updater, @Param("updateMillis") long updateMillis);

    @Transactional
    @Modifying
    @Query("update BlackListDeviceInfo d set d.deleteFlag = true, d.deleter = :deleter, "
            + "d.deleteMillis = :deleteMillis where d.id = :id")
    void updateDeleteFlagAndDeleterAndDeleteMillisById(@Param("id") Long id,
            @Param("deleter") String deleter, @Param("deleteMillis") long now);

    @Query("select bldi from BlackListDeviceInfo bldi where bldi.issuerBankId = :issuerBankId and "
            + "bldi.deviceID = :deviceId and bldi.deleteFlag = false")
    Optional<BlackListDeviceInfo> findByIssuerBankIdAndDeviceId(
            @Param("issuerBankId") Long issuerBankId, @Param("deviceId") String deviceId);

}
