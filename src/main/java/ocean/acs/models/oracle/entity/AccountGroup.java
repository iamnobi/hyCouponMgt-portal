package ocean.acs.models.oracle.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ocean.acs.models.data_object.entity.AccountGroupDO;
import ocean.acs.models.entity.DBKey;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

@DynamicUpdate
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = DBKey.TABLE_ACCOUNT_GROUP)
public class AccountGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "account_group_seq_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name",
                            value = "ACCOUNT_GROUP_ID_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_group_seq_generator")
    @Column(name = DBKey.COL_ACCOUNT_GROUP_ID)
    private Long id;

    @NonNull
    @Column(name = DBKey.COL_ACCOUNT_GROUP_ACCOUNT_ID)
    private Long accountId;

    @NonNull
    @Column(name = DBKey.COL_ACCOUNT_GROUP_GROUP_ID)
    private Long groupId;

    @Column(name = DBKey.COL_ACCOUNT_GROUP_AUDIT_STATUS)
    private String auditStatus;

    @NonNull
    @Column(name = DBKey.COL_ACCOUNT_GROUP_CREATOR)
    private String creator;

    @NonNull
    @Column(name = DBKey.COL_ACCOUNT_GROUP_CREATE_MILLIS)
    private Long createMillis;

    public static AccountGroup valueOf(AccountGroupDO d) {
        AccountGroupBuilder builder = AccountGroup.builder()
          .accountId(d.getAccountId())
          .createMillis(d.getCreateMillis())
          .creator(d.getCreator())
          .groupId(d.getGroupId())
          .id(d.getId());
        if (d.getAuditStatus() != null)
            builder.auditStatus(d.getAuditStatus().getSymbol());
        return builder.build();
    }

}
