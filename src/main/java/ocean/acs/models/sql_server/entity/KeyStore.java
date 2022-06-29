package ocean.acs.models.sql_server.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ocean.acs.models.data_object.entity.KeyStoreDO;
import ocean.acs.models.entity.DBKey;

@DynamicUpdate
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = DBKey.TABLE_KEY_STORE)
public class KeyStore implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = DBKey.COL_KEY_STORE_ID)
    private String id;

    @NotBlank(message = "{column.notempty}")
    @Column(name = DBKey.COL_KEY_STORE_KEY_NAME)
    private String keyName;

    @NotBlank(message = "{column.notempty}")
    @Column(name = DBKey.COL_KEY_STORE_KEY_BODY)
    private String keyBody;

    @NonNull
    @Column(name = DBKey.COL_KEY_STORE_SYS_CREATOR)
    private String sysCreator;

    @NonNull
    @Column(name = DBKey.COL_KEY_STORE_CREATE_MILLIS)
    private Long createMillis;

    public static KeyStore valueOf(KeyStoreDO d) {
        return new KeyStore(d.getId(), d.getKeyName(), d.getKeyBody(), d.getSysCreator(),
                d.getCreateMillis());
    }

}
