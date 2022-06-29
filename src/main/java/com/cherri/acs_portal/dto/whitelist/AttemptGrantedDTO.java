package com.cherri.acs_portal.dto.whitelist;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.dto.acs_integrator.Get3ds1AttemptGrantedOriginDataResDto;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.validator.validation.CurrencyCodeValidation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.neovisionaries.i18n.CurrencyCode;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.models.data_object.entity.WhiteListAttemptSettingDO;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@JsonPropertyOrder({"issuerBankId", "panId", "version"})
public class AttemptGrantedDTO extends AuditableDTO {

    @NotNull(message = "{column.notempty}")
    private Long panId;

    /** 彈性授權金額 */
    @NotNull(message = "{column.notempty}")
    private Double maxMoney;

    /** Format:Numeric */
    @NotNull(message = "{column.notempty}")
    @CurrencyCodeValidation
    private String currency;

    /** 彈性授權次數 */
    @NotNull(message = "{column.notempty}")
    private Long triesPermitted;

    private String creator;
    private Long createMillis;
    private Long expireMillis;

    private AuditStatus auditStatus;

    public static AttemptGrantedDTO valueOf(WhiteListAttemptSettingDO attemptSetting) {
        AttemptGrantedDTO attemptDTO = new AttemptGrantedDTO();
        attemptDTO.setId(attemptSetting.getId());
        attemptDTO.setPanId(attemptSetting.getPanId());
        attemptDTO.setExpireMillis(attemptSetting.getAttemptExpiredTime());
        attemptDTO.setMaxMoney(attemptSetting.getAmount());
        attemptDTO.setCurrency(attemptSetting.getCurrency());
        attemptDTO.setTriesPermitted(Long.valueOf(attemptSetting.getPermittedTimes()));
        attemptDTO.setCreator(attemptSetting.getCreator());
        attemptDTO.setCreateMillis(attemptSetting.getCreateMillis());
        return attemptDTO;
    }

    public static AttemptGrantedDTO valueOf(
      Get3ds1AttemptGrantedOriginDataResDto resDto) {
        AttemptGrantedDTO attemptGrantedDTO = new AttemptGrantedDTO();
        attemptGrantedDTO.setId(resDto.getId());
        attemptGrantedDTO.setMaxMoney(resDto.getMaxMoney());
        attemptGrantedDTO.setCurrency(CurrencyCode.TWD.getName());
        attemptGrantedDTO.setTriesPermitted(resDto.getTriesPermitted());
        attemptGrantedDTO.setExpireMillis(resDto.getActiveTime());
        return attemptGrantedDTO;
    }

    @JsonIgnore
    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.ATTEMPT;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }
}
