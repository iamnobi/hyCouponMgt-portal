package ocean.acs.models.sql_server.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.sql_server.entity.CaCertificate;

@Repository
public interface CaCertificateRepository extends CrudRepository<CaCertificate, Long> {

    Optional<CaCertificate> findByIdAndDeleteFlagFalse(Long id);

    @Override
    @Query("select c from CaCertificate c where deleteFlag = false")
    List<CaCertificate> findAll();

    List<CaCertificate> findByCardBrandAndDeleteFlagFalseOrderByExpireMillisDesc(String cardBrand);

    int countByCardBrandAndDeleteFlagFalse(String cardBrand);

    Boolean existsByCardBrandAndDeleteFlagFalse(String cardBrand);

}
