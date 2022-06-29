package com.cherri.acs_portal.dto.system;

import com.cherri.acs_portal.controller.request.SmsOperationReqDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.models.data_object.entity.SmsTemplateDO;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@NoArgsConstructor
public class SmsTemplateDTO extends AuditableDTO {

    private Long expireMillis;
    private String verifyMessage;
    private String exceedLimitMessage;

    private AuditStatus auditStatus;
    private String operator;

    public SmsTemplateDTO(SmsOperationReqDTO reqDto) {
        this.id = reqDto.getId();
        this.issuerBankId = reqDto.getIssuerBankId();
        this.expireMillis = reqDto.getExpireMillis();
        this.verifyMessage = reqDto.getVerifyMessage();
        this.exceedLimitMessage = reqDto.getExceedLimitMessage();
    }

    public SmsTemplateDTO(
      Long id,
      Long issuerBankId,
      Long expireMillis,
      String verifyMessage,
      String exceedLimitMessage,
      AuditStatus auditStatus) {
        this.id = id;
        this.issuerBankId = issuerBankId;
        this.expireMillis = expireMillis;
        this.verifyMessage = verifyMessage;
        this.exceedLimitMessage = exceedLimitMessage;
        this.auditStatus = auditStatus;
    }

    public static SmsTemplateDTO valueOf(SmsTemplateDO entity) {
        return new SmsTemplateDTO(
          entity.getId(),
          entity.getIssuerBankId(),
          entity.getExpireMillis(),
          entity.getVerifyMessage(),
          entity.getExceedLimitMessage(),
          AuditStatus.getStatusBySymbol(entity.getAuditStatus()));
    }

    @JsonIgnore
    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.SYS_CHALLENGE_SMS_MSG;
    }
}
