package com.cherri.acs_portal.component;

import com.cherri.acs_kernel.plugin.dto.IssuerPropertyDefinition;
import java.util.List;
import ocean.acs.commons.enumerator.PluginType;

public interface IPluginComponent {

    String getName();

    PluginType getPluginType();

    List<IssuerPropertyDefinition> getPropertyDefinitionList();

    IssuerPropertyDefinition findPropertyDefinitionById(int propertyId);
}
