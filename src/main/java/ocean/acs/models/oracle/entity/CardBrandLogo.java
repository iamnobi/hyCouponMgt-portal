package ocean.acs.models.oracle.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.models.data_object.entity.CardBrandLogoDO;
import ocean.acs.models.entity.DBKey;
import ocean.acs.models.entity.OperatorInfo;

@DynamicUpdate
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = DBKey.TABLE_CARD_BRAND_LOGO)
public class CardBrandLogo extends OperatorInfo {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "card_brand_logo_seq_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name",
                            value = "CARD_BRAND_LOGO_ID_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_brand_logo_seq_generator")
    @Column(name = DBKey.COL_CARD_BRAND_LOGO_ID)
    private Long id;

    @Column(name = DBKey.COL_CARD_BRAND_LOGO_THREE_D_S_VERSION)
    private int threeDSVersion;

    @NotNull
    @Column(name = DBKey.COL_CARD_BRAND_LOGO_CARD_BRAND)
    private String cardBrand;

    @NotNull
    @Column(name = DBKey.COL_CARD_BRAND_LOGO_IMAGE_NAME)
    private String imageName;

    @Lob
    @NotNull
    @Column(name = DBKey.COL_CARD_BRAND_LOGO_IMAGE_CONTENT, columnDefinition = "BLOB")
    private byte[] imageContent;

    @NotNull
    @Column(name = DBKey.COL_CARD_BRAND_LOGO_IMAGE_SIZE)
    private Integer imageSize;

    @Column(name = DBKey.COL_AUDIT_STATUS)
    private String auditStatus;

    public CardBrandLogo(Long id, int threeDSVersion, String cardBrand, String imageName, byte[] imageContent,
            Integer imageSize, String auditStatus, String creator, Long createMillis,
            String updater, Long updateMillis, Boolean deleteFlag, String deleter,
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

    public static CardBrandLogo valueOf(CardBrandLogoDO d) {
        return new CardBrandLogo(d.getId(), d.getThreeDSVersion(), d.getCardBrand(), d.getImageName(), d.getImageContent(),
                d.getImageSize(), d.getAuditStatus(), d.getCreator(), d.getCreateMillis(),
                d.getUpdater(), d.getUpdateMillis(), d.getDeleteFlag(), d.getDeleter(),
                d.getDeleteMillis());
    }

}
