package ocean.acs.models.oracle.repository;

import java.util.List;
import java.util.Optional;
import ocean.acs.models.oracle.entity.CardBrand;
import org.springframework.data.repository.CrudRepository;

public interface CardBrandRepository extends CrudRepository<CardBrand, String> {
    List<CardBrand> findAllByDeleteFlagFalseOrderByDisplayOrderAsc();

    Optional<CardBrand> findByNameAndDeleteFlagFalse(String name);
}
