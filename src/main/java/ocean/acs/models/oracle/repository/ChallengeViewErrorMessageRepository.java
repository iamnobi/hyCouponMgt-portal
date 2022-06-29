package ocean.acs.models.oracle.repository;

import ocean.acs.models.oracle.entity.ChallengeViewErrorMessage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeViewErrorMessageRepository extends CrudRepository<ChallengeViewErrorMessage, Long> {

    @Query("select c from ChallengeViewErrorMessage c where deleteFlag = false order by createMillis asc")
    @Override
    Iterable<ChallengeViewErrorMessage> findAll();
}
