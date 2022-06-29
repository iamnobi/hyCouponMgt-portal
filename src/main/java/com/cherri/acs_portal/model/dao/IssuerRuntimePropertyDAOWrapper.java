package com.cherri.acs_portal.model.dao;

import com.cherri.acs_portal.component.HsmPlugin;
import com.cherri.acs_portal.dto.hsm.DecryptResultDTO;
import com.cherri.acs_portal.dto.hsm.EncryptResultDTO;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ocean.acs.commons.enumerator.PluginType;
import ocean.acs.models.dao.IssuerRuntimePropertyDAO;
import ocean.acs.models.data_object.entity.IssuerRuntimePropDO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class IssuerRuntimePropertyDAOWrapper {

  private IssuerRuntimePropertyDAO issuerRuntimePropertyDAO;
  private HsmPlugin hsmPlugin;

  @Autowired
  public IssuerRuntimePropertyDAOWrapper(
    HsmPlugin hsmPlugin, IssuerRuntimePropertyDAO issuerRuntimePropertyDAO) {
    this.hsmPlugin = hsmPlugin;
    this.issuerRuntimePropertyDAO = issuerRuntimePropertyDAO;
  }

  public String getByPropertyId(String issuerId, String propertyId) {
    return null;
  }

  public Map<String, String> getPropertyConfigMap(String issuerId, Set<String> propertyIdSet) {
    return issuerRuntimePropertyDAO.getPropertyConfigMap(issuerId, propertyIdSet);
  }

  public List<IssuerRuntimePropDO> getPropertyListOfIssuer(Long issuerId, PluginType pluginType) {
    List<IssuerRuntimePropDO> propertyDOList =
      issuerRuntimePropertyDAO.getPropertyListOfIssuer(issuerId, pluginType);

    if (propertyDOList == null || propertyDOList.isEmpty())
      return propertyDOList;

    for (IssuerRuntimePropDO runtimePropDO : propertyDOList) {
      if (!runtimePropDO.isEncrypt())
        continue;

      DecryptResultDTO decryptResultDTO = null;
      try {
        decryptResultDTO = hsmPlugin.decryptWithIssuerBankId(runtimePropDO.getValue(), runtimePropDO.getIssuerId());
        runtimePropDO.setValue(decryptResultDTO.getString());
      } finally {
        if (decryptResultDTO != null) {
          decryptResultDTO.clearPlainText();
        }
      }
    }

    return propertyDOList;
  }

  @Transactional
  public void updatePluginPropertyList(
    Long issuerBankId, String user, PluginType pluginType, List<IssuerRuntimePropDO> propDOList) {
    if (propDOList == null || propDOList.isEmpty())
      return;

    for (IssuerRuntimePropDO propDO : propDOList) {
      if (!propDO.isEncrypt())
        continue;
      if (StringUtils.isEmpty(propDO.getValue()))
        continue;

      EncryptResultDTO encryptResultDTO =
        hsmPlugin.encryptWithIssuerBankId(propDO.getValue(), propDO.getIssuerId());

      propDO.setValue(encryptResultDTO.getBase64());
    }

    issuerRuntimePropertyDAO.updatePluginPropertyList(issuerBankId, user, pluginType, propDOList);
  }
}
