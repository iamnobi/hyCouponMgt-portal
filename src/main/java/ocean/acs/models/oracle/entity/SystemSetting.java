package ocean.acs.models.oracle.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.models.data_object.entity.SystemSettingDO;
import ocean.acs.models.entity.DBKey;
import ocean.acs.models.entity.OperatorInfo;

@DynamicUpdate
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = DBKey.TABLE_SYSTEM_SETTING)
public class SystemSetting extends OperatorInfo {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "system_setting_seq_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name",
                            value = "SYSTEM_SETTING_ID_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "system_setting_seq_generator")
    @Column(name = DBKey.COL_SYSTEM_SETTING_ID)
    private Long id;

    @Size(max = 30, message = "{unaccepted.value}")
    @Column(name = DBKey.COL_SYSTEM_SETTING_CATEGORY)
    private String category;

    @Size(max = 50, message = "{unaccepted.value}")
    @Column(name = DBKey.COL_SYSTEM_SETTING_CLASS_NAME)
    private String className;

    @Size(max = 50, message = "{unaccepted.value}")
    @Column(name = DBKey.COL_SYSTEM_SETTING_GROUP_NAME)
    private String groupName;

    @Size(max = 50)
    @Column(name = DBKey.COL_SYSTEM_SETTING_ITEM)
    private String item;

    @NotBlank(message = "{column.notempty}")
    @Column(name = DBKey.COL_SYSTEM_SETTING_KY)
    private String key;

    @NotBlank(message = "{column.notempty}")
    @Size(max = 2048, message = "{unaccepted.value}")
    @Column(name = DBKey.COL_SYSTEM_SETTING_VAL)
    private String value;

    @Column(name = DBKey.COL_AUDIT_STATUS)
    private String auditStatus;

    public SystemSetting(Long id, String category, String className, String groupName, String item,
            String key, String value, String auditStatus, String creator, Long createMillis,
            String updater, Long updateMillis, Boolean deleteFlag, String deleter,
            Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.category = category;
        this.className = className;
        this.groupName = groupName;
        this.item = item;
        this.key = key;
        this.value = value;
        this.auditStatus = auditStatus;
    }

    public static SystemSetting valueOf(SystemSettingDO d) {
        return new SystemSetting(d.getId(), d.getCategory(), d.getClassName(), d.getGroupName(),
                d.getItem(), d.getKey(), d.getValue(), d.getAuditStatus(), d.getCreator(),
                d.getCreateMillis(), d.getUpdater(), d.getUpdateMillis(), d.getDeleteFlag(),
                d.getDeleter(), d.getDeleteMillis());
    }

}
