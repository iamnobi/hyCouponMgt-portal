package ocean.acs.models.dao;

import java.util.List;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.entity.SdkUiTypeLogDO;

public interface SdkUiTypeLogDAO {

    List<SdkUiTypeLogDO> saveAll(List<SdkUiTypeLogDO> sdkUiTypeLogDoList) throws DatabaseException;

    List<String> findByAuthLogId(Long authLogId);

}
