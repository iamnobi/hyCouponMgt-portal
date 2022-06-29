package ocean.acs.models.oracle.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.experimental.SuperBuilder;
import ocean.acs.models.data_object.entity.IssuerHandingFeeDO;
import ocean.acs.models.data_object.portal.IssuerBankRelativeData;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.models.entity.DBKey;
import ocean.acs.models.entity.OperatorInfo;

@DynamicUpdate
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = DBKey.TABLE_ISSUER_HANDING_FEE)
public class IssuerHandingFee extends OperatorInfo implements IssuerBankRelativeData {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "issuer_handing_fee_seq_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name",
                            value = "ISSUER_HANDING_FEE_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "issuer_handing_fee_seq_generator")
    @Column(name = DBKey.COL_ISSUER_HANDING_FEE_ID)
    private Long id;

    @Column(name = DBKey.COL_ISSUER_HANDING_FEE_ISSUER_BANK_ID)
    private Long issuerBankId;

    @Column(name = DBKey.COL_ISSUER_HANDING_FEE_HANDING_FEE_TYPE)
    private Integer handingFeeType;

    @Column(name = DBKey.COL_ISSUER_HANDING_FEE_FEE_PER_CARD)
    private Double feePerCard;

    @Column(name = DBKey.COL_ISSUER_HANDING_FEE_THRESHOLD_FEE)
    private Double thresholdFee;

    @Column(name = DBKey.COL_ISSUER_HANDING_FEE_MINIMUM_FEE)
    private Double minimumFee;

    @Column(name = DBKey.COL_ISSUER_HANDING_FEE_FEE_PER_MONTH)
    private Double feePerMonth;

    @Column(name = DBKey.COL_AUDIT_STATUS)
    private String auditStatus;

    public IssuerHandingFee(Long id, Long issuerBankId, Integer handingFeeType, Double feePerCard,
            Double thresholdFee, Double minimumFee, Double feePerMonth, String auditStatus,
            String creator, Long createMillis, String updater, Long updateMillis,
            Boolean deleteFlag, String deleter, Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.issuerBankId = issuerBankId;
        this.handingFeeType = handingFeeType;
        this.feePerCard = feePerCard;
        this.thresholdFee = thresholdFee;
        this.minimumFee = minimumFee;
        this.feePerMonth = feePerMonth;
        this.auditStatus = auditStatus;
    }

    public static IssuerHandingFee valueOf(IssuerHandingFeeDO d) {
        return new IssuerHandingFee(d.getId(), d.getIssuerBankId(), d.getHandingFeeType(),
                d.getFeePerCard(), d.getThresholdFee(), d.getMinimumFee(), d.getFeePerMonth(),
                d.getAuditStatus(), d.getCreator(), d.getCreateMillis(), d.getUpdater(),
                d.getUpdateMillis(), d.getDeleteFlag(), d.getDeleter(), d.getDeleteMillis());
    }
}
