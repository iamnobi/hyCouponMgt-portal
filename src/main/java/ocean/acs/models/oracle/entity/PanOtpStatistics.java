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
import ocean.acs.models.data_object.entity.PanOtpStatisticsDO;
import ocean.acs.models.entity.DBKey;
import ocean.acs.models.entity.OperatorInfo;

@DynamicUpdate
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = DBKey.TABLE_PAN_OTP_STATISTICS)
public class PanOtpStatistics extends OperatorInfo {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "pan_otp_statistics_seq_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name",
                            value = "PAN_OTP_STATISTICS_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "pan_otp_statistics_seq_generator")
    @Column(name = DBKey.COL_PAN_OTP_STATISTICS_ID)
    private Long id;

    @Column(name = DBKey.COL_PAN_OTP_STATISTICS_PAN_INFO_ID)
    private Long panInfoId;

    @Column(name = DBKey.COL_PAN_OTP_STATISTICS_VERIFY_OTP_COUNT)
    private Integer verifyOtpCount = 0;

    @Column(name = DBKey.COL_AUDIT_STATUS)
    private String auditStatus;

    public PanOtpStatistics(Long id, Long panInfoId, Integer verifyOtpCount, String auditStatus,
            String creator, Long createMillis, String updater, Long updateMillis,
            Boolean deleteFlag, String deleter, Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.panInfoId = panInfoId;
        this.verifyOtpCount = verifyOtpCount;
        this.auditStatus = auditStatus;
    }

    public static PanOtpStatistics valueOf(PanOtpStatisticsDO d) {
        return new PanOtpStatistics(d.getId(), d.getPanInfoId(), d.getVerifyOtpCount(),
                d.getAuditStatus(), d.getCreator(), d.getCreateMillis(), d.getUpdater(),
                d.getUpdateMillis(), d.getDeleteFlag(), d.getDeleter(), d.getDeleteMillis());
    }

    public static PanOtpStatistics newInstance(Long panInfoId, String creator) {
        PanOtpStatistics otpStatistics = new PanOtpStatistics();
        otpStatistics.setPanInfoId(panInfoId);
        otpStatistics.setVerifyOtpCount(0);
        otpStatistics.setAuditStatus("P"); // PUBLISHED
        otpStatistics.setCreator(creator);
        otpStatistics.setCreateMillis(System.currentTimeMillis());
        return otpStatistics;
    }

}
