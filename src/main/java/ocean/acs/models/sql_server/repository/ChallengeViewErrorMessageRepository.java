package ocean.acs.models.sql_server.repository;

import ocean.acs.models.sql_server.entity.ChallengeViewErrorMessage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeViewErrorMessageRepository extends CrudRepository<ChallengeViewErrorMessage, Long> {

    @Query("select c from ChallengeViewErrorMessage c where deleteFlag = false order by createMillis asc")
    @Override
    Iterable<ChallengeViewErrorMessage> findAll();
}
