package ocean.acs.models.data_object.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class IssuerHandingFeeDO extends OperatorInfoDO {

    private Long id;
    private Long issuerBankId;
    private Integer handingFeeType;
    private Double feePerCard;
    private Double thresholdFee;
    private Double minimumFee;
    private Double feePerMonth;
    private String auditStatus;

    public IssuerHandingFeeDO(Long id, Long issuerBankId, Integer handingFeeType, Double feePerCard,
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

    public static IssuerHandingFeeDO valueOf(ocean.acs.models.oracle.entity.IssuerHandingFee e) {
        return new IssuerHandingFeeDO(e.getId(), e.getIssuerBankId(), e.getHandingFeeType(),
                e.getFeePerCard(), e.getThresholdFee(), e.getMinimumFee(), e.getFeePerMonth(),
                e.getAuditStatus(), e.getCreator(), e.getCreateMillis(), e.getUpdater(),
                e.getUpdateMillis(), e.getDeleteFlag(), e.getDeleter(), e.getDeleteMillis());
    }
    
    public static IssuerHandingFeeDO valueOf(ocean.acs.models.sql_server.entity.IssuerHandingFee e) {
        return new IssuerHandingFeeDO(e.getId(), e.getIssuerBankId(), e.getHandingFeeType(),
                e.getFeePerCard(), e.getThresholdFee(), e.getMinimumFee(), e.getFeePerMonth(),
                e.getAuditStatus(), e.getCreator(), e.getCreateMillis(), e.getUpdater(),
                e.getUpdateMillis(), e.getDeleteFlag(), e.getDeleter(), e.getDeleteMillis());
    }

}
