package ocean.acs.models.oracle.dao.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.exception.OceanExceptionForPortal;
import ocean.acs.models.dao.SystemSettingDAO;
import ocean.acs.models.data_object.entity.SystemSettingDO;
import ocean.acs.models.data_object.portal.TimeoutSettingUpdateDO;
import ocean.acs.models.oracle.entity.SystemSetting;

@Log4j2
@Repository
@AllArgsConstructor
public class SystemSettingDAOImpl implements SystemSettingDAO {

    private final ocean.acs.models.oracle.repository.SystemSettingRepository repo;

    @Override
    public List<SystemSettingDO> findByClassName(String className) {
        return repo.findByClassName(className).stream().map(SystemSettingDO::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<SystemSettingDO> findByID(Long id) {
        return repo.findById(id).map(SystemSettingDO::valueOf);
    }

    @Override
    public SystemSettingDO save(SystemSettingDO systemSettingDO) {
        SystemSetting systemSetting = SystemSetting.valueOf(systemSettingDO);
        return SystemSettingDO.valueOf(repo.save(systemSetting));
    }

    @Override
    public Optional<SystemSettingDO> update(SystemSettingDO systemSettingDO, String user) {
        SystemSetting systemSetting = repo.findById(systemSettingDO.getId()).map(e -> {
            e.setValue(systemSettingDO.getValue());
            e.setAuditStatus(systemSettingDO.getAuditStatus());
            e.setUpdater(user);
            e.setUpdateMillis(System.currentTimeMillis());
            return e;
        }).map(repo::save).orElseThrow(() -> {
            log.error(
                    "[update] Failed in update system setting due to unknown system setting content with id={}",
                    systemSettingDO.getId());
            return new OceanExceptionForPortal("Command failed in missing target content.");
        });

        return Optional.of(SystemSettingDO.valueOf(systemSetting));
    }

    @Override
    public Optional<SystemSettingDO> update(TimeoutSettingUpdateDO timeoutSettingUpdateDo) {
        SystemSetting systemSetting = repo.findById(timeoutSettingUpdateDo.getId()).map(e -> {
            e.setValue(timeoutSettingUpdateDo.getValue());
            e.setAuditStatus(timeoutSettingUpdateDo.getAuditStatus().getSymbol());
            e.setUpdater(timeoutSettingUpdateDo.getUser());
            e.setUpdateMillis(System.currentTimeMillis());
            return e;
        }).map(repo::save).orElseThrow(() -> {
            log.error(
                    "[update] Failed in update system setting due to unknown system setting content with id={}",
                    timeoutSettingUpdateDo.getId());
            return new OceanExceptionForPortal("Command failed in missing target content.");
        });

        return Optional.of(SystemSettingDO.valueOf(systemSetting));
    }

    @Override
    public Optional<SystemSettingDO> findByKey(String key) {
        return repo.findByKey(key).map(SystemSettingDO::valueOf);
    }

    @Override
    public void updateAll(List<SystemSettingDO> systemSettingList) {
        List<SystemSetting> entities = systemSettingList.stream()
                .map(SystemSetting::valueOf)
                .collect(Collectors.toList());
        repo.saveAll(entities);
    }
}
