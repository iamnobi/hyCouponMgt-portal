package ocean.acs.models.dao;

import java.util.List;
import java.util.Optional;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.entity.OtpSendingSettingDO;

public interface OtpSendingSettingDAO {

    List<OtpSendingSettingDO> findAll() throws DatabaseException;

    Optional<OtpSendingSettingDO> save(OtpSendingSettingDO otpSendingSettingDO);

    Optional<OtpSendingSettingDO> findByIdAndNotDelete(Long id);

    boolean existByIssuerBankIdAndNotDelete(Long issuerBankId);

    Optional<OtpSendingSettingDO> findByIssuerBankIdAndNotDelete(Long issuerBankId);

    Optional<Long> findIdByIssuerBankIdAndNotDelete(Long issuerBankId);
    int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis);

}
