package ocean.acs.models.data_object.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssuerRuntimePropDO {

    private Long issuerId;
    private String pluginId;
    private int propertyId;
    private boolean isEncrypt;
    private String name;
    private String value;

    public static IssuerRuntimePropDO valueOf(ocean.acs.models.oracle.entity.IssuerRuntimeProp e) {
        IssuerRuntimePropDO issuerRuntimeAttributeDO = new IssuerRuntimePropDO();
        issuerRuntimeAttributeDO.setIssuerId(e.getIssuerId());
        issuerRuntimeAttributeDO.setPluginId(e.getPluginId());
        issuerRuntimeAttributeDO.setPropertyId(e.getPropertyId());
        issuerRuntimeAttributeDO.setName(e.getName());
        issuerRuntimeAttributeDO.setValue(e.getValue());
        issuerRuntimeAttributeDO.setEncrypt(e.getValueEncrypted());
        return issuerRuntimeAttributeDO;
    }
    
    public static IssuerRuntimePropDO valueOf(ocean.acs.models.sql_server.entity.IssuerRuntimeProp e) {
        IssuerRuntimePropDO issuerRuntimeAttributeDO = new IssuerRuntimePropDO();
        issuerRuntimeAttributeDO.setIssuerId(e.getIssuerId());
        issuerRuntimeAttributeDO.setPluginId(e.getPluginId());
        issuerRuntimeAttributeDO.setPropertyId(e.getPropertyId());
        issuerRuntimeAttributeDO.setName(e.getName());
        issuerRuntimeAttributeDO.setValue(e.getValue());
        issuerRuntimeAttributeDO.setEncrypt(e.getValueEncrypted());
        return issuerRuntimeAttributeDO;
    }
}
