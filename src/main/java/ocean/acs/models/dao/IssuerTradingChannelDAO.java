package ocean.acs.models.dao;

import java.util.Optional;
import ocean.acs.models.data_object.entity.IssuerTradingChannelDO;

public interface IssuerTradingChannelDAO {

    Optional<IssuerTradingChannelDO> getByIssuerBankId(Long issuerBankId);

    IssuerTradingChannelDO save(IssuerTradingChannelDO issuerTradingChannelDO);
    int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis);


}
