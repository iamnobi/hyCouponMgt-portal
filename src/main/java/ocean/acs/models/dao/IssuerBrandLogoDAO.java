package ocean.acs.models.dao;

import java.util.Optional;
import ocean.acs.models.data_object.entity.IssuerBrandLogoDO;

public interface IssuerBrandLogoDAO {

    Optional<IssuerBrandLogoDO> findByIssuerBankId(Long issuerBankId);

    IssuerBrandLogoDO save(IssuerBrandLogoDO issuerLogoDO);
    int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis);

}
