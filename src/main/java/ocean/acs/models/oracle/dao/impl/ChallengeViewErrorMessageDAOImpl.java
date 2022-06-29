package ocean.acs.models.oracle.dao.impl;

import lombok.AllArgsConstructor;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.dao.ChallengeViewErrorMessageDAO;
import ocean.acs.models.data_object.entity.ChallengeViewErrorMessageDO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
@AllArgsConstructor
public class ChallengeViewErrorMessageDAOImpl implements ChallengeViewErrorMessageDAO {

    private final ocean.acs.models.oracle.repository.ChallengeViewErrorMessageRepository repo;

    @Override
    public List<ChallengeViewErrorMessageDO> findAll() throws DatabaseException {
        return StreamSupport.stream(repo.findAll().spliterator(), false)
                .map(ChallengeViewErrorMessageDO::valueOf)
                .collect(Collectors.toList());
    }
}
