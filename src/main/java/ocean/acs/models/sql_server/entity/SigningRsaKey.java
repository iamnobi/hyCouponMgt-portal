package ocean.acs.models.sql_server.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.DynamicUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.models.data_object.entity.SigningRsaKeyDO;
import ocean.acs.models.entity.DBKey;

@DynamicUpdate
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = DBKey.TABLE_SIGNING_RSA_KEY)
public class SigningRsaKey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBKey.COL_SIGNING_RSA_KEY_ID)
    private Long id;

    @NotNull
    @Column(name = DBKey.COL_SIGNING_RSA_KEY_KEY_ID)
    private String keyId;

    @NotNull
    @Column(name = DBKey.COL_SIGNING_RSA_KEY_MODULUS_HEX)
    private String modulusHex;

    @NotNull
    @Column(name = DBKey.COL_SIGNING_RSA_KEY_PUBLIC_EXPONENT_HEX)
    private String publicExponentHex;

    @NotNull
    @Column(name = DBKey.COL_SIGNING_RSA_KEY_CREATE_MILLIS)
    private Long createMillis;

    @NotNull
    @Column(name = DBKey.COL_SIGNING_RSA_KEY_UPDATE_MILLIS)
    private Long updateMillis;

    public static SigningRsaKey valueOf(SigningRsaKeyDO d) {
        return new SigningRsaKey(d.getId(), d.getKeyId(),d.getModulusHex(),
                d.getPublicExponentHex(), d.getCreateMillis(), d.getUpdateMillis());

    }

}
