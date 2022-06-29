package ocean.acs.models.oracle.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.ClassicRbaCheck;

@Repository
public interface ClassicRbaCheckRepository extends JpaRepository<ClassicRbaCheck, Long> {

    @Override
    @Query("select c from  ClassicRbaCheck c where deleteFlag=0 order by createMillis desc")
    List<ClassicRbaCheck> findAll();

    Optional<ClassicRbaCheck> findByIssuerBankIdAndDeleteFlagFalse(long issuerBankId);

}
