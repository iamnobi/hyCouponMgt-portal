package ocean.acs.models.dao;

import java.util.Optional;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.entity.BankDataKeyDO;

/**
 * BankDataKeyDAO
 *
 * @author Alan Chen
 */
public interface BankDataKeyDAO {

     BankDataKeyDO save(BankDataKeyDO bankDataKeyDO, String creator) throws DatabaseException;

     Optional<BankDataKeyDO> findByIssuerBankId(long issuerBankId) throws DatabaseException;

}
