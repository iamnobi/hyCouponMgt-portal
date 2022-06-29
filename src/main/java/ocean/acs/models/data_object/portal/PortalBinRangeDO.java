package ocean.acs.models.data_object.portal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ocean.acs.commons.constant.PortalEnvironmentConstants;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.enumerator.CardType;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortalBinRangeDO extends AuditableDO {

    private Long id;

    private Long issuerBankId;

    @NotBlank(message = "{column.notempty}")
    private String cardBrand;

    private String bankCode;

    @NotBlank(message = "{column.notempty}")
    private CardType cardType;

    @NotBlank(message = "{column.notempty}")
    private String startRange;

    @NotBlank(message = "{column.notempty}")
    private String endRange;

    private String user;

    private AuditStatus auditStatus = AuditStatus.UNKNOWN;

    public static PortalBinRangeDO valueOf(ocean.acs.models.oracle.entity.BinRange entity) {
        PortalBinRangeDO dto = new PortalBinRangeDO(entity.getId(), entity.getIssuerBankId(),
                entity.getCardBrand(), null,
                entity.getCardType() == null ? null : CardType.getByCode(entity.getCardType()),
                String.valueOf(entity.getStartBin()), String.valueOf(entity.getEndBin()),
                entity.getUpdater(), AuditStatus.getStatusBySymbol(entity.getAuditStatus()));
        return dto;
    }
    
    public static PortalBinRangeDO valueOf(ocean.acs.models.sql_server.entity.BinRange entity) {
        PortalBinRangeDO dto = new PortalBinRangeDO(entity.getId(), entity.getIssuerBankId(),
                entity.getCardBrand(), null,
                entity.getCardType() == null ? null : CardType.getByCode(entity.getCardType()),
                String.valueOf(entity.getStartBin()), String.valueOf(entity.getEndBin()),
                entity.getUpdater(), AuditStatus.getStatusBySymbol(entity.getAuditStatus()));
        return dto;
    }

    public static PortalBinRangeDO newInstance(long issuer_bank_id, String bankCode, String cardBrand, CardType cardType) {
        return PortalBinRangeDO.builder().issuerBankId(issuer_bank_id)
                .bankCode(bankCode)
                .cardBrand(cardBrand)
                .cardType(cardType).build();
    }

    @JsonIgnore
    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.SYS_BIN_RANGE;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, PortalEnvironmentConstants.AUDIT_LOG_STYLE);
    }
}
