package com.cherri.acs_portal.dto.bank;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.models.data_object.entity.IssuerBankDO;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class IssuerBankDto extends AuditableDTO {

    /**
     * 銀行名稱
     */
    @NotNull(message = "{column.notempty}")
    private String name;

    /**
     * 銀行代碼
     */
    private String code;

    private String threeDSMethodUrl;
    private String acsUrl;
    private String acsRefNumber;
    private String acsOperatorId;

    // for AuditableDTO
    private Long id;

    @NotNull(message = "{column.notempty}")
    private Long issuerBankId;

    private String symmetricKeyId;

    private String sensitiveDataKeyId;

    private AuditStatus auditStatus;

    private String user;

    public static IssuerBankDto valueOf(IssuerBankDO entity) {
        IssuerBankDto dto = new IssuerBankDto();
        dto.id = entity.getId();
        dto.name = entity.getName();
        dto.code = entity.getCode();
        dto.symmetricKeyId = entity.getSymmetricKeyId();
        dto.sensitiveDataKeyId = entity.getSensitiveDataKeyId();
        dto.threeDSMethodUrl = entity.getThreeDSMethodUrl();
        dto.acsUrl = entity.getAcsUrl();
        dto.acsRefNumber = entity.getAcsRefNumber();
        dto.acsOperatorId = entity.getAcsOperatorId();
        dto.auditStatus = AuditStatus.getStatusBySymbol(entity.getAuditStatus());
        return dto;
    }

    @JsonIgnore
    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.BANK_MANAGE;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }
}
