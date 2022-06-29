package ocean.acs.models.sql_server.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ocean.acs.models.data_object.entity.ModuleSettingDO;
import ocean.acs.models.entity.DBKey;

@DynamicUpdate
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = DBKey.TABLE_MODULE_SETTING)
public class ModuleSetting implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBKey.COL_MODULE_SETTING_ID)
    private Long id;

    @NonNull
    @Column(name = DBKey.COL_MODULE_SETTING_FUNCTION_TYPE)
    private String functionType;

    @NonNull
    @Column(name = DBKey.COL_MODULE_SETTING_AUDIT_DEMAND)
    private Boolean auditDemand = true;

    public static ModuleSetting valueOf(ModuleSettingDO d) {
        return new ModuleSetting(d.getId(), d.getFunctionType(), d.getAuditDemand());
    }

}

