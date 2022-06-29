package ocean.acs.models.dao;

import ocean.acs.models.data_object.entity.CurrencyDO;

import java.util.List;
import java.util.Optional;

public interface CurrencyDAO {
    Optional<CurrencyDO> findByCode(String code);

    List<CurrencyDO> findAll();

    List<CurrencyDO> findByUseExchangeCodeNotNull();
}
