package ocean.acs.models.sql_server.entity;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ocean.acs.models.entity.DBKey;
import ocean.acs.models.entity.OperatorInfo;
import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = DBKey.TABLE_ERROR_ISSUE_GROUP)
public class ErrorIssueGroup extends OperatorInfo {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBKey.COL_ERROR_ISSUE_GROUP_ID)
    private Long id;

    @Column(name = DBKey.COL_ERROR_ISSUE_GROUP_NAME)
    private String name;

    @NonNull
    @Column(name = DBKey.COL_AUDIT_STATUS)
    private String auditStatus;

    @OneToMany(mappedBy = "issueGroup", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderBy(value = "code asc")
    private List<ErrorCodeMapping> codeList;

}
