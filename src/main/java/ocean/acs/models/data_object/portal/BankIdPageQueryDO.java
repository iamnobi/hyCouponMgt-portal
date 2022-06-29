package ocean.acs.models.data_object.portal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ocean.acs.commons.constant.PortalEnvironmentConstants;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class BankIdPageQueryDO extends PageQueryDO {

    private Long issuerBankId;

    public void setIssuerBankId(Long reqIssuerBankId, Long sessionIssuerBankId) {
        this.issuerBankId = reqIssuerBankId == null ? sessionIssuerBankId : reqIssuerBankId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, PortalEnvironmentConstants.AUDIT_LOG_STYLE);
    }
}
