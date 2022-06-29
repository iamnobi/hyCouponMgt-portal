package ocean.acs.models.sql_server.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.DynamicUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.models.data_object.entity.RootCaCertificateDO;
import ocean.acs.models.entity.DBKey;

@DynamicUpdate
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = DBKey.TABLE_ROOT_CA_CERTIFICATE)
public class RootCaCertificate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBKey.COL_ROOT_CA_CERTIFICATE_ID)
    private Long id;

    @Lob
    @NotNull
    @Column(name = DBKey.COL_ROOT_CA_CERTIFICATE_CERTIFICATE)
    private byte[] certificate;

    @NotNull
    @Column(name = DBKey.COL_ROOT_CA_CERTIFICATE_INFORMATION)
    private String information;

    @NotNull
    @Column(name = DBKey.COL_ROOT_CA_CERTIFICATE_ISSUER)
    private String issuer;

    @NotNull
    @Column(name = DBKey.COL_ROOT_CA_CERTIFICATE_APPLY_DATE)
    private Long applyDate;

    @NotNull
    @Column(name = DBKey.COL_ROOT_CA_CERTIFICATE_EXPIRY_DATE)
    private Long expiryDate;

    @NotNull
    @Column(name = DBKey.COL_ROOT_CA_CERTIFICATE_CREATE_MILLIS)
    private Long createMillis;

    public static RootCaCertificate valueOf(RootCaCertificateDO d) {
        return new RootCaCertificate(d.getId(), d.getCertificate(), d.getInformation(),
                d.getIssuer(), d.getApplyDate(), d.getExpiryDate(), d.getCreateMillis());
    }

}
