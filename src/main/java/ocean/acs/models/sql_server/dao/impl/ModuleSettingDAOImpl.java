package ocean.acs.models.sql_server.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.models.dao.ModuleSettingDAO;
import ocean.acs.models.data_object.entity.ModuleSettingDO;
import ocean.acs.models.sql_server.entity.ModuleSetting;

@Repository
@AllArgsConstructor
public class ModuleSettingDAOImpl implements ModuleSettingDAO {

    private final ocean.acs.models.sql_server.repository.ModuleSettingRepository repo;

    @Override
    public boolean isAuditOnDemand(AuditFunctionType functionType) {
        List<ModuleSetting> moduleSettingList =
          repo.findByFunctionType(functionType.getTypeSymbol());
        if (null != moduleSettingList && moduleSettingList.size() > 0) {
            return moduleSettingList.get(0).getAuditDemand();
        } else {
            return true;
        }
    }

    @Override
    public List<ModuleSettingDO> findAll() {
        List<ModuleSetting> list = new ArrayList<>();
        repo.findAll().forEach(list::add);
        return list.stream().map(ModuleSettingDO::valueOf).collect(Collectors.toList());
    }

}
