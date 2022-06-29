package ocean.acs.models.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import ocean.acs.models.data_object.entity.AbnormalTransactionDO;
import ocean.acs.models.data_object.portal.AbnormalTransactionQueryDO;

public interface AbnormalTransactionDAO {

    List<AbnormalTransactionDO> saveAll(List<AbnormalTransactionDO> abnormalTransactionDoList);

    Optional<AbnormalTransactionDO> findByYearAndMonthAndDayAndIssuerBankIdAndMerchantId(int year,
            int month, int day, Long issuerBankId, String merchantId);

    /** 查詢商店異常交易統計 */
    Optional<Page<AbnormalTransactionDO>> query(AbnormalTransactionQueryDO queryDO);

}
