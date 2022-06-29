package ocean.acs.models.sql_server.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.models.data_object.entity.IssuerLogoDO;
import ocean.acs.models.data_object.portal.IssuerLogoUpdateDO;
import ocean.acs.models.entity.DBKey;
import ocean.acs.models.entity.OperatorInfo;

@DynamicUpdate
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = DBKey.TABLE_ISSUER_LOGO)
public class IssuerLogo extends OperatorInfo {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBKey.COL_ISSUER_LOGO_ID)
    private Long id;

    @Column(name = DBKey.COL_ISSUER_LOGO_ISSUER_BANK_ID)
    private Long issuerBankId;

    @Column(name = DBKey.COL_ISSUER_LOGO_IMAGE_NAME)
    private String imageName;

    @Column(name = DBKey.COL_ISSUER_LOGO_IMAGE_CONTENT)
    private byte[] imageContent;

    @Column(name = DBKey.COL_ISSUER_LOGO_IMAGE_SIZE)
    private Integer imageSize;

    @Column(name = DBKey.COL_AUDIT_STATUS)
    private String auditStatus;

    public IssuerLogo(Long id, Long issuerBankId, String imageName, byte[] imageContent,
            Integer imageSize, String auditStatus, String creator, Long createMillis,
            String updater, Long updateMillis, Boolean deleteFlag, String deleter,
            Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.issuerBankId = issuerBankId;
        this.imageName = imageName;
        this.imageContent = imageContent;
        this.imageSize = imageSize;
        this.auditStatus = auditStatus;
    }

    public static IssuerLogo valueOf(IssuerLogoDO d) {
        return new IssuerLogo(d.getId(), d.getIssuerBankId(), d.getImageName(), d.getImageContent(),
                d.getImageSize(), d.getAuditStatus(), d.getCreator(), d.getCreateMillis(),
                d.getUpdater(), d.getUpdateMillis(), d.getDeleteFlag(), d.getDeleter(),
                d.getDeleteMillis());
    }

    public static IssuerLogo valueOf(IssuerLogoUpdateDO issuerLogoUpdateDO) {
        IssuerLogo entity = new IssuerLogo();
        entity.setIssuerBankId(issuerLogoUpdateDO.getIssuerBankId());
        entity.setImageName(issuerLogoUpdateDO.getFileName());
        entity.setImageContent(issuerLogoUpdateDO.getFileContent());
        entity.setImageSize(issuerLogoUpdateDO.getImageSize());
        entity.setAuditStatus(AuditStatus.AUDITING.getSymbol());
        entity.setCreator(issuerLogoUpdateDO.getUser());
        entity.setCreateMillis(System.currentTimeMillis());
        return entity;
    }

}
