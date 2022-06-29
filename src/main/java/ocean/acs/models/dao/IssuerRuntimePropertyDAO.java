package ocean.acs.models.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;
import ocean.acs.commons.enumerator.PluginType;
import ocean.acs.models.data_object.entity.IssuerRuntimePropDO;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public interface IssuerRuntimePropertyDAO {

    String getByPropertyId(String issuerId, String propertyId);

    Map<String, String> getPropertyConfigMap(String issuerId, Set<String> propertyIdSet);

    void updatePluginPropertyList(
      Long issuerId, String user, PluginType pluginType, List<IssuerRuntimePropDO> propDOList);

    List<IssuerRuntimePropDO> getPropertyListOfIssuer(Long issuerId, PluginType pluginType);


    List<IssuerRuntimePropDO> findByIssuerIdAndPluginId(Long issuerId, String pluginId);

    void deleteIssuerRuntimePropsByIssuerIdAndPluginId(Long issuerId, String pluginId);

    int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis);

}
