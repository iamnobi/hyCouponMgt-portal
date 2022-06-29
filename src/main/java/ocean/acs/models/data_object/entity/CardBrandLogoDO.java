package ocean.acs.models.data_object.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class CardBrandLogoDO extends OperatorInfoDO {

    private Long id;
    private int threeDSVersion;
    private String cardBrand;
    private String imageName;
    private byte[] imageContent;
    private Integer imageSize;
    private String auditStatus;

    public CardBrandLogoDO(
            Long id,
            int threeDSVersion,
            String cardBrand,
            String imageName,
            byte[] imageContent,
            Integer imageSize,
            String auditStatus,
            String creator,
            Long createMillis,
            String updater,
            Long updateMillis,
            Boolean deleteFlag,
            String deleter,
            Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.threeDSVersion = threeDSVersion;
        this.cardBrand = cardBrand;
        this.imageName = imageName;
        this.imageContent = imageContent;
        this.imageSize = imageSize;
        this.auditStatus = auditStatus;
    }

    public static CardBrandLogoDO valueOf(ocean.acs.models.oracle.entity.CardBrandLogo e) {
        return new CardBrandLogoDO(
                e.getId(),
                e.getThreeDSVersion(),
                e.getCardBrand(),
                e.getImageName(),
                e.getImageContent(),
                e.getImageSize(),
                e.getAuditStatus(),
                e.getCreator(),
                e.getCreateMillis(),
                e.getUpdater(),
                e.getUpdateMillis(),
                e.getDeleteFlag(),
                e.getDeleter(),
                e.getDeleteMillis());
    }
    
    public static CardBrandLogoDO valueOf(ocean.acs.models.sql_server.entity.CardBrandLogo e) {
        return new CardBrandLogoDO(
                e.getId(),
                e.getThreeDSVersion(),
                e.getCardBrand(),
                e.getImageName(),
                e.getImageContent(),
                e.getImageSize(),
                e.getAuditStatus(),
                e.getCreator(),
                e.getCreateMillis(),
                e.getUpdater(),
                e.getUpdateMillis(),
                e.getDeleteFlag(),
                e.getDeleter(),
                e.getDeleteMillis());
    }
}
