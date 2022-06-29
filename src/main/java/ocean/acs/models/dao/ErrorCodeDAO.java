package ocean.acs.models.dao;

import java.util.List;
import java.util.Optional;
import ocean.acs.models.data_object.entity.ErrorCodeMappingDO;
import ocean.acs.models.data_object.entity.ErrorIssueGroupDO;

public interface ErrorCodeDAO {

    List<ErrorCodeMappingDO> getErrorCodeMappingByErrorGroupId(Long id);

    List<ErrorIssueGroupDO> getGroupList();

    Optional<ErrorIssueGroupDO> getByGroupId(Long id);

    ErrorIssueGroupDO updateGroupMessage(ErrorIssueGroupDO updateIssueGroupDO);

}
