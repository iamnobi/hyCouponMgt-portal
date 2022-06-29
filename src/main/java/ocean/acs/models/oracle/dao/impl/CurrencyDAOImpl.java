package ocean.acs.models.oracle.dao.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.models.dao.CurrencyDAO;
import ocean.acs.models.data_object.entity.CurrencyDO;
import ocean.acs.models.oracle.entity.Currency;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Log4j2
@Repository
@AllArgsConstructor
public class CurrencyDAOImpl implements CurrencyDAO {
    private final ocean.acs.models.oracle.repository.CurrencyRepository repository;

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
