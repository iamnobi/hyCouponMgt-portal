package ocean.acs.models.data_object.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ModuleSettingDO {

    private Long id;
    private String functionType;
    private Boolean auditDemand;

    public static ModuleSettingDO valueOf(ocean.acs.models.oracle.entity.ModuleSetting e) {
        return new ModuleSettingDO(e.getId(), e.getFunctionType(), e.getAuditDemand());
    }

    public static ModuleSettingDO valueOf(ocean.acs.models.sql_server.entity.ModuleSetting e) {
        return new ModuleSettingDO(e.getId(), e.getFunctionType(), e.getAuditDemand());
    }

}
