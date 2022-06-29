package ocean.acs.models.oracle.dao.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.dao.SecretKeyDAO;
import ocean.acs.models.data_object.entity.SecretKeyDO;
import ocean.acs.models.oracle.entity.SecretKey;

@Log4j2
@Repository
@AllArgsConstructor
public class SecretKeyDAOImpl implements SecretKeyDAO {

    private final ocean.acs.models.oracle.repository.SecretKeyRepository repo;
    private final DeleteIssuerBankRelativeDataHelper deleteIssuerBankRelativeDataHelper;

    @Override
    public Optional<SecretKeyDO> getKey(Long issuerBankId, String cardBrand)
            throws DatabaseException {
        try {
            Optional<SecretKey> opt = repo.findByIssuerBankIdAndCardBrand(issuerBankId, cardBrand);
            return opt.map(SecretKeyDO::valueOf);
        } catch (Exception e) {
            log.error("[getKey] unknown exception, issuerBankId={}, cardBrand={}",
                issuerBankId,
                StringUtils.normalizeSpace(cardBrand), e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public boolean existsSecretKey(Long issuerBankId, String cardBrand)
            throws DatabaseException {
        try {
            return repo.existsByIssuerBankIdAndCardBrandAndDeleteFlag(issuerBankId, cardBrand,
                    false);
        } catch (Exception e) {
            log.error("[existsSecretKey] unknown exception, issuerBankId={}, cardBrand={}",
                issuerBankId,
                StringUtils.normalizeSpace(cardBrand), e);
            throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public Optional<SecretKeyDO> findOneSecretKey(Long secretKeyId) throws DatabaseException {
        try {
            return repo.findById(secretKeyId).map(SecretKeyDO::valueOf);
        } catch (Exception e) {
            log.error("[findOneSecretKey] unknown exception, secretKeyId={}", secretKeyId, e);
            throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public List<SecretKeyDO> findByIssuerBankId(Long issuerBankId) throws DatabaseException {
        try {
            List<SecretKey> resultList = repo.findByIssuerBankId(issuerBankId);
            if (resultList == null || resultList.isEmpty()) {
                return Collections.emptyList();
            }
            return resultList.stream().map(SecretKeyDO::valueOf).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[findByIssuerBankId] unknown exception, issuerBankId={}", issuerBankId, e);
            throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public void saveOrUpdate(SecretKeyDO secretKeyDO) throws DatabaseException {
        try {
            repo.save(SecretKey.valueOf(secretKeyDO));
        } catch (Exception e) {
            log.error("[saveOrUpdate] unknown exception, SecretKey={}", secretKeyDO, e);
            throw new DatabaseException(ResultStatus.DB_SAVE_ERROR, e.getMessage());
        }
    }

    @Override
    public int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis) {
        return deleteIssuerBankRelativeDataHelper.deleteByIssuerBankId(SecretKey.class, issuerBankId, deleter, deleteMillis);
    }

}
