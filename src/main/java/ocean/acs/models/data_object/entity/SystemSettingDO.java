package ocean.acs.models.data_object.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class SystemSettingDO extends OperatorInfoDO {

    private Long id;
    private String category;
    private String className;
    private String groupName;
    private String item;
    private String key;
    private String value;
    private String auditStatus;

    public SystemSettingDO(
            Long id,
            String category,
            String className,
            String groupName,
            String item,
            String key,
            String value,
            String auditStatus,
            String creator,
            Long createMillis,
            String updater,
            Long updateMillis,
            Boolean deleteFlag,
            String deleter,
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

    public static SystemSettingDO valueOf(ocean.acs.models.oracle.entity.SystemSetting e) {
        return new SystemSettingDO(
                e.getId(),
                e.getCategory(),
                e.getClassName(),
                e.getGroupName(),
                e.getItem(),
                e.getKey(),
                e.getValue(),
                e.getAuditStatus(),
                e.getCreator(),
                e.getCreateMillis(),
                e.getUpdater(),
                e.getUpdateMillis(),
                e.getDeleteFlag(),
                e.getDeleter(),
                e.getDeleteMillis());
    }
    
    public static SystemSettingDO valueOf(ocean.acs.models.sql_server.entity.SystemSetting e) {
        return new SystemSettingDO(
                e.getId(),
                e.getCategory(),
                e.getClassName(),
                e.getGroupName(),
                e.getItem(),
                e.getKey(),
                e.getValue(),
                e.getAuditStatus(),
                e.getCreator(),
                e.getCreateMillis(),
                e.getUpdater(),
                e.getUpdateMillis(),
                e.getDeleteFlag(),
                e.getDeleter(),
                e.getDeleteMillis());
    }

    public static SystemSettingDO newInstance(
            Long id,
            String category,
            String className,
            String groupName,
            String item,
            String key,
            String value,
            String auditStatus) {
        SystemSettingDO systemSettingDO = SystemSettingDO.builder().build();
        systemSettingDO.setId(id);
        systemSettingDO.setCategory(category);
        systemSettingDO.setClassName(className);
        systemSettingDO.setGroupName(groupName);
        systemSettingDO.setItem(item);
        systemSettingDO.setKey(key);
        systemSettingDO.setValue(value);
        systemSettingDO.setAuditStatus(auditStatus);

        return systemSettingDO;
    }
}
