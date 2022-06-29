package ocean.acs.models.oracle.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.ChallengeViewMessage;

@Repository
public interface ChallengeViewMessageRepository extends CrudRepository<ChallengeViewMessage, Long> {

    @Override
    @Query("select c from ChallengeViewMessage c where deleteFlag=0 order by updateMillis, createMillis desc")
    List<ChallengeViewMessage> findAll();

    @Query("select c from ChallengeViewMessage c where issuerBankId = ?1 and deleteFlag=0")
    List<ChallengeViewMessage> findByIssuerBankId(Long issuerBankId);

    List<ChallengeViewMessage> findByIssuerBankIdAndLanguageCodeAndDeleteFlagFalse(Long issuerBankId, String languageCode);

}
