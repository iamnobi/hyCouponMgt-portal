package ocean.acs.models.oracle.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.CaCertificate;

@Repository
public interface CaCertificateRepository extends CrudRepository<CaCertificate, Long> {

    Optional<CaCertificate> findByIdAndDeleteFlagFalse(Long id);

    @Override
    @Query(value = "select c from CaCertificate c where deleteFlag = false")
    List<CaCertificate> findAll();

    List<CaCertificate> findByCardBrandAndDeleteFlagFalseOrderByExpireMillisDesc(String cardBrand);

    int countByCardBrandAndDeleteFlagFalse(String cardBrand);

    Boolean existsByCardBrandAndDeleteFlagFalse(String cardBrand);

}
