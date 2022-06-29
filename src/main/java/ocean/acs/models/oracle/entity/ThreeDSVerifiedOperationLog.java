package ocean.acs.models.oracle.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import ocean.acs.models.data_object.portal.IssuerBankRelativeData;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
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
public class ThreeDSVerifiedOperationLog extends OperatorInfo implements IssuerBankRelativeData {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "three_d_s_verify_enabled_log_seq_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name",
                            value = "T_D_S_V_E_L_ID_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "three_d_s_verify_enabled_log_seq_generator")
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
