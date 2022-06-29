package ocean.acs.models.sql_server.repository;

import java.util.List;
import java.util.Optional;
import ocean.acs.models.sql_server.entity.CardBrand;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardBrandRepository extends CrudRepository<CardBrand, String> {

    List<CardBrand> findAllByDeleteFlagFalseOrderByDisplayOrderAsc();

    Optional<CardBrand> findByNameAndDeleteFlagFalse(String name);
}
