package ocean.acs.models.oracle.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
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
    @GenericGenerator(name = "white_list_attempt_setting_seq_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name",
                            value = "WHITE_LIST_ATTE_SET_ID_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "white_list_attempt_setting_seq_generator")
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

    // public static WhiteListAttemptSetting valueOf(AttemptGrantedDTO grantDTO)
    // {
    // WhiteListAttemptSetting attemptSetting = new WhiteListAttemptSetting();
    // attemptSetting.setPanId(grantDTO.getPanId());
    // attemptSetting.setAmount(grantDTO.getMaxMoney());
    // attemptSetting.setCurrency(grantDTO.getCurrency());
    // attemptSetting.setPermittedTimes(grantDTO.getTriesPermitted());
    // attemptSetting.setPermittedQuota(grantDTO.getTriesPermitted());
    // // 當前時間 + (有效時間(30分鐘)*秒(60)*毫秒(1000))
    // long now = System.currentTimeMillis();
    // long expiredTime = now + (EnvironmentConstants.WHITELIST_ATTEMPT_AVAILABLE_DURATION * 60 *
    // 1000);
    // attemptSetting.setAttemptExpiredTime(expiredTime);
    // attemptSetting.setAuditStatus(grantDTO.getAuditStatus().getSymbol());
    // attemptSetting.setCreateMillis(now);
    // attemptSetting.setCreator(grantDTO.getCreator());
    //
    // return attemptSetting;
    // }

}
