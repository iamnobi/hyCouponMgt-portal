package ocean.acs.models.dao;

import java.util.List;
import java.util.Optional;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.entity.PanOtpStatisticsDO;

public interface PanOtpStatisticsDAO {

    Optional<PanOtpStatisticsDO> findByPanInfoId(Long panInfoId) throws DatabaseException;

    boolean isOtpLock(Long panInfoId, Integer maxOtpVerifyCount) throws DatabaseException;

    boolean isExistsByPanInfoId(Long panInfoId) throws DatabaseException;

    void increaseVerifyOtpCount(Long panInfoId, String updater) throws DatabaseException;

    void initVerifyOtpCount(Long panInfoId, String updater) throws DatabaseException;

    PanOtpStatisticsDO save(PanOtpStatisticsDO panOtpStatisticsDO) throws DatabaseException;

    void saveOrUpdate(PanOtpStatisticsDO panOtpStatistics) throws DatabaseException;

    List<PanOtpStatisticsDO> saveAll(List<PanOtpStatisticsDO> panOtpStatisticsList) throws DatabaseException;
    int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis);

}
