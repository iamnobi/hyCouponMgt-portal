package com.cherri.acs_portal.component;

import com.cherri.acs_kernel.plugin.AuthenticationInterface;
import com.cherri.acs_kernel.plugin.dto.IssuerPropertyDefinition;
import com.cherri.acs_kernel.plugin.dto.ResultDTO;
import com.cherri.acs_kernel.plugin.dto.authentication.invoke.AuthenticateInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.authentication.invoke.InitializeInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.authentication.result.AuthenticateResultDTO;
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

@Component("PortalAuthenticationPlugin")
@Log4j2
public class AuthenticationPlugin extends PluginBase {

  @Getter
  private AuthenticationInterface pluginImpl;
  private final Map<Integer, IssuerPropertyDefinition> propertyDefinitionMap;
  private final IssuerRuntimePropertyDAOWrapper runtimePropertyDAO;

  @Autowired
  public AuthenticationPlugin(
    AuthenticationInterface pluginImpl, IssuerRuntimePropertyDAOWrapper runtimePropertyDAO) {
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

  public AuthenticateResultDTO authenticate(Long issuerBankId, String account, String password) {
    Map<IssuerPropertyDefinition, String> issuerRuntimeProp =
      getIssuerRuntimeProperty(issuerBankId);

    AuthenticateInvokeDTO invokeDTO =
      AuthenticateInvokeDTO.builder()
        .account(account)
        .password(password)
        .issuerPropertiesMap(issuerRuntimeProp)
        .systemPropertiesMap(new HashMap<>())
        .build();
    AuthenticateResultDTO resultDTO = pluginImpl.authenticate(invokeDTO);

    ensureInvokeSuccessOrThrow(resultDTO);

    return resultDTO;
  }

  @Override
  public String getName() {
    return pluginImpl.getPluginName();
  }

  @Override
  public PluginType getPluginType() {
    return PluginType.AUTHENTICATION;
  }

  @Override
  public List<IssuerPropertyDefinition> getPropertyDefinitionList() {
    return pluginImpl.getIssuerPropertyDefinitionList();
  }

  @Override
  public IssuerPropertyDefinition findPropertyDefinitionById(int propertyId) {
    return propertyDefinitionMap.get(propertyId);
  }

  private void ensureInvokeSuccessOrThrow(ResultDTO pluginResultDTO) throws OceanException {
    if (pluginResultDTO.isSuccess()) {
      return;
    }

    log.error("[AuthenticationPluginHelper] plugin error", pluginResultDTO.getException());
    throw new OceanException(
      ResultStatus.AUTHENTICATION_PLUGIN_EXCEPTION,
      ResultStatus.AUTHENTICATION_PLUGIN_EXCEPTION.toString());
  }
}
