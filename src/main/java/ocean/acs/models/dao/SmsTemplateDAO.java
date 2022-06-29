package ocean.acs.models.dao;

import java.util.List;
import java.util.Optional;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.entity.SmsTemplateDO;

public interface SmsTemplateDAO {

    List<SmsTemplateDO> findAll() throws DatabaseException;

    Optional<SmsTemplateDO> findByIssuerBankId(Long issuerBankId) throws DatabaseException;

    Optional<SmsTemplateDO> findById(Long smsTemplateId) throws DatabaseException;

    SmsTemplateDO saveOrUpdate(SmsTemplateDO SmsTemplateDO) throws DatabaseException;

    int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis);


}
