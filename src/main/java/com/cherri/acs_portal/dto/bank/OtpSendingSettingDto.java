package com.cherri.acs_portal.dto.bank;

import com.cherri.acs_portal.controller.request.OtpSendingSettingUpdateReqDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.models.data_object.entity.OtpSendingSettingDO;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class OtpSendingSettingDto extends AuditableDTO {

    private static final long serialVersionUID = 1L;

    private Boolean orgEnable;
    private Boolean bankEnable;
    private String bankUrl;
    private String jwsSecretKey;

    private AuditStatus auditStatus;
    private String userAccount;

    @Override
    public void setAuditStatus(AuditStatus auditStatus) {
        this.auditStatus = auditStatus;
    }

    @JsonIgnore
    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.BANK_OTP_SENDING;
    }

    public static OtpSendingSettingDto valueOf(
      OtpSendingSettingUpdateReqDTO request) {
        OtpSendingSettingDto dto = new OtpSendingSettingDto();
        dto.setIssuerBankId(request.getIssuerBankId());
        dto.setOrgEnable(request.getOrgEnable());
        dto.setBankEnable(request.getBankEnable());
        dto.setBankUrl(request.getBankApiUrl());
        return dto;
    }

    public static OtpSendingSettingDto valueOf(
      OtpSendingSettingDO entity) {
        OtpSendingSettingDto dto = new OtpSendingSettingDto();
        dto.setId(entity.getId());
        dto.setIssuerBankId(entity.getIssuerBankId());
        dto.setOrgEnable(entity.getOrgEnable());
        dto.setBankEnable(entity.getBankEnable());
        dto.setBankUrl(entity.getBankUrl());
        dto.setJwsSecretKey(entity.getJwsSecretKey());
        dto.setAuditStatus(AuditStatus.getStatusBySymbol(entity.getAuditStatus()));
        return dto;
    }
}
