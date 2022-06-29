package com.cherri.acs_portal.component;

import com.cherri.acs_kernel.plugin.dto.IssuerPropertyDefinition;
import com.cherri.acs_portal.model.dao.IssuerRuntimePropertyDAOWrapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.PluginType;
import ocean.acs.models.data_object.entity.IssuerRuntimePropDO;
import org.springframework.stereotype.Component;

@Log4j2
@Component("PortalPluginBase")
public class PluginBase implements IPluginComponent {

    private final IssuerRuntimePropertyDAOWrapper runtimePropertyDAO;

    public PluginBase(IssuerRuntimePropertyDAOWrapper runtimePropertyDAO) {
        this.runtimePropertyDAO = runtimePropertyDAO;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public PluginType getPluginType() {
        return null;
    }

    @Override
    public List<IssuerPropertyDefinition> getPropertyDefinitionList() {
        return null;
    }

    @Override
    public IssuerPropertyDefinition findPropertyDefinitionById(int propertyId) {
        return null;
    }

    protected Map<IssuerPropertyDefinition, String> getIssuerRuntimeProperty(Long issuerBankId) {
        Map<IssuerPropertyDefinition, String> propMap = new HashMap<>();
        List<IssuerRuntimePropDO> propValueDOList =
          runtimePropertyDAO.getPropertyListOfIssuer(issuerBankId, getPluginType());

        if (propValueDOList == null || propValueDOList.isEmpty())
            return propMap;

        for (IssuerRuntimePropDO propValue : propValueDOList) {
            IssuerPropertyDefinition propDef = findPropertyDefinitionById(
              propValue.getPropertyId());
            if (propDef == null) {
                log.warn(
                  "Plugin Value miss matched. There might be some property has been removed or deprecated in plugin setting.");
                continue;
            }

            propMap.put(propDef, propValue.getValue());
        }
        return propMap;
    }
}
