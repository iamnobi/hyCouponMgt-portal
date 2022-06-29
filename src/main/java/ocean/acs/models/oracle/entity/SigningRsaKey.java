package ocean.acs.models.oracle.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
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
    @GenericGenerator(name = "signing_rsa_key_seq_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name",
                            value = "SEQ_SIGNING_RSA_KEY"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "signing_rsa_key_seq_generator")
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
        return new SigningRsaKey(d.getId(), d.getKeyId(), d.getModulusHex(),
                d.getPublicExponentHex(), d.getCreateMillis(), d.getUpdateMillis());

    }
}
