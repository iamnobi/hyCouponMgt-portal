package ocean.acs.models.sql_server.dao.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.OceanExceptionForPortal;
import ocean.acs.models.dao.OtpSendingSettingDAO;
import ocean.acs.models.data_object.entity.OtpSendingSettingDO;
import ocean.acs.models.sql_server.entity.OtpSendingSetting;

@Log4j2
@Repository
@AllArgsConstructor
public class OtpSendingSettingDAOImpl implements OtpSendingSettingDAO {

    private final ocean.acs.models.sql_server.repository.OtpSendingSettingRepository repo;

    @Override
    public List<OtpSendingSettingDO> findAll() throws DatabaseException {
        try {
            return repo.findAll().stream().map(OtpSendingSettingDO::valueOf)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[findAll] unknown exception", e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public Optional<OtpSendingSettingDO> save(OtpSendingSettingDO otpSendingSettingDO) {
        try {
            OtpSendingSetting otpSendingSetting = OtpSendingSetting.valueOf(otpSendingSettingDO);
            return Optional.ofNullable(repo.save(otpSendingSetting))
                    .map(OtpSendingSettingDO::valueOf);
        } catch (Exception e) {
            log.error("[save] unknown exception", e);
            throw new OceanExceptionForPortal(ResultStatus.DB_SAVE_ERROR, e.getMessage());
        }
    }

    @Override
    public Optional<OtpSendingSettingDO> findByIdAndNotDelete(Long id) {
        try {
            return repo.findByIdAndNotDelete(id).map(OtpSendingSettingDO::valueOf);
        } catch (Exception e) {
            log.error("[findByIdAndNotDelete] unknown exception, id={}", id, e);
            throw new OceanExceptionForPortal(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public boolean existByIssuerBankIdAndNotDelete(Long issuerBankId) {
        try {
            Optional<Integer> existOpt = repo.existByIssuerBankIdAndNotDelete(issuerBankId);
            return existOpt.isPresent();
        } catch (Exception e) {
            log.error("[existByIssuerBankIdAndNotDelete] unknown exception, issuerBankId={}",
                    issuerBankId, e);
            throw new OceanExceptionForPortal(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public Optional<OtpSendingSettingDO> findByIssuerBankIdAndNotDelete(Long issuerBankId) {
        try {
            return repo.findByIssuerBankIdAndNotDelete(issuerBankId)
                    .map(OtpSendingSettingDO::valueOf);
        } catch (Exception e) {
            log.error("[findByIssuerBankIdAndNotDelete] unknown exception, issuerBankId={}",
                    issuerBankId, e);
            throw new OceanExceptionForPortal(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public Optional<Long> findIdByIssuerBankIdAndNotDelete(Long issuerBankId) {
        try {
            return repo.findIdByIssuerBankIdAndNotDelete(issuerBankId);
        } catch (Exception e) {
            log.error("[findIdByIssuerBankIdAndNotDelete] unknown exception, issuerBankId={}",
                    issuerBankId, e);
            throw new OceanExceptionForPortal(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis) {
        //TODO Implement method
        return 0;
    }

}
