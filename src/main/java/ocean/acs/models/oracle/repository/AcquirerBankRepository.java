package ocean.acs.models.oracle.repository;

import ocean.acs.models.oracle.entity.AcquirerBank;
import ocean.acs.models.oracle.entity.IssuerBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AcquirerBankRepository extends JpaRepository<AcquirerBank, Long> {
    @Query(value = "SELECT THREE_D_S_OPERATOR_ID_SEQ.nextval FROM dual", nativeQuery = true)
    Long getThreeDSOperatorIdNextVal();

    //00003 allow control asc/3ds oper id by user
    @Query("select i from AcquirerBank i where threeDSOperatorId = ?1")
    Optional<AcquirerBank> findBythreeDSOperatorId(String threeDSOperatorId);
}
