package ocean.acs.models.sql_server.dao.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import ocean.acs.models.dao.CurrencyDAO;
import ocean.acs.models.data_object.entity.CurrencyDO;
import ocean.acs.models.sql_server.entity.Currency;

@Repository
@AllArgsConstructor
public class CurrencyDAOImpl implements CurrencyDAO {
    
    private final ocean.acs.models.sql_server.repository.CurrencyRepository repository;

    @Override
    public Optional<CurrencyDO> findByCode(String code) {
        if (code == null) {
            return Optional.empty();
        }
        return repository.findById(code).map(CurrencyDO::valueOf);
    }

    @Override
    public List<CurrencyDO> findAll() {
        Iterable<Currency> result = repository.findAll();
        return StreamSupport.stream(result.spliterator(), false)
                .map(CurrencyDO::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    public List<CurrencyDO> findByUseExchangeCodeNotNull() {
        Iterable<Currency> result = repository.findByUsdExchangeRateNotNull();
        return StreamSupport.stream(result.spliterator(), false)
                .map(CurrencyDO::valueOf)
                .collect(Collectors.toList());
    }
}
