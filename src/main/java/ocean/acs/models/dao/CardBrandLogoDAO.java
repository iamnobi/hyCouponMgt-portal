package ocean.acs.models.dao;

import java.util.List;
import java.util.Optional;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.entity.CardBrandLogoDO;

public interface CardBrandLogoDAO {
    
    List<CardBrandLogoDO> findAll(int version) throws DatabaseException;

    CardBrandLogoDO save(CardBrandLogoDO cardBrandLogoDO);

    Optional<CardBrandLogoDO> findByCardBrandAndNotDelete(int version, String cardBrand);

    Optional<Long> findIdByCardBrandAndNotDelete(int version, String cardBrand);

    Optional<CardBrandLogoDO> findById(Long id);

}
