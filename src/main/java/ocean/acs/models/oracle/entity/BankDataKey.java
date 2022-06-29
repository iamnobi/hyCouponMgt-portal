package ocean.acs.models.oracle.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ocean.acs.models.data_object.entity.BankDataKeyDO;
import ocean.acs.models.entity.DBKey;

/**
 * BankDataKey
 *
 * @author ?
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = DBKey.TABLE_BANK_DATA_KEY)
public class BankDataKey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "BANK_DATA_KEY_ID_GENERATOR",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {@Parameter(name = "sequence_name", value = "BANK_DATA_KEY_ID_SEQ")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BANK_DATA_KEY_ID_GENERATOR")
    @Column(name = DBKey.COL_BANK_DATA_KEY_ID)
    private Long id;

    @NonNull
    @Column(name = DBKey.COL_BANK_DATA_KEY_ISSUER_BANK_ID)
    private Long issuerBankId;

    @NonNull
    @Column(name = DBKey.COL_BANK_DATA_KEY_RSA_PRIVATE_KEY_ID)
    private String rsaPrivateKyId;

    // base64 string
    @NonNull
    @Column(name = DBKey.COL_BANK_DATA_KEY_RSA_PUBLIC_KEY)
    private String rsaPublicKy;

    @Column(name = DBKey.COL_BANK_DATA_KEY_CREATOR)
    private String creator;

    @Column(name = DBKey.COL_BANK_DATA_KEY_CREATE_MILLIS)
    private Long createMillis;

    @Column(name = DBKey.COL_BANK_DATA_KEY_UPDATER)
    private String updater;

    @Column(name = DBKey.COL_BANK_DATA_KEY_UPDATE_MILLIS)
    private Long updateMillis;

    public static BankDataKey valueOf(BankDataKeyDO d) {
        return BankDataKey.builder().id(d.getId()).issuerBankId(d.getIssuerBankId())
                .rsaPrivateKyId(d.getRsaPrivateKyId()).rsaPublicKy(d.getRsaPublicKy())
                .creator(d.getCreator()).createMillis(d.getCreateMillis()).updater(d.getUpdater())
                .updateMillis(d.getUpdateMillis()).build();
    }
}
