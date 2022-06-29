package ocean.acs.models.sql_server.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ocean.acs.models.data_object.entity.ThreeDSVerifiedOperationLogDO;
import ocean.acs.models.entity.DBKey;
import ocean.acs.models.entity.OperatorInfo;

@DynamicUpdate
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = DBKey.TABLE_THREE_D_S_VERIFY_ENABLED_LOG)
public class ThreeDSVerifiedOperationLog extends OperatorInfo {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBKey.COL_THREE_D_S_VERIFY_ENABLED_LOG_ID)
    private Long id;

    @Column(name = DBKey.COL_THREE_D_S_VERIFY_ENABLED_LOG_ISSUER_BANK_ID)
    private Long issuerBankId;

    @NonNull
    @Column(name = DBKey.COL_THREE_D_S_VERIFY_ENABLED_LOG_PAN_ID)
    private Long panId;

    @Column(name = DBKey.COL_THREE_D_S_VERIFY_ENABLED_LOG_ENABLED)
    private Boolean threeDSVerifiedEnabled;

    public ThreeDSVerifiedOperationLog(Long id, Long issuerBankId, Long panId,
            Boolean threeDSVerifiedEnabled, String creator, Long createMillis, String updater,
            Long updateMillis, Boolean deleteFlag, String deleter, Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.issuerBankId = issuerBankId;
        this.panId = panId;
        this.threeDSVerifiedEnabled = threeDSVerifiedEnabled;
    }

    public static ThreeDSVerifiedOperationLog valueOf(ThreeDSVerifiedOperationLogDO d) {
        return new ThreeDSVerifiedOperationLog(d.getId(), d.getIssuerBankId(), d.getPanId(),
                d.getThreeDSVerifiedEnabled(), d.getCreator(), d.getCreateMillis(), d.getUpdater(),
                d.getUpdateMillis(), d.getDeleteFlag(), d.getDeleter(), d.getDeleteMillis());
    }

}
