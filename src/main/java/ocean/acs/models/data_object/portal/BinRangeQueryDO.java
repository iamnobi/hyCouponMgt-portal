package ocean.acs.models.data_object.portal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.commons.constant.PortalEnvironmentConstants;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BinRangeQueryDO extends PageQueryDO {

    @NotNull(message = "{column.notempty}")
    private Long issuerBankId;

    private String cardBrand;

    public static BinRangeQueryDO newInstance(Long issuerBankId
    ,String cardBrand) {
        BinRangeQueryDO binRangeQueryDO = new BinRangeQueryDO();
        binRangeQueryDO.setIssuerBankId(issuerBankId);
        binRangeQueryDO.setCardBrand(cardBrand);
        return binRangeQueryDO;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, PortalEnvironmentConstants.AUDIT_LOG_STYLE);
    }
}
