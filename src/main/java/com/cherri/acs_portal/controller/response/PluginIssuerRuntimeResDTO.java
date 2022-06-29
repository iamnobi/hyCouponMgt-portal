package com.cherri.acs_portal.controller.response;

import com.cherri.acs_portal.dto.plugin.IssuerRuntimeProptDTO;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import ocean.acs.commons.enumerator.PluginType;
import org.apache.commons.lang3.StringUtils;

@Data
public class PluginIssuerRuntimeResDTO {

    private Long issuerId;
    private String pluginId;
    private String pluginName;
    private List<IssuerRuntimeProptDTO> propertyList;

    @Builder
    public PluginIssuerRuntimeResDTO(Long issuerId, PluginType pluginType, String pluginName) {
        this.issuerId = issuerId;
        this.pluginId = pluginType.getPluginId();

        if (StringUtils.isEmpty(pluginName)) {
            this.pluginName = pluginType.name();
        } else {
            this.pluginName = pluginName;
        }
    }
}
