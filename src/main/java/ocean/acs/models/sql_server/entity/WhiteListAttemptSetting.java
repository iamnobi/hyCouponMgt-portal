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
import ocean.acs.models.data_object.entity.WhiteListAttemptSettingDO;
import ocean.acs.models.entity.DBKey;
import ocean.acs.models.entity.OperatorInfo;

@DynamicUpdate
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = DBKey.TABLE_WHITE_LIST_ATTEMPT_SETTING)
public class WhiteListAttemptSetting extends OperatorInfo {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBKey.COL_WHITE_LIST_ATTEMPT_SETTING_ID)
    private Long id;

    @Column(name = DBKey.COL_WHITE_LIST_ATTEMPT_SETTING_PAN_ID)
    private Long panId;

    @Column(name = DBKey.COL_WHITE_LIST_ATTEMPT_SETTING_AMOUNT)
    private Double amount;

    @Column(name = DBKey.COL_WHITE_LIST_ATTEMPT_SETTING_CURRENCY)
    private String currency;

    @NonNull
    @Column(name = DBKey.COL_WHITE_LIST_ATTEMPT_SETTING_TRIES_PERMITTED)
    private Integer permittedTimes;

    @NonNull
    @Column(name = DBKey.COL_WHITE_LIST_ATTEMPT_SETTING_TRIES_QUOTA)
    private Integer permittedQuota;

    @NonNull
    @Column(name = DBKey.COL_WHITE_LIST_ATTEMPT_SETTING_ATTEMPT_EXPIRE_TIME)
    private Long attemptExpiredTime;

    @NonNull
    @Column(name = DBKey.COL_AUDIT_STATUS)
    private String auditStatus;

    public WhiteListAttemptSetting(Long id, Long panId, Double amount, String currency,
            Integer permittedTimes, Integer permittedQuota, Long attemptExpiredTime, String auditStatus,
            String creator, Long createMillis, String updater, Long updateMillis,
            Boolean deleteFlag, String deleter, Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.panId = panId;
        this.amount = amount;
        this.currency = currency;
        this.permittedTimes = permittedTimes;
        this.permittedQuota = permittedQuota;
        this.attemptExpiredTime = attemptExpiredTime;
        this.auditStatus = auditStatus;
    }

    public static WhiteListAttemptSetting valueOf(WhiteListAttemptSettingDO d) {
        return new WhiteListAttemptSetting(d.getId(), d.getPanId(), d.getAmount(), d.getCurrency(),
                d.getPermittedTimes(), d.getPermittedQuota(), d.getAttemptExpiredTime(),
                d.getAuditStatus(), d.getCreator(), d.getCreateMillis(), d.getUpdater(),
                d.getUpdateMillis(), d.getDeleteFlag(), d.getDeleter(), d.getDeleteMillis());
    }

}
