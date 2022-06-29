package com.cherri.acs_kernel.plugin;

import com.cherri.acs_kernel.plugin.dto.IssuerPropertyDefinition;
import java.util.List;

public interface IPlugin {
    String getPluginName();
    List<IssuerPropertyDefinition> getIssuerPropertyDefinitionList();
}
