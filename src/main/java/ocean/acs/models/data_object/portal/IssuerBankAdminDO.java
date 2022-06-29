package ocean.acs.models.data_object.portal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.models.oracle.entity.UserAccount;

@Getter
@Setter
@SuperBuilder
public class IssuerBankAdminDO extends AuditableDO {

  /** 銀行id */
  @NotNull(message = "{column.notempty}")
  private Long bankId;

  /** 使用者帳號 */
  private String account;

  /** 加密後密碼 */
  private String encryptedPassword;

  /** 使用者姓名 */
  @NotBlank(message = "{column.notempty}")
  private String userName;

  /** 部門類別 */
  private String department;

  /** 電子郵件 */
  private String email;
  /** 電話 */
  private String phone;
  /** 分機 */
  private String ext;

  // for AuditableDTO
  /** USER_ACCOUNT.ID */
  private AuditStatus auditStatus;

  private String user;

  @JsonIgnore
  @Override
  public AuditFunctionType getFunctionType() {
    return AuditFunctionType.BANK_ADMIN;
  }

  public static IssuerBankAdminDO valueOf(UserAccount entity) {
    return IssuerBankAdminDO.builder()
      .id(entity.getId())
      .issuerBankId(entity.getIssuerBankId())
      .account(entity.getAccount())
      .encryptedPassword(entity.getPassword())
      .userName(entity.getUsername())
      .department(entity.getDepartment())
      .email(entity.getEmail())
      .phone(entity.getPhone())
      .ext(entity.getExt())
      .auditStatus(AuditStatus.getStatusBySymbol(entity.getAuditStatus()))
      .build();

  }
}
