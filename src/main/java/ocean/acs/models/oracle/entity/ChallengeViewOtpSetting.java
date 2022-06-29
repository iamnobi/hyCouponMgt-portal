package ocean.acs.models.oracle.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.models.data_object.entity.ChallengeViewOtpSettingDO;
import ocean.acs.models.data_object.portal.IssuerBankRelativeData;
import ocean.acs.models.entity.DBKey;
import ocean.acs.models.entity.OperatorInfo;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@DynamicUpdate
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = DBKey.TABLE_CHALLENGE_VIEW_OTP_SETTING)
public class ChallengeViewOtpSetting extends OperatorInfo implements IssuerBankRelativeData {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(
            name = "challenge_view_otp_setting_seq_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @org.hibernate.annotations.Parameter(
                        name = "sequence_name",
                        value = "CHALLENGE_VIEW_OTP_SET_ID_SEQ"),
                @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            })
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "challenge_view_otp_setting_seq_generator")
    @Column(name = DBKey.COL_CHALLENGE_VIEW_OTP_SETTING_ID)
    private Long id;

    @Column(name = DBKey.COL_CHALLENGE_VIEW_OTP_SETTING_ISSUER_BANK_ID)
    private Long issuerBankId;

    @Column(name = DBKey.COL_CHALLENGE_VIEW_OTP_SETTING_MAX_RESEND_TIMES)
    private Integer maxResendTimes;

    @Column(name = DBKey.COL_CHALLENGE_VIEW_OTP_SETTING_MAX_CHALLENGE_TIMES)
    private Integer maxChallengeTimes;

    @Column(name = DBKey.COL_AUDIT_STATUS)
    private String auditStatus;

    public ChallengeViewOtpSetting(
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

    public static ChallengeViewOtpSetting valueOf(ChallengeViewOtpSettingDO d) {
        return new ChallengeViewOtpSetting(
                d.getId(),
                d.getIssuerBankId(),
                d.getMaxResendTimes(),
                d.getMaxChallengeTimes(),
                d.getAuditStatus(),
                d.getCreator(),
                d.getCreateMillis(),
                d.getUpdater(),
                d.getUpdateMillis(),
                d.getDeleteFlag(),
                d.getDeleter(),
                d.getDeleteMillis());
    }
}
