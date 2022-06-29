package ocean.acs.models.data_object.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class SmsTemplateDO extends OperatorInfoDO {

    private Long id;
    private Long issuerBankId;
    private Long expireMillis;
    private String verifyMessage;
    private String exceedLimitMessage;
    private String auditStatus;

    public SmsTemplateDO(Long id, Long issuerBankId, Long expireMillis, String verifyMessage,
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

    public static SmsTemplateDO valueOf(ocean.acs.models.oracle.entity.SmsTemplate e) {
        return new SmsTemplateDO(e.getId(), e.getIssuerBankId(), e.getExpireMillis(),
                e.getVerifyMessage(), e.getExceedLimitMessage(), e.getAuditStatus(), e.getCreator(),
                e.getCreateMillis(), e.getUpdater(), e.getUpdateMillis(), e.getDeleteFlag(),
                e.getDeleter(), e.getDeleteMillis());
    }
    
    public static SmsTemplateDO valueOf(ocean.acs.models.sql_server.entity.SmsTemplate e) {
        return new SmsTemplateDO(e.getId(), e.getIssuerBankId(), e.getExpireMillis(),
                e.getVerifyMessage(), e.getExceedLimitMessage(), e.getAuditStatus(), e.getCreator(),
                e.getCreateMillis(), e.getUpdater(), e.getUpdateMillis(), e.getDeleteFlag(),
                e.getDeleter(), e.getDeleteMillis());
    }

}
