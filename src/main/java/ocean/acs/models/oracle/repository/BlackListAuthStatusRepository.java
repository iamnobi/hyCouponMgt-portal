package ocean.acs.models.oracle.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.BlackListAuthStatus;

@Repository
public interface BlackListAuthStatusRepository extends CrudRepository<BlackListAuthStatus, Long> {

    @Query("select b from BlackListAuthStatus b where b.issuerBankId = ?1 and b.deleteFlag = 0")
    List<BlackListAuthStatus> findByIssuerBankId(Long issuerBankId);

    @Query("select auth from BlackListAuthStatus auth where issuerBankId = :issuerBankId and auth.deleteFlag = 0 and auth.category = :category")
    Optional<BlackListAuthStatus> findByCategory(@Param("issuerBankId") Long issuerBankId,
            @Param("category") Integer category);
}
