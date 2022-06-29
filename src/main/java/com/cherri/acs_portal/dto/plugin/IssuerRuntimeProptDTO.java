package com.cherri.acs_portal.dto.plugin;

import com.cherri.acs_kernel.plugin.dto.IssuerPropertyDefinition;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class IssuerRuntimeProptDTO extends IssuerPropertyDefinition {

    private Long issuerId;
    private String pluginId;
    private String value;

    public IssuerRuntimeProptDTO(
      Long issuerId, String pluginId, IssuerPropertyDefinition propertyDefinition) {
        super(
          propertyDefinition.getName(),
          propertyDefinition.isEncrypt(),
          propertyDefinition.getDescription());
        this.issuerId = issuerId;
        this.pluginId = pluginId;
    }
}
