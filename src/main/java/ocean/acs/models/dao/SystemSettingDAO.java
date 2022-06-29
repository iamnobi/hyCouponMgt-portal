package ocean.acs.models.dao;

import ocean.acs.models.data_object.entity.SystemSettingDO;
import ocean.acs.models.data_object.portal.TimeoutSettingUpdateDO;

import java.util.List;
import java.util.Optional;

public interface SystemSettingDAO {

    List<SystemSettingDO> findByClassName(String className);

    Optional<SystemSettingDO> findByID(Long id);

    SystemSettingDO save(SystemSettingDO systemSettingDO);

    /** 修改 Operator ID */
    Optional<SystemSettingDO> update(SystemSettingDO systemSettingDO, String user);

    /** 修改連線逾時設定 */
    Optional<SystemSettingDO> update(TimeoutSettingUpdateDO timeoutSettingUpdateDO);

    Optional<SystemSettingDO> findByKey(String key);

    void updateAll(List<SystemSettingDO> systemSettingList);
}
