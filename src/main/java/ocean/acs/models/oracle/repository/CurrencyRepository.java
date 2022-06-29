package ocean.acs.models.oracle.repository;

import ocean.acs.models.oracle.entity.Currency;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyRepository extends CrudRepository<Currency, String> {

    @Query("select c from Currency c where usdExchangeRate is not null order by alpha asc")
    List<Currency> findByUsdExchangeRateNotNull();
}
