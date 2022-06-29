package com.cherri.acs_portal.dto.cardholder;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.dto.acs_integrator.Get3ds1OtpLockOriginDataResDto;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.models.data_object.entity.PanOtpStatisticsDO;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class UnlockOtpVerifyDTO extends AuditableDTO {

    @NotNull(message = "{column.notempty}")
    private Long panId;

    @JsonIgnore
    @Getter
    private final int verifyOtpCount = 0;

    private AuditStatus auditStatus;

    /**
     * Audit 操作者
     */
    private String user;

    /**
     * DB中的值
     */
    private String creator;

    /**
     * DB中的值
     */
    private String updater;

    /**
     * 卡片3DS 版本
     */
    private Integer version;

    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.UNLOCK_OTP;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }

    public static UnlockOtpVerifyDTO valueOf(Long issuerBankId, PanOtpStatisticsDO entity) {
        UnlockOtpVerifyDTO dto = new UnlockOtpVerifyDTO();
        dto.setId(entity.getId());
        dto.setPanId(entity.getPanInfoId());
        dto.setIssuerBankId(issuerBankId);
        dto.setAuditStatus(AuditStatus.getStatusBySymbol(entity.getAuditStatus()));
        dto.setCreator(entity.getCreator());
        dto.setUpdater(entity.getUpdater());
        return dto;
    }

    public static UnlockOtpVerifyDTO valueOf(Long panInfoId, Long issuerBankId,
      Get3ds1OtpLockOriginDataResDto resDto) {
        UnlockOtpVerifyDTO dto = new UnlockOtpVerifyDTO();
        dto.setPanId(panInfoId);
        dto.setIssuerBankId(issuerBankId);

        dto.setId(resDto.getId());
        dto.setAuditStatus(AuditStatus.getStatusBySymbol(resDto.getAuditStatus()));
        dto.setCreator(resDto.getCreator());
        dto.setUpdater(resDto.getUpdater());
        return dto;
    }
}
