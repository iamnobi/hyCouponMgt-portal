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
import ocean.acs.models.data_object.entity.SmsTemplateDO;
import ocean.acs.models.entity.DBKey;
import ocean.acs.models.entity.OperatorInfo;

@DynamicUpdate
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = DBKey.TABLE_SMS_TEMPLATE)
public class SmsTemplate extends OperatorInfo implements IssuerBankRelativeData {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "sms_template_seq_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name",
                            value = "SMS_TEMPLATE_ID_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sms_template_seq_generator")
    @Column(name = DBKey.COL_SMS_TEMPLATE_ID)
    private Long id;

    @Column(name = DBKey.COL_SMS_TEMPLATE_ISSUER_BANK_ID)
    private Long issuerBankId;

    @Column(name = DBKey.COL_SMS_TEMPLATE_EXPIRE_MILLIS)
    private Long expireMillis = 180000L; // 3分鐘

    @Column(name = DBKey.COL_SMS_TEMPLATE_VERIFY_MESSAGE)
    private String verifyMessage =
            "台端線上交易網路驗證安全碼為{{authCode}}，請確認網頁識別碼{{authId}}，交易卡號後四碼{{lastFour}}，金額{{amount}}";

    @Column(name = DBKey.COL_SMS_TEMPLATE_EXCEED_LIMIT_MESSAGE)
    private String exceedLimitMessage = "台端線上交易網路驗證次數已超過上限，已取消這次交易";

    @Column(name = DBKey.COL_AUDIT_STATUS)
    private String auditStatus;

    public SmsTemplate(Long id, Long issuerBankId, Long expireMillis, String verifyMessage,
            String exceedLimitMessage, String auditStatus, String creator, Long createMillis,
            String updater, Long updateMillis, Boolean deleteFlag, String deleter,
            Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.issuerBankId = issuerBankId;
        this.expireMillis = expireMillis;
        this.verifyMessage = verifyMessage;
        this.exceedLimitMessage = exceedLimitMessage;
        this.auditStatus = auditStatus;
    }

    public static SmsTemplate valueOf(SmsTemplateDO d) {
        return new SmsTemplate(d.getId(), d.getIssuerBankId(), d.getExpireMillis(),
                d.getVerifyMessage(), d.getExceedLimitMessage(), d.getAuditStatus(), d.getCreator(),
                d.getCreateMillis(), d.getUpdater(), d.getUpdateMillis(), d.getDeleteFlag(),
                d.getDeleter(), d.getDeleteMillis());
    }

}
