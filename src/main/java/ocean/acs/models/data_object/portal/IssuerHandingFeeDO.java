package ocean.acs.models.data_object.portal;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ocean.acs.models.data_object.entity.OperatorInfoDO;
import ocean.acs.models.oracle.entity.IssuerHandingFee;

@Getter
@Setter
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

  public static IssuerHandingFeeDO valueOf(IssuerHandingFee e) {
    return IssuerHandingFeeDO.builder()
      .id(e.getId())
      .issuerBankId(e.getIssuerBankId())
      .handingFeeType(e.getHandingFeeType())
      .feePerCard(e.getFeePerCard())
      .thresholdFee(e.getThresholdFee())
      .auditStatus(e.getAuditStatus())

      .createMillis(e.getCreateMillis())
      .creator(e.getCreator())
      .updateMillis(e.getUpdateMillis())
      .updater(e.getUpdater())
      .deleteMillis(e.getDeleteMillis())
      .deleter(e.getDeleter())
      .deleteFlag(e.getDeleteFlag())
      .build();
  }
}
