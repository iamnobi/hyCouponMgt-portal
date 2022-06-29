package ocean.acs.models.dao;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import ocean.acs.models.data_object.entity.IssuerHandingFeeDO;

public interface IssuerHandingFeeDAO {

    Optional<IssuerHandingFeeDO> getByIssuerBankId(Long issuerBankId);

    List<IssuerHandingFeeDO> listByIssuerBankIdIn(Set<Long> issuerBankIdSet);

    Optional<IssuerHandingFeeDO> findOneById(Long issuerHandingFeeId);

    Boolean save(IssuerHandingFeeDO issuerHandingFeeDO);
    int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis);

}
