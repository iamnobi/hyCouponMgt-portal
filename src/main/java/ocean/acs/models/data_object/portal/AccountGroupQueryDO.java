package ocean.acs.models.data_object.portal;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class AccountGroupQueryDO extends AuditableDO {

    private static final long serialVersionUID = 1L;

    private Long accountId;
    private String accountName;
    private Long groupId;
    private String groupName;
    private AuditStatus auditStatus;
    private String creator;
    @Builder.Default
  private Long createMillis = System.currentTimeMillis();

  @Override
  public AuditFunctionType getFunctionType() {
    return AuditFunctionType.ACCOUNT_GROUP;
  }

}
