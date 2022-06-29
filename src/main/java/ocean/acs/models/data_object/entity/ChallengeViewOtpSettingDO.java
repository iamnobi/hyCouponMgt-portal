package ocean.acs.models.data_object.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class ChallengeViewOtpSettingDO extends OperatorInfoDO {

    private Long id;
    private Long issuerBankId;

    private String languageCode;
    private Integer maxResendTimes;
    private Integer maxChallengeTimes;
    private String auditStatus;

    public ChallengeViewOtpSettingDO(
            Long id,
            Long issuerBankId,
            Integer maxResendTimes,
            Integer maxChallengeTimes,
            String auditStatus,
            String creator,
            Long createMillis,
            String updater,
            Long updateMillis,
            Boolean deleteFlag,
            String deleter,
            Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.issuerBankId = issuerBankId;
        this.maxResendTimes = maxResendTimes;
        this.maxChallengeTimes = maxChallengeTimes;
        this.auditStatus = auditStatus;
    }

    public static ChallengeViewOtpSettingDO valueOf(
            ocean.acs.models.oracle.entity.ChallengeViewOtpSetting e) {
        return new ChallengeViewOtpSettingDO(
                e.getId(),
                e.getIssuerBankId(),
                e.getMaxResendTimes(),
                e.getMaxChallengeTimes(),
                e.getAuditStatus(),
                e.getCreator(),
                e.getCreateMillis(),
                e.getUpdater(),
                e.getUpdateMillis(),
                e.getDeleteFlag(),
                e.getDeleter(),
                e.getDeleteMillis());
    }

    public static ChallengeViewOtpSettingDO valueOf(
            ocean.acs.models.sql_server.entity.ChallengeViewOtpSetting e) {
        return new ChallengeViewOtpSettingDO(
                e.getId(),
                e.getIssuerBankId(),
                e.getMaxResendTimes(),
                e.getMaxChallengeTimes(),
                e.getAuditStatus(),
                e.getCreator(),
                e.getCreateMillis(),
                e.getUpdater(),
                e.getUpdateMillis(),
                e.getDeleteFlag(),
                e.getDeleter(),
                e.getDeleteMillis());
    }
}
