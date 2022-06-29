package ocean.acs.models.dao;

import java.util.List;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.models.data_object.entity.ModuleSettingDO;

public interface ModuleSettingDAO {

    /** 如果有設定，則依據設定；若否，則一律送審。 */
    boolean isAuditOnDemand(AuditFunctionType functionType);

    List<ModuleSettingDO> findAll();

}
