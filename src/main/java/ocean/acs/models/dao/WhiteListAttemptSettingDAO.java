package ocean.acs.models.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.entity.WhiteListAttemptSettingDO;
import ocean.acs.models.data_object.kernel.WhiteListAttemptPanAuthStatusDO;
import ocean.acs.models.data_object.portal.GrantedLogQueryDO;
import ocean.acs.models.data_object.portal.GrantedTransactionLogDO;

public interface WhiteListAttemptSettingDAO {

    Optional<WhiteListAttemptPanAuthStatusDO> getLatestWhiteListAttemptByPanInfoId(Long panInfoId)
            throws DatabaseException;

    void updateTriesQuota(Long whiteListPanAttemptID, Integer triesQuota, String updater)
            throws DatabaseException;

    Optional<WhiteListAttemptSettingDO> findOneByPanId(Long panId);

    WhiteListAttemptSettingDO saveOrUpdate(WhiteListAttemptSettingDO settingDO);

    Page<WhiteListAttemptSettingDO> findAttemptSettingByPanId(GrantedLogQueryDO logQueryDO,
            Pageable pageable);

    List<GrantedTransactionLogDO> getGrantedTransactionLog(Long attemptSettingId);

    /**
     * 統計人工彈性授權開通次數
     *
     * @return -1 indicates an error.
     */
    int sumByTriesPermitted(Long issuerBankId, long startMillis, long endMillis);

    /**
     * 統計人工彈性授權還剩多少次
     *
     * @return -1 indicates an error.
     */
    int sumByTriesQuota(Long issuerBankId, long startMillis, long endMillis);
    int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis);

}
