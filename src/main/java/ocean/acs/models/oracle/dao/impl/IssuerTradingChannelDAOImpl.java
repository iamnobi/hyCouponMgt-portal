package ocean.acs.models.oracle.dao.impl;

import java.util.Optional;
import ocean.acs.models.oracle.entity.BinRange;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import ocean.acs.models.dao.IssuerTradingChannelDAO;
import ocean.acs.models.data_object.entity.IssuerTradingChannelDO;
import ocean.acs.models.oracle.entity.IssuerTradingChannel;

@Repository
@AllArgsConstructor
public class IssuerTradingChannelDAOImpl implements IssuerTradingChannelDAO {

    private final ocean.acs.models.oracle.repository.IssuerTradingChannelRepository repo;
    private final DeleteIssuerBankRelativeDataHelper deleteIssuerBankRelativeDataHelper;

    @Override
    public Optional<IssuerTradingChannelDO> getByIssuerBankId(Long issuerBankId) {
        return repo.getByIssuerBankId(issuerBankId).map(IssuerTradingChannelDO::valueOf);
    }

    @Override
    public IssuerTradingChannelDO save(IssuerTradingChannelDO issuerTradingChannelDO) {
        IssuerTradingChannel issuerTradingChannel =
                IssuerTradingChannel.valueOf(issuerTradingChannelDO);
        return IssuerTradingChannelDO.valueOf(repo.save(issuerTradingChannel));
    }

    @Override
    public int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis) {
        return deleteIssuerBankRelativeDataHelper.deleteByIssuerBankId(IssuerTradingChannel.class, issuerBankId, deleter, deleteMillis);
    }

}
