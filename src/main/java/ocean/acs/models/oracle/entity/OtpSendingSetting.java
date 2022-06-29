package ocean.acs.models.oracle.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import ocean.acs.models.data_object.portal.IssuerBankRelativeData;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ocean.acs.models.data_object.entity.OtpSendingSettingDO;
import ocean.acs.models.entity.DBKey;
import ocean.acs.models.entity.OperatorInfo;

@DynamicUpdate
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = DBKey.TABLE_OTP_SENDING_SETTING)
public class OtpSendingSetting extends OperatorInfo implements IssuerBankRelativeData {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "otp_sending_setting_seq_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name",
                            value = "OTP_SENDING_SETTING_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "otp_sending_setting_seq_generator")
    @Column(name = DBKey.COL_OTP_SENDING_SETTING_ID)
    private Long id;

    @NonNull
    @Column(name = DBKey.COL_OTP_SENDING_SETTING_ISSUER_BANK_ID)
    private Long issuerBankId;

    @NonNull
    @Column(name = DBKey.COL_OTP_SENDING_SETTING_ORG_ENABLE)
    private Boolean orgEnable;

    @NotNull
    @Column(name = DBKey.COL_OTP_SENDING_SETTING_BANK_ENABLE)
    private Boolean bankEnable;

    @Column(name = DBKey.COL_OTP_SENDING_SETTING_BANK_URL)
    private String bankUrl;

    @Column(name = DBKey.COL_OTP_SENDING_SETTING_JWE_RSA_PUBLIC_KEY)
    private byte[] jweRsaPublicKey;

    @Column(name = DBKey.COL_OTP_SENDING_SETTING_JWS_SECRET_KEY)
    private String jwsSecretKey;

    @Column(name = DBKey.COL_AUDIT_STATUS)
    private String auditStatus;

    public OtpSendingSetting(Long id, Long issuerBankId, Boolean orgEnable, Boolean bankEnable,
            String bankUrl, byte[] jweRsaPublicKey, String jwsSecretKey, String auditStatus,
            String creator, Long createMillis, String updater, Long updateMillis,
            Boolean deleteFlag, String deleter, Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.issuerBankId = issuerBankId;
        this.orgEnable = orgEnable;
        this.bankEnable = bankEnable;
        this.bankUrl = bankUrl;
        this.jweRsaPublicKey = jweRsaPublicKey;
        this.jwsSecretKey = jwsSecretKey;
        this.auditStatus = auditStatus;
    }

    public static OtpSendingSetting valueOf(OtpSendingSettingDO d) {
        return new OtpSendingSetting(d.getId(), d.getIssuerBankId(), d.getOrgEnable(),
                d.getBankEnable(), d.getBankUrl(), d.getJweRsaPublicKey(), d.getJwsSecretKey(),
                d.getAuditStatus(), d.getCreator(), d.getCreateMillis(), d.getUpdater(),
                d.getUpdateMillis(), d.getDeleteFlag(), d.getDeleter(), d.getDeleteMillis());
    }

}
