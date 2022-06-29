package ocean.acs.models.oracle.dao.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.BlackListAuthStatusCategory;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.enumerator.TransStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.NoSuchDataException;
import ocean.acs.models.dao.BlackListAuthStatusDAO;
import ocean.acs.models.data_object.entity.BlackListAuthStatusDO;
import ocean.acs.models.oracle.entity.BlackListAuthStatus;

@Log4j2
@Repository
@AllArgsConstructor
public class BlackListAuthStatusDAOImpl implements BlackListAuthStatusDAO {

    private final ocean.acs.models.oracle.repository.BlackListAuthStatusRepository repo;
    private final DeleteIssuerBankRelativeDataHelper deleteIssuerBankRelativeDataHelper;

    @Override
    public TransStatus getAuthStatus(Long issuerBankId, BlackListAuthStatusCategory category)
            throws NoSuchDataException, DatabaseException {
        try {
            return repo.findByCategory(issuerBankId, category.getCode())
                    .map(authStatus -> TransStatus.codeOf(authStatus.getTransStatus()))
                    .orElseThrow(() -> {
                        String errMsg = "Please provide BlackListAuthStatus data in advance.";
                        log.warn("[getAuthStatus] {}", errMsg);
                        return new NoSuchDataException(errMsg);
                    });
        } catch (Exception e) {
            log.error("[getAuthStatus] unknown exception, issuerBankId={}, category={}",
                    issuerBankId, category.name(), e);
            throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public Optional<BlackListAuthStatusDO> findOneAuthStatus(Long blackListAuthStatusId)
            throws DatabaseException {
        try {
            return repo.findById(blackListAuthStatusId).map(BlackListAuthStatusDO::valueOf);
        } catch (Exception e) {
            log.error("[findOneAuthStatus] unknown exception, blackListAuthStatusId={}",
                    blackListAuthStatusId, e);
            throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public List<BlackListAuthStatusDO> findByIssuerBank(Long issuerBankId)
            throws DatabaseException {
        try {
            return repo.findByIssuerBankId(issuerBankId).stream()
                    .map(BlackListAuthStatusDO::valueOf).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[findByIssuerBank] uknown exceptoin, issuerBankId={}", issuerBankId, e);
            throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public BlackListAuthStatusDO saveOrUpdateAuthStatus(BlackListAuthStatusDO blackListAuthStatusDO)
            throws DatabaseException {
        try {
            BlackListAuthStatus blackListAuthStatus =
                    BlackListAuthStatus.valueOf(blackListAuthStatusDO);
            return BlackListAuthStatusDO.valueOf(repo.save(blackListAuthStatus));
        } catch (Exception e) {
            log.error("[saveOrUpdateAuthStatus] unknown exception, BlackListAuthStatus={}",
                    blackListAuthStatusDO, e);
            throw new DatabaseException(ResultStatus.DB_SAVE_ERROR, e.getMessage());
        }
    }

    @Override
    public BlackListAuthStatusDO save(BlackListAuthStatusDO blackListAuthStatusDO)
            throws DatabaseException {
        try {
            BlackListAuthStatus blackListAuthStatus =
                    BlackListAuthStatus.valueOf(blackListAuthStatusDO);
            return BlackListAuthStatusDO.valueOf(repo.save(blackListAuthStatus));
        } catch (Exception e) {
            log.error("[save] unknown exception, BlackListAuthStatus={}",
                StringUtils.normalizeSpace(blackListAuthStatusDO.toString()), e);
            throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis) {
        return deleteIssuerBankRelativeDataHelper.deleteByIssuerBankId(BlackListAuthStatus.class, issuerBankId, deleter, deleteMillis);
    }

}
