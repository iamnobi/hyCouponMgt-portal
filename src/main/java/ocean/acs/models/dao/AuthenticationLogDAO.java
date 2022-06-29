package ocean.acs.models.dao;

import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.entity.AuthenticationLogDO;

import java.util.Optional;

public interface AuthenticationLogDAO {

    Optional<AuthenticationLogDO> findById(Long id) throws DatabaseException;

    Long countByTimeMillis(long startTimeMillis, long endTimeMillis);

    String getLatestMerchantName(Long issuerBankId, String acquirerMerchantID);

    int countByPanInfoIdWithinMillis(Long panInfoId, Long createMillis)
            throws DatabaseException;

    Long findLatestSuccessMillisByPanInfoId(Long panInfoId) throws DatabaseException;

    AuthenticationLogDO save(AuthenticationLogDO authLog) throws DatabaseException;

    void cleanCardExpireDateById(Long authLogId) throws DatabaseException;

}
