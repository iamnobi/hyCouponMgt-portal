package ocean.acs.models.sql_server.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.DynamicUpdate;
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
public class OtpSendingSetting extends OperatorInfo {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
