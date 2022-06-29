package ocean.acs.models.data_object.portal;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.models.oracle.entity.IssuerBank;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
public class PortalIssuerBankDO extends AuditableDO {

  /** 銀行名稱 */
  @NotNull(message = "{column.notempty}")
  private String name;

  /** 銀行代碼 */
  private String code;

  // for AuditableDTO
  private Long id;

  @NotNull(message = "{column.notempty}")
  private Long issuerBankId;

  private String symmetricKeyId;

  private String sensitiveDataKeyId;

  private String threeDSMethodUrl;
  private String acsUrl;
  private String acsRefNumber;
  private String acsOperatorId;

  private AuditStatus auditStatus;

  private String user;

  public static PortalIssuerBankDO valueOf(IssuerBank entity) {
    return PortalIssuerBankDO.builder()
      .id(entity.getId())
      .name(entity.getName())
      .code(entity.getCode())
      .symmetricKeyId(entity.getSymmetricKeyId())
      .sensitiveDataKeyId(entity.getSensitiveDataKeyId())
        .threeDSMethodUrl(entity.getThreeDSMethodUrl())
        .acsUrl(entity.getAcsUrl())
        .acsRefNumber(entity.getAcsRefNumber())
        .acsOperatorId(entity.getAcsOperatorId())
      .auditStatus(AuditStatus.getStatusBySymbol(entity.getAuditStatus()))
      .build();
  }

  @JsonIgnore
  @Override
  public AuditFunctionType getFunctionType() {
    return AuditFunctionType.BANK_MANAGE;
  }
}
