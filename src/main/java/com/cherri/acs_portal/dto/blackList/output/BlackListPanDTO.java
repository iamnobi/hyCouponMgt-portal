package com.cherri.acs_portal.dto.blackList.output;

import com.cherri.acs_portal.controller.request.BlackListPanOperationReqDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.enumerator.TransStatus;
import ocean.acs.models.data_object.entity.BlackListPanDO;
import ocean.acs.models.data_object.entity.PanInfoDO;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BlackListPanDTO extends AuditableDTO {

    private Long id;
    private Long issuerBankId;
    private Long panId;

    private String cardNumber;
    private TransStatus transStatus;
    private Long blackListPanBatchId;

    private AuditStatus auditStatus;
    private String creator;
    private String updater;

    public BlackListPanDTO(BlackListPanOperationReqDTO reqDto) {
        this.issuerBankId = reqDto.getIssuerBankId();
        this.cardNumber = reqDto.getRealCardNumber();
    }

    public static BlackListPanDTO valueOf(PanInfoDO panInfo, BlackListPanDO blackListPan) {
        return new BlackListPanDTO(
          blackListPan.getId(),
          panInfo.getIssuerBankId(),
          blackListPan.getPanId(),
          panInfo.getCardNumber(),
          TransStatus.codeOf(blackListPan.getTransStatus()),
          blackListPan.getBlackListPanBatchId(),
          AuditStatus.getStatusBySymbol(blackListPan.getAuditStatus()),
          blackListPan.getCreator(),
          blackListPan.getUpdater());
    }

    @Override
    public Long getIssuerBankId() {
        return this.issuerBankId;
    }

    @Override
    public void setAuditStatus(AuditStatus auditStatus) {
        this.auditStatus = auditStatus;
    }

    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.BLACK_LIST_PAN;
    }
}
