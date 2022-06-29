package ocean.acs.models.dao;

import java.util.List;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.kernel.BlackListIpDO;

public interface BlackListIpDAO {

    List<BlackListIpDO> findAllIpBy(Long issuerBankID) throws DatabaseException;
}
