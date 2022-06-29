package ocean.acs.models.oracle.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.IssuerTradingChannel;

@Repository
public interface IssuerTradingChannelRepository
        extends CrudRepository<IssuerTradingChannel, String> {

    @Query(value = "select * from ISSUER_TRADING_CHANNEL where issuer_bank_id = ?1 and delete_flag = 0 ",
            nativeQuery = true)
    Optional<IssuerTradingChannel> getByIssuerBankId(Long issuerBankId);
}
