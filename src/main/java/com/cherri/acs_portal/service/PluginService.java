package com.cherri.acs_portal.service;

import com.cherri.acs_kernel.plugin.dto.IssuerPropertyDefinition;
import com.cherri.acs_portal.component.AuthenticationPlugin;
import com.cherri.acs_portal.component.CardholderPlugin;
import com.cherri.acs_portal.component.HsmPlugin;
import com.cherri.acs_portal.component.MessengerPlugin;
import com.cherri.acs_portal.component.OtpPlugin;
import com.cherri.acs_portal.controller.request.PluginRuntimePropertyCollectionDTO;
import com.cherri.acs_portal.controller.request.PluginRuntimePropertyDTO;
import com.cherri.acs_portal.controller.response.PluginIssuerRuntimeResDTO;
import com.cherri.acs_portal.dto.plugin.IssuerRuntimeProptDTO;
import com.cherri.acs_portal.model.dao.IssuerRuntimePropertyDAOWrapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.PluginType;
import ocean.acs.models.data_object.entity.IssuerRuntimePropDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@NoArgsConstructor
public class PluginService {

    @Getter
    private HsmPlugin hsmPlugin;
    @Getter
    private CardholderPlugin cardholderPlugin;
    @Getter
    private MessengerPlugin messengerPlugin;
    @Getter
    private OtpPlugin otpPlugin;
    @Getter
    private AuthenticationPlugin authenticationPlugin;

    private Map<String, String> systemProperty = new HashMap<>();

    protected Map<PluginType, List<IssuerPropertyDefinition>> pluginPropertyDefinitionMap;
    protected Map<PluginType, String> pluginNameMap;

    private IssuerRuntimePropertyDAOWrapper runtimePropertyDAOWrapper;

    @Autowired
    public PluginService(
      IssuerRuntimePropertyDAOWrapper runtimePropertyDAOWrapper,
      HsmPlugin hsmPlugin,
      CardholderPlugin cardholderPlugin,
      MessengerPlugin messengerPlugin,
      OtpPlugin otpPlugin,
      AuthenticationPlugin authenticationPlugin) {
        this.runtimePropertyDAOWrapper = runtimePropertyDAOWrapper;
        this.hsmPlugin = hsmPlugin;
        this.cardholderPlugin = cardholderPlugin;
        this.messengerPlugin = messengerPlugin;
        this.otpPlugin = otpPlugin;
        this.authenticationPlugin = authenticationPlugin;

        pluginPropertyDefinitionMap = new HashMap<>();
        pluginNameMap = new HashMap<>();

        pluginPropertyDefinitionMap.put(PluginType.HSM, hsmPlugin.getPropertyDefinitionList());
        pluginNameMap.put(PluginType.HSM, hsmPlugin.getName());

        pluginPropertyDefinitionMap.put(
          PluginType.CARDHOLDER, cardholderPlugin.getPropertyDefinitionList());
        pluginNameMap.put(PluginType.CARDHOLDER, cardholderPlugin.getName());

        pluginPropertyDefinitionMap.put(
          PluginType.MESSENGER, messengerPlugin.getPropertyDefinitionList());
        pluginNameMap.put(PluginType.MESSENGER, messengerPlugin.getName());

        pluginPropertyDefinitionMap.put(PluginType.OTP, otpPlugin.getPropertyDefinitionList());
        pluginNameMap.put(PluginType.OTP, otpPlugin.getName());

        pluginPropertyDefinitionMap.put(
          PluginType.AUTHENTICATION, authenticationPlugin.getPropertyDefinitionList());
        pluginNameMap.put(PluginType.AUTHENTICATION, authenticationPlugin.getName());
    }

    public List<IssuerPropertyDefinition> getPropertyDefinitionByPluginType(PluginType pluginType) {
        return pluginPropertyDefinitionMap.get(pluginType);
    }

    public PluginIssuerRuntimeResDTO getPluginRuntimeIssuerProperty(
      Long issuerBankId, PluginType pluginType) {

        List<IssuerRuntimePropDO> propertyValueDOList =
          runtimePropertyDAOWrapper.getPropertyListOfIssuer(issuerBankId, pluginType);

        List<IssuerPropertyDefinition> propertyDefinitionList =
          pluginPropertyDefinitionMap.get(pluginType);

        List<IssuerRuntimeProptDTO> resultProptDTOList = new ArrayList<>();
        PluginIssuerRuntimeResDTO runtimeProperty =
          new PluginIssuerRuntimeResDTO(issuerBankId, pluginType, pluginNameMap.get(pluginType));
        runtimeProperty.setPropertyList(resultProptDTOList);

        // If there is no extra definition in needed, then just return.
        if (propertyDefinitionList == null || propertyDefinitionList.isEmpty())
            return runtimeProperty;

        for (IssuerPropertyDefinition propertyDefinition : propertyDefinitionList) {
            IssuerRuntimeProptDTO resultPropDTO =
              new IssuerRuntimeProptDTO(issuerBankId, pluginType.getPluginId(), propertyDefinition);
            resultProptDTOList.add(resultPropDTO);

            for (IssuerRuntimePropDO propertyValueDO : propertyValueDOList) {
                // just in case if someone modify the condition in previous stage
                if (!resultPropDTO.getIssuerId().equals(propertyValueDO.getIssuerId())
                  || !resultPropDTO.getPluginId().equals(propertyValueDO.getPluginId())
                  || !(resultPropDTO.getPropertyId() == propertyValueDO.getPropertyId()))
                    continue;

                resultPropDTO.setValue(propertyValueDO.getValue());
                break;
            }
        }

        return runtimeProperty;
    }

    public List<PluginIssuerRuntimeResDTO> getAllPluginRuntimeIssuerProperty(Long issuerBankId) {
        List<PluginIssuerRuntimeResDTO> resultList = new ArrayList<>();

        for (PluginType pluginType : PluginType.values()) {
            if (pluginType == PluginType.UNKNOWN)
                continue;

            PluginIssuerRuntimeResDTO pluginIssuerProperty =
              getPluginRuntimeIssuerProperty(issuerBankId, pluginType);
            resultList.add(pluginIssuerProperty);
        }

        return resultList;
    }

    public void updatePluginRuntimeIssuerProperty(
      Long issuerBankId,
      String user,
      String pluginId,
      List<PluginRuntimePropertyDTO> propertyList) {
        PluginType pluginType = PluginType.getByCode(pluginId);

        if (pluginType == PluginType.UNKNOWN) {
            // Todo: format argument error log
            log.error(
              "Illegal argument error. pluginId shall be in enum range but get: "
                + pluginId
                + ", in issuerBankId: "
                + issuerBankId);
            return;
        }

        updatePluginRuntimeIssuerProperty(issuerBankId, user, pluginType, propertyList);
    }

    @Transactional
    public void updatePluginRuntimeIssuerProperty(
      Long issuerBankId,
      String user,
      PluginType pluginType,
      List<PluginRuntimePropertyDTO> propertyList) {
        if (propertyList == null || propertyList.size() <= 0)
            return;

        List<IssuerRuntimePropDO> propertyDOList = new ArrayList<>();
        for (PluginRuntimePropertyDTO propertyDTO : propertyList) {
            IssuerRuntimePropDO propDO = new IssuerRuntimePropDO();
            propDO.setIssuerId(issuerBankId);
            propDO.setPluginId(pluginType.getPluginId());
            propDO.setPropertyId(propertyDTO.getPropertyId());
            propDO.setName(propertyDTO.getName());
            propDO.setValue(propertyDTO.getValue());

            if (getPropertyValueEncryptDefinition(pluginType, propertyDTO.getPropertyId())) {
                propDO.setEncrypt(true);
            } else {
                propDO.setEncrypt(false);
            }

            propertyDOList.add(propDO);
        }

        runtimePropertyDAOWrapper.updatePluginPropertyList(
          issuerBankId, user, pluginType, propertyDOList);
    }

    @Transactional
    public void updateAllPluginRuntimeIssuerProperty(
      Long issuerBankId,
      String user,
      List<PluginRuntimePropertyCollectionDTO> pluginPropertyCollectionDTOList) {
        if (pluginPropertyCollectionDTOList == null || pluginPropertyCollectionDTOList.isEmpty())
            return;

        for (PluginRuntimePropertyCollectionDTO pluginIssuerRuntimeCollectionDTO :
          pluginPropertyCollectionDTOList) {
            updatePluginRuntimeIssuerProperty(
              issuerBankId,
              user,
              pluginIssuerRuntimeCollectionDTO.getPluginId() + "",
              pluginIssuerRuntimeCollectionDTO.getPropertyList());
        }
    }

    private boolean getPropertyValueEncryptDefinition(PluginType pluginType, Integer propertyId) {
        List<IssuerPropertyDefinition> propertyDefinitionList =
          pluginPropertyDefinitionMap.get(pluginType);
        for (IssuerPropertyDefinition propertyDefinition : propertyDefinitionList) {
            if (propertyDefinition.getPropertyId() != propertyId)
                continue;

            return propertyDefinition.isEncrypt();
        }

        // default: no encrypt
        return false;
    }
}
