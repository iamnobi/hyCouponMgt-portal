package ocean.acs.models.data_object.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class WhiteListAttemptSettingDO extends OperatorInfoDO {

    private Long id;
    private Long panId;
    private Double amount;
    private String currency;
    private Integer permittedTimes;
    private Integer permittedQuota;
    private Long attemptExpiredTime;
    private String auditStatus;

    public WhiteListAttemptSettingDO(Long id, Long panId, Double amount, String currency,
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

    public static WhiteListAttemptSettingDO valueOf(
            ocean.acs.models.oracle.entity.WhiteListAttemptSetting e) {
        return new WhiteListAttemptSettingDO(e.getId(), e.getPanId(), e.getAmount(),
                e.getCurrency(), e.getPermittedTimes(), e.getPermittedQuota(),
                e.getAttemptExpiredTime(), e.getAuditStatus(), e.getCreator(), e.getCreateMillis(),
                e.getUpdater(), e.getUpdateMillis(), e.getDeleteFlag(), e.getDeleter(),
                e.getDeleteMillis());
    }
    
    public static WhiteListAttemptSettingDO valueOf(
            ocean.acs.models.sql_server.entity.WhiteListAttemptSetting e) {
        return new WhiteListAttemptSettingDO(e.getId(), e.getPanId(), e.getAmount(),
                e.getCurrency(), e.getPermittedTimes(), e.getPermittedQuota(),
                e.getAttemptExpiredTime(), e.getAuditStatus(), e.getCreator(), e.getCreateMillis(),
                e.getUpdater(), e.getUpdateMillis(), e.getDeleteFlag(), e.getDeleter(),
                e.getDeleteMillis());
    }

}
