package ocean.acs.models.dao;

import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.entity.ChallengeViewErrorMessageDO;

import java.util.List;

public interface ChallengeViewErrorMessageDAO {

    List<ChallengeViewErrorMessageDO> findAll() throws DatabaseException;
}
