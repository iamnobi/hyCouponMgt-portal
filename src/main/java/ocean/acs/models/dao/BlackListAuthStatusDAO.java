package ocean.acs.models.dao;

import java.util.List;
import java.util.Optional;
import ocean.acs.commons.enumerator.BlackListAuthStatusCategory;
import ocean.acs.commons.enumerator.TransStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.NoSuchDataException;
import ocean.acs.models.data_object.entity.BlackListAuthStatusDO;

public interface BlackListAuthStatusDAO {

    TransStatus getAuthStatus(Long issuerBankId, BlackListAuthStatusCategory category)
            throws NoSuchDataException, DatabaseException;

    Optional<BlackListAuthStatusDO> findOneAuthStatus(Long blackListAuthStatusId)
            throws DatabaseException;

    List<BlackListAuthStatusDO> findByIssuerBank(Long issuerBankId) throws DatabaseException;

    BlackListAuthStatusDO saveOrUpdateAuthStatus(BlackListAuthStatusDO blackListAuthStatusDO)
            throws DatabaseException;

    BlackListAuthStatusDO save(BlackListAuthStatusDO blackListAuthStatusDO)
            throws DatabaseException;
    int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis);

}
