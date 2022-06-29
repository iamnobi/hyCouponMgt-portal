package ocean.acs.models.sql_server.dao.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.dao.AuthenticationLogDAO;
import ocean.acs.models.data_object.entity.AuthenticationLogDO;
import ocean.acs.models.sql_server.entity.AuthenticationLog;

@Log4j2
@Repository
@AllArgsConstructor
public class AuthenticationLogDAOImpl implements AuthenticationLogDAO {

    private final ocean.acs.models.sql_server.repository.AuthenticationLogRepository repo;

    @Override
    public Optional<AuthenticationLogDO> findById(Long id) throws DatabaseException {
        try {
            Optional<AuthenticationLog> authLogOpt = repo.findById(id);
            return authLogOpt.map(AuthenticationLogDO::valueOf);
        } catch (Exception e) {
            log.error("[findById] unknown exception, id={}", id, e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public Long countByTimeMillis(long startTimeMillis, long endTimeMillis) {
        return repo.countByCreateMillis(startTimeMillis, endTimeMillis);
    }

    @Override
    public String getLatestMerchantName(Long issuerBankId, String acquirerMerchantID) {
        try {
            return repo
                    .findFirstByIssuerBankIdAndAcquirerMerchantIDOrderByCreateMillisDesc(
                            issuerBankId, acquirerMerchantID)
                    .map(AuthenticationLog::getMerchantName).orElse(null);
        } catch (Exception e) {
            log.error(
                    "[getLatestMerchantName] unknown exception, issuerBankId={}, acquirerMerchantID={}",
                    issuerBankId, acquirerMerchantID, e);
        }
        return null;
    }

    @Override
    public int countByPanInfoIdWithinMillis(Long panInfoId, Long createMillis)
            throws DatabaseException {
        try {
            Objects.requireNonNull(panInfoId);
            Objects.requireNonNull(createMillis);
            Integer result = repo.countByPanInfoIdWithinMillis(panInfoId, createMillis);
            return result == null ? 0 : result;
        } catch (Exception e) {
            log.error("[findByPanInfoIdWithinMillis] system error, panInfoId={}", panInfoId);
            log.error(e.getMessage(), e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public Long findLatestSuccessMillisByPanInfoId(Long panInfoId) throws DatabaseException {
        try {
            return repo.findLatestSuccessMillisByPanInfoId(panInfoId);
        } catch (Exception e) {
            log.error("[findLatestSuccessMillisByPanInfoId] system error, panInfoId={}", panInfoId);
            log.error(e.getMessage(), e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public AuthenticationLogDO save(AuthenticationLogDO authLogDO) throws DatabaseException {
        try {
            AuthenticationLog authLog = AuthenticationLog.valueOf(authLogDO);
            authLog = repo.save(authLog);
            return AuthenticationLogDO.valueOf(authLog);
        } catch (Exception e) {
            log.error("[save] unknown exception, authLog={}", authLogDO, e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public void cleanCardExpireDateById(Long authLogId) throws DatabaseException {
        try {
            repo.cleanCardExpireDateById(authLogId);
        } catch (Exception e) {
            log.error("[cleanCardExpireDateById] unknown exception, authLogId={}", authLogId, e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

}
