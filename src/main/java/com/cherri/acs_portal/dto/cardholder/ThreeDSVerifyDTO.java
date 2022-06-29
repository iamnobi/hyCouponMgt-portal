package com.cherri.acs_portal.dto.cardholder;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.enumerator.CardType;
import ocean.acs.models.data_object.entity.PanInfoDO;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class ThreeDSVerifyDTO extends AuditableDTO {

    @NotNull(message = "{column.notempty}")
    private Long panId; // panId

    @NotNull(message = "{column.notempty}")
    private Boolean verifyEnabled;

    private Boolean otpLock;

    private CardType cardType;

    private String cardNumber;

    private AuditStatus auditStatus;

    /**
     * API操作者
     */
    private String user;

    /**
     * DB的值
     */
    private String creator;
    /**
     * DB的值
     */
    private String updater;

    @JsonIgnore
    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.THREE_DS_VERIFY_ENABLED;
    }

    public ThreeDSVerifyDTO(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }

    public static ThreeDSVerifyDTO valueOf(PanInfoDO panInfo) {
        ThreeDSVerifyDTO dto = new ThreeDSVerifyDTO();
        dto.setId(panInfo.getId());
        dto.setPanId(panInfo.getId());
        dto.setCardNumber(panInfo.getCardNumber());
        dto.setVerifyEnabled(panInfo.getThreeDSVerifyEnable());
        dto.setIssuerBankId(panInfo.getIssuerBankId());
        dto.setAuditStatus(AuditStatus.getStatusBySymbol(panInfo.getAuditStatus()));
        dto.setCreator(panInfo.getCreator());
        dto.setUpdater(panInfo.getUpdater());
        return dto;
    }
}
