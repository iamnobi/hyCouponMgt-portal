package ocean.acs.models.oracle.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import ocean.acs.models.data_object.entity.IssuerRuntimePropDO;
import ocean.acs.models.entity.DBKey;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
//@RequiredArgsConstructor
@Table(name = DBKey.TABLE_PLUGIN_ISSUER_RUNTIME_PROPERTY)
public class IssuerRuntimeProp implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(
            name = "ISSUER_RUNTIME_PROP_ID_GENERATOR",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {@Parameter(name = "sequence_name", value = "PLUGIN_IRP_ID_SEQ")})
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ISSUER_RUNTIME_PROP_ID_GENERATOR")
    @Column(name = DBKey.COL_PLUGIN_ISSUER_RUNTIME_PROPERTY_ID)
    private Long id;

    @NonNull
    @Column(name = DBKey.COL_PLUGIN_ISSUER_RUNTIME_PROPERTY_ISSUER_BANK_ID)
    private Long issuerId;

    @Column(name = DBKey.COL_PLUGIN_ISSUER_RUNTIME_PROPERTY_PLUGIN_ID)
    private String pluginId;

    @Column(name = DBKey.COL_PLUGIN_ISSUER_RUNTIME_PROPERTY_PROPERTY_ID)
    private Integer propertyId;

    @Column(name = DBKey.COL_PLUGIN_ISSUER_RUNTIME_PROPERTY_NAME)
    private String name;

    @Column(name = DBKey.COL_PLUGIN_ISSUER_RUNTIME_PROPERTY_VALUE)
    private String value;

    @Column(name= DBKey.COL_PLUGIN_ISSUER_RUNTIME_PROPERTY_VALUE_ENCRYPTED)
    private Boolean valueEncrypted;

    @NonNull
    @Column(name = DBKey.COL_PLUGIN_ISSUER_RUNTIME_PROPERTY_CREATOR)
    private String creator;

    @NonNull
    @Column(name = DBKey.COL_PLUGIN_ISSUER_RUNTIME_PROPERTY_CREATE_MILLIS)
    private Long createMillis;

    @Column(name = DBKey.COL_PLUGIN_ISSUER_RUNTIME_PROPERTY_UPDATER)
    private String updater;

    @Column(name = DBKey.COL_PLUGIN_ISSUER_RUNTIME_PROPERTY_UPDATE_MILLIS)
    private Long updateMillis;

    @Column(name = DBKey.COL_PLUGIN_ISSUER_RUNTIME_PROPERTY_DELETE_FLAG)
    private Boolean deleteFlag = false;

    @Column(name = DBKey.COL_PLUGIN_ISSUER_RUNTIME_PROPERTY_DELETER)
    private String deleter;

    @Column(name = DBKey.COL_PLUGIN_ISSUER_RUNTIME_PROPERTY_DELETE_MILLIS)
    private Long deleteMillis;

    public static IssuerRuntimeProp valueOf(IssuerRuntimePropDO srcAttrDO) {
        IssuerRuntimeProp resultAttr = new IssuerRuntimeProp();
        resultAttr.setIssuerId(srcAttrDO.getIssuerId());
        resultAttr.setPluginId(srcAttrDO.getPluginId());
        resultAttr.setPropertyId(srcAttrDO.getPropertyId());
        resultAttr.setName(srcAttrDO.getName());
        resultAttr.setValue(srcAttrDO.getValue());
        resultAttr.setValueEncrypted(srcAttrDO.isEncrypt());

        return resultAttr;
    }
}
