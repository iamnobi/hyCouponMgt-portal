package ocean.acs.models.data_object.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class IssuerBrandLogoDO extends OperatorInfoDO {

    private Long id;
    private Long issuerBankId;
    private String imageName;
    private byte[] imageContent;
    private Integer imageSize;
    private String auditStatus;

    public IssuerBrandLogoDO(Long id, Long issuerBankId, String imageName, byte[] imageContent,
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

    public static IssuerBrandLogoDO valueOf(ocean.acs.models.oracle.entity.IssuerBrandLogo e) {
        return new IssuerBrandLogoDO(e.getId(), e.getIssuerBankId(), e.getImageName(),
                e.getImageContent(), e.getImageSize(), e.getAuditStatus(), e.getCreator(),
                e.getCreateMillis(), e.getUpdater(), e.getUpdateMillis(), e.getDeleteFlag(),
                e.getDeleter(), e.getDeleteMillis());
    }
    
    public static IssuerBrandLogoDO valueOf(ocean.acs.models.sql_server.entity.IssuerBrandLogo e) {
        return new IssuerBrandLogoDO(e.getId(), e.getIssuerBankId(), e.getImageName(),
                e.getImageContent(), e.getImageSize(), e.getAuditStatus(), e.getCreator(),
                e.getCreateMillis(), e.getUpdater(), e.getUpdateMillis(), e.getDeleteFlag(),
                e.getDeleter(), e.getDeleteMillis());
    }

}
