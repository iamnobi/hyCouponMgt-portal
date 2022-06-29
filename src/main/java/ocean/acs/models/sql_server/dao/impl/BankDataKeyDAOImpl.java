package ocean.acs.models.sql_server.dao.impl;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.dao.BankDataKeyDAO;
import ocean.acs.models.data_object.entity.BankDataKeyDO;
import ocean.acs.models.sql_server.entity.BankDataKey;

@Slf4j
@Repository
@AllArgsConstructor
public class BankDataKeyDAOImpl implements BankDataKeyDAO {

    private final ocean.acs.models.sql_server.repository.BankDataKeyRepository bankDataKeyRepository;

    @Override
    public Optional<BankDataKeyDO> findByIssuerBankId(long issuerBankId) throws DatabaseException {
        try {
            Optional<BankDataKey> bankDataKeyOptional =
              bankDataKeyRepository.findByIssuerBankId(issuerBankId);
            if (bankDataKeyOptional.isPresent()) {
                return Optional.of(BankDataKeyDO.valueOf(bankDataKeyOptional.get()));
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            log.error("[findByIssuerBankId] error", e);
            throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public BankDataKeyDO save(BankDataKeyDO bankDataKey, String creator) throws DatabaseException {

        bankDataKey.setCreator(creator);
        bankDataKey.setCreateMillis(System.currentTimeMillis());
        bankDataKey.setUpdater(creator);
        bankDataKey.setUpdateMillis(System.currentTimeMillis());

        try {
            return BankDataKeyDO
              .valueOf(bankDataKeyRepository.save(BankDataKey.valueOf(bankDataKey)));
        } catch (Exception e) {
            log.error("[save] error", e);
            throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }
}
