package ocean.acs.models.dao;

import java.util.List;
import java.util.Optional;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.entity.SecretKeyDO;

public interface SecretKeyDAO {

    Optional<SecretKeyDO> getKey(Long issuerBankId, String cardBrand) throws DatabaseException;

    boolean existsSecretKey(Long issuerBankId, String cardBrand) throws DatabaseException;

    Optional<SecretKeyDO> findOneSecretKey(Long secretKeyId) throws DatabaseException;

    List<SecretKeyDO> findByIssuerBankId(Long issuerBankId) throws DatabaseException;

    void saveOrUpdate(SecretKeyDO secretKeyDO) throws DatabaseException;
    int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis);

}
