package ocean.acs.models.oracle.dao.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.dao.SmsTemplateDAO;
import ocean.acs.models.data_object.entity.SmsTemplateDO;
import ocean.acs.models.oracle.entity.SmsTemplate;

@Log4j2
@Repository
@AllArgsConstructor
public class SmsTemplateDAOImpl implements SmsTemplateDAO {

    private final ocean.acs.models.oracle.repository.SmsTemplateRepository repo;
    private final DeleteIssuerBankRelativeDataHelper deleteIssuerBankRelativeDataHelper;

    @Override
    public List<SmsTemplateDO> findAll() throws DatabaseException {
        try {
            return repo.findAll().stream().map(SmsTemplateDO::valueOf).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[findAll] unknown exception", e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public Optional<SmsTemplateDO> findByIssuerBankId(Long issuerBankId) throws DatabaseException {
        try {
            List<SmsTemplate> result = repo.findByIssuerBankId(issuerBankId);
            if (result == null || result.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(SmsTemplateDO.valueOf(result.get(0)));
        } catch (Exception e) {
            log.error("[findByIssuerBankId] unknown exception", e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public Optional<SmsTemplateDO> findById(Long smsTemplateId) throws DatabaseException {
        try {
            return repo.findById(smsTemplateId).map(SmsTemplateDO::valueOf);
        } catch (Exception e) {
            log.error("[findById] unknown exception, smsTemplateId={}", smsTemplateId, e);
            throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public SmsTemplateDO saveOrUpdate(SmsTemplateDO smsTemplateDO) throws DatabaseException {
        try {
            SmsTemplate smsTemplate = SmsTemplate.valueOf(smsTemplateDO);
            return SmsTemplateDO.valueOf(repo.save(smsTemplate));
        } catch (Exception e) {
            log.error("[saveOrUpdate] unknown exception", e);
            throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis) {
        return deleteIssuerBankRelativeDataHelper.deleteByIssuerBankId(SmsTemplate.class, issuerBankId, deleter, deleteMillis);
    }

}
