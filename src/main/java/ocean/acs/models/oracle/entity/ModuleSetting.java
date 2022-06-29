package ocean.acs.models.oracle.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
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
    @GenericGenerator(name = "module_setting_seq_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name",
                            value = "MODULE_SETTING_ID_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1000"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "module_setting_seq_generator")
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

