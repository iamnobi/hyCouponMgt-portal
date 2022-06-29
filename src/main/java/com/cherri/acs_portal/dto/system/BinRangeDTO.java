package com.cherri.acs_portal.dto.system;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.dto.acs_integrator.BinRangeV1ListResDTO;
import com.cherri.acs_portal.dto.acs_integrator.BinRangeV1ResDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.enumerator.CardType;
import ocean.acs.models.data_object.entity.BinRangeDO;
import ocean.acs.models.data_object.portal.PortalBinRangeDO;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class BinRangeDTO extends AuditableDTO {

    private Long id;

    private Long issuerBankId;

    private String cardBrand;

    private String cardType;

    @NotBlank(message = "{column.notempty}")
    private String startRange;

    @NotBlank(message = "{column.notempty}")
    private String endRange;

    private String user;

    private AuditStatus auditStatus = AuditStatus.UNKNOWN;


    public static BinRangeDTO valueOf(BinRangeDO entity) {
        BinRangeDTO dto =
          new BinRangeDTO(
            entity.getId(),
            entity.getIssuerBankId(),
            entity.getCardBrand(),
            entity.getCardType() == null ? "" : CardType.getByCode(entity.getCardType()).name(),
            String.valueOf(entity.getStartBin()),
            String.valueOf(entity.getEndBin()),
            entity.getUpdater(),
            AuditStatus.getStatusBySymbol(entity.getAuditStatus()));
        return dto;
    }


    public static BinRangeDTO valueOf(PortalBinRangeDO entity) {
        BinRangeDTO dto =
          new BinRangeDTO(
            entity.getId(),
            entity.getIssuerBankId(),
            entity.getCardBrand(),
            entity.getCardType().name(),
            String.valueOf(entity.getStartRange()),
            String.valueOf(entity.getEndRange()),
            entity.getUser(),
            entity.getAuditStatus());
        return dto;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }

    @JsonIgnore
    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.SYS_BIN_RANGE;
    }

}
