package ocean.acs.models.sql_server.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ocean.acs.models.sql_server.entity.CardBrandLogo;

@Repository
public interface CardBrandLogoRepository extends CrudRepository<CardBrandLogo, Long> {

    List<CardBrandLogo> findAllByThreeDSVersionAndDeleteFlag(int threeDSVersion, Boolean deleteFlag);

    @Query(value = "select * from card_brand_logo where three_d_s_version = :version and card_brand = :cardBrand and delete_flag = 0",
            nativeQuery = true)
    Optional<CardBrandLogo> findByCardBrandAndNotDelete(@Param("version") int version, @Param("cardBrand") String cardBrand);

    @Query(value = "select id from card_brand_logo where three_d_s_version = :version and card_brand = :cardBrand and delete_flag = 0",
            nativeQuery = true)
    Optional<Long> findIdByCardBrandAndNotDelete(@Param("version") int version, @Param("cardBrand") String cardBrand);

}
