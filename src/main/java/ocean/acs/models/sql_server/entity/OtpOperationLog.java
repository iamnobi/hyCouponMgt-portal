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
import ocean.acs.models.data_object.entity.OtpOperationLogDO;
import ocean.acs.models.entity.DBKey;
import ocean.acs.models.entity.OperatorInfo;

@DynamicUpdate
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = DBKey.TABLE_OTP_LOCK_LOG)
public class OtpOperationLog extends OperatorInfo {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
