package com.cherri.acs_portal.dto.system;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ocean.acs.models.data_object.entity.SystemSettingDO;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SystemSettingDTO implements Serializable {

    private Long id;

    private String category;

    private String className;

    private String groupName;

    private String item;

    private String key;

    private String value;

    private String auditStatus;

    public static SystemSettingDTO valueOf(SystemSettingDO entity) {
        return new SystemSettingDTO(
          entity.getId(),
          entity.getCategory(),
          entity.getClassName(),
          entity.getGroupName(),
          entity.getItem(),
          entity.getKey(),
          entity.getValue(),
          entity.getAuditStatus());
    }
}
