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
import ocean.acs.models.data_object.entity.OtpOperationLogDO;
import ocean.acs.models.entity.DBKey;
import ocean.acs.models.entity.OperatorInfo;

@DynamicUpdate
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = DBKey.TABLE_OTP_LOCK_LOG)
public class OtpOperationLog extends OperatorInfo implements IssuerBankRelativeData {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "otp_lock_log_seq_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name",
                            value = "OTP_LOCK_LOG_ID_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "otp_lock_log_seq_generator")
    @Column(name = DBKey.COL_OTP_LOCK_LOG_ID)
    private Long id;

    @NonNull
    @Column(name = DBKey.COL_OTP_LOCK_LOG_ISSUER_BANK_ID)
    private Long issuerBankId;

    @NonNull
    @Column(name = DBKey.COL_OTP_LOCK_LOG_PAN_ID)
    private Long panId;

    @Column(name = DBKey.COL_OTP_LOCK_LOG_ENABLED)
    private Boolean otpEnabled;

    public OtpOperationLog(Long id, Long issuerBankId, Long panId, Boolean otpEnabled,
            String creator, Long createMillis, String updater, Long updateMillis,
            Boolean deleteFlag, String deleter, Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.issuerBankId = issuerBankId;
        this.panId = panId;
        this.otpEnabled = otpEnabled;
    }

    public static OtpOperationLog valueOf(OtpOperationLogDO d) {
        return new OtpOperationLog(d.getId(), d.getIssuerBankId(), d.getPanId(), d.getOtpEnabled(),
                d.getCreator(), d.getCreateMillis(), d.getUpdater(), d.getUpdateMillis(), d.getDeleteFlag(),
                d.getDeleter(), d.getDeleteMillis());
    }

    // private OtpOperationLog(@NonNull Long issuerBankId, @NonNull Long panId, Boolean otpEnabled,
    // String creator) {
    // this.issuerBankId = issuerBankId;
    // this.panId = panId;
    // this.otpEnabled = otpEnabled;
    // super.setCreator(creator);
    // super.setCreateMillis(System.currentTimeMillis());
    // }

    // public static OtpOperationLog newInstance(UnlockOtpVerifyDTO dto)
    // {
    // return new OtpOperationLog(dto.getIssuerBankId(), dto.getPanId(), true, dto.getUser());
    // }

}
