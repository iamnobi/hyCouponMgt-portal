package com.cherri.acs_portal.controller.request;

import java.util.List;
import lombok.Data;

@Data
public class PluginRuntimePropertyCollectionDTO {

    private Long pluginId;
    private List<PluginRuntimePropertyDTO> propertyList;
}
