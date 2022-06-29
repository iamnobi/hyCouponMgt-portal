package com.cherri.acs_portal.controller.request;

import lombok.Data;

@Data
public class PluginRuntimePropertyDTO {

    private String name;
    private Integer propertyId;
    private String value;
}
