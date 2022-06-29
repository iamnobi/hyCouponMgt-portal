package ocean.acs.models.oracle.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import ocean.acs.models.oracle.repository.IssuerRuntimePropertyRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import ocean.acs.commons.enumerator.PluginType;
import ocean.acs.models.dao.IssuerRuntimePropertyDAO;
import ocean.acs.models.data_object.entity.IssuerRuntimePropDO;
import ocean.acs.models.oracle.entity.IssuerRuntimeProp;

@Primary
@Repository
@AllArgsConstructor
public class IssuerRuntimePropertyDAOImpl implements IssuerRuntimePropertyDAO {

    private final IssuerRuntimePropertyRepository repo;

    public String getByPropertyId(String issuerId, String propertyId) {
        return String.format("value - %s", propertyId);
    }

    public Map<String, String> getPropertyConfigMap(String issuerId, Set<String> propertyIdSet) {
        Map<String, String> propertyMap = new HashMap<>();

        for (String propertyId : propertyIdSet) {
            propertyMap.put(propertyId, getByPropertyId(issuerId, propertyId));
        }

        return propertyMap;
    }

    @Override
    public List<IssuerRuntimePropDO> getPropertyListOfIssuer(Long issuerId, PluginType pluginType) {
        List<IssuerRuntimePropDO> propertyDOList = new ArrayList<>();
        List<IssuerRuntimeProp> propList =
                repo.findByIssuerIdAndPluginId(issuerId, pluginType.getPluginId());

        if (propList.isEmpty())
            return propertyDOList;

        // Todo: convert IssuerRuntimeProperty propertyCollectionList to PluginIssuerRuntimeResDTO
        // propDOList
        for (IssuerRuntimeProp storedProp : propList) {
            IssuerRuntimePropDO resultDO = IssuerRuntimePropDO.valueOf(storedProp);
            propertyDOList.add(resultDO);
        }

        return propertyDOList;
    }

    @Override
    public List<IssuerRuntimePropDO> findByIssuerIdAndPluginId(Long issuerId, String pluginId) {
        return repo.findByIssuerIdAndPluginId(issuerId, pluginId).stream()
                .map(IssuerRuntimePropDO::valueOf).collect(Collectors.toList());
    }

    @Override
    public void deleteIssuerRuntimePropsByIssuerIdAndPluginId(Long issuerId, String pluginId) {
        repo.deleteIssuerRuntimePropsByIssuerIdAndPluginId(issuerId, pluginId);
    }

    @Transactional
    @Override
    public void updatePluginPropertyList(Long issuerId, String user, PluginType pluginType,
            List<IssuerRuntimePropDO> propDOList) {
        if (propDOList == null || propDOList.isEmpty())
            return;

        List<IssuerRuntimeProp> updateProptList = new ArrayList<>();
        // Todo: modified creator

        for (IssuerRuntimePropDO srcPropDO : propDOList) {
            IssuerRuntimeProp savedContainerProp = IssuerRuntimeProp.valueOf(srcPropDO);
            savedContainerProp.setCreator(user);
            savedContainerProp.setCreateMillis(System.currentTimeMillis());
            savedContainerProp.setUpdater(user);
            savedContainerProp.setUpdateMillis(System.currentTimeMillis());
            updateProptList.add(savedContainerProp);
        }
        repo.deleteIssuerRuntimePropsByIssuerIdAndPluginId(issuerId, pluginType.getPluginId());
        repo.saveAll(updateProptList);
    }

    @Override
    public int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis) {
        return repo.deleteByIssuerBankId(issuerBankId, deleter, deleteMillis);
    }
}
