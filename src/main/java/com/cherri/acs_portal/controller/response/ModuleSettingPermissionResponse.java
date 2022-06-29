package com.cherri.acs_portal.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModuleSettingPermissionResponse {

    @JsonProperty("permission")
    private String permission;

    @JsonProperty("auditDemand")
    private Boolean auditDemand;

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        final ModuleSettingPermissionResponse mspr = (ModuleSettingPermissionResponse) obj;
        return this.permission.equals(mspr.permission);
    }
}
