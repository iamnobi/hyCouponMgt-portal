package ocean.acs.models.sql_server.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.sql_server.entity.SecretKey;

@Repository
public interface SecretKeyRepository extends CrudRepository<SecretKey, Long> {

    @Query("select s from SecretKey s where s.issuerBankId =?1 and s.cardBrand = ?2 and deleteFlag=0")
    Optional<SecretKey> findByIssuerBankIdAndCardBrand(Long issuerBankID, String cardBrand);

    @Query("select s from SecretKey s where s.issuerBankId =?1 and s.deleteFlag=false")
    List<SecretKey> findByIssuerBankId(Long issuerBankId);

    Boolean existsByIssuerBankIdAndCardBrandAndDeleteFlag(Long issuerBankId, String cardBrand,
            boolean deleteFlag);
}
