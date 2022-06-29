package ocean.acs.models.data_object.portal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.commons.constant.PortalEnvironmentConstants;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WhiteListPanQueryDO extends TimePageQueryDO {

    private String cardBrand;
    private Long issuerBankId;
    private String cardNumberHash;

    public static WhiteListPanQueryDO newInstance(
            Long startTime,
            Long endTime,
            String cardBrand,
            String cardNumberHash,
            Long issuerBankId) {

        WhiteListPanQueryDO whiteListPanQueryDO = new WhiteListPanQueryDO();
        whiteListPanQueryDO.setStartTime(startTime);
        whiteListPanQueryDO.setEndTime(endTime);
        whiteListPanQueryDO.setCardBrand(cardBrand);
        whiteListPanQueryDO.setCardNumberHash(cardNumberHash);
        whiteListPanQueryDO.setIssuerBankId(issuerBankId);
        return whiteListPanQueryDO;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, PortalEnvironmentConstants.AUDIT_LOG_STYLE);
    }
}
