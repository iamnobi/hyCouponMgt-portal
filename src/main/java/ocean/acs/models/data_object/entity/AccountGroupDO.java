package ocean.acs.models.data_object.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.AuditStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountGroupDO {

    private Long id;
    private Long accountId;
    private Long groupId;
    private AuditStatus auditStatus;
    private String creator;
    private Long createMillis;

    public static AccountGroupDO valueOf(ocean.acs.models.oracle.entity.AccountGroup e) {
        return new AccountGroupDO(e.getId(), e.getAccountId(), e.getGroupId(),
          AuditStatus.getStatusBySymbol(e.getAuditStatus()), e.getCreator(),
          e.getCreateMillis());
    }

    public static AccountGroupDO valueOf(ocean.acs.models.sql_server.entity.AccountGroup e) {
        return new AccountGroupDO(e.getId(), e.getAccountId(), e.getGroupId(),
          AuditStatus.getStatusBySymbol(e.getAuditStatus()), e.getCreator(),
          e.getCreateMillis());
    }

}
