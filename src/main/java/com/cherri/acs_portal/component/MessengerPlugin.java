package com.cherri.acs_portal.component;

import com.cherri.acs_kernel.plugin.MessengerInterface;
import com.cherri.acs_kernel.plugin.dto.IssuerPropertyDefinition;
import com.cherri.acs_kernel.plugin.dto.ResultDTO;
import com.cherri.acs_kernel.plugin.dto.messenger.invoke.InitializeInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.messenger.invoke.SendAuthInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.messenger.result.SendAuthResultDTO;
import com.cherri.acs_portal.model.dao.IssuerRuntimePropertyDAOWrapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.PluginType;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("PortalMessengerPlugin")
@Log4j2
public class MessengerPlugin extends PluginBase {

    @Getter
    private MessengerInterface pluginImpl;
    private final Map<Integer, IssuerPropertyDefinition> propertyDefinitionMap;
    private final IssuerRuntimePropertyDAOWrapper runtimePropertyDAO;

    @Autowired
    public MessengerPlugin(
      MessengerInterface pluginImpl, IssuerRuntimePropertyDAOWrapper runtimePropertyDAO) {
        super(runtimePropertyDAO);

        this.pluginImpl = pluginImpl;
        this.runtimePropertyDAO = runtimePropertyDAO;
        List<IssuerPropertyDefinition> propertyDefinitionList =
          pluginImpl.getIssuerPropertyDefinitionList();

        this.propertyDefinitionMap = new HashMap<>();
        if (propertyDefinitionList != null && !propertyDefinitionList.isEmpty()) {
            for (IssuerPropertyDefinition propDef : propertyDefinitionList) {
                propertyDefinitionMap.put(propDef.getPropertyId(), propDef);
            }
        }

        this.pluginImpl.initialize(
          InitializeInvokeDTO.builder().systemPropertiesMap(new HashMap<>()).build());
    }

    public SendAuthResultDTO sendAuthentication(Long issuerBankId, SendAuthInvokeDTO invokeDTO) {
        Map<IssuerPropertyDefinition, String> issuerRuntimeProp =
          getIssuerRuntimeProperty(issuerBankId);

        SendAuthResultDTO resultDTO = pluginImpl.sendAuthentication(invokeDTO);

        ensureInvokeSuccessOrThrow(resultDTO);

        return resultDTO;
    }

    @Override
    public String getName() {
        return pluginImpl.getPluginName();
    }

    @Override
    public List<IssuerPropertyDefinition> getPropertyDefinitionList() {
        return pluginImpl.getIssuerPropertyDefinitionList();
    }

    @Override
    public PluginType getPluginType() {
        return PluginType.MESSENGER;
    }

    @Override
    public IssuerPropertyDefinition findPropertyDefinitionById(int propertyId) {
        return propertyDefinitionMap.get(propertyId);
    }

    private void ensureInvokeSuccessOrThrow(ResultDTO pluginResultDTO) {
        if (pluginResultDTO.isSuccess()) {
            return;
        }

        log.error("[MessengerPlugin] plugin error", pluginResultDTO.getException());
        throw new OceanException(
          ResultStatus.MESSENGER_PLUGIN_EXCEPTION,
          ResultStatus.MESSENGER_PLUGIN_EXCEPTION.toString());
    }
}
