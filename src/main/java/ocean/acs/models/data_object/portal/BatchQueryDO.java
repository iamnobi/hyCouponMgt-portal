package ocean.acs.models.data_object.portal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import ocean.acs.commons.constant.PortalEnvironmentConstants;
import ocean.acs.commons.enumerator.TransStatus;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class BatchQueryDO extends PageQueryDO {

    private Long id;
    private String batchName;
    private Long startTime;
    private Long endTime;
    private String pan;
    private String authStatus;
    private TransStatus transStatus;
    private Long issuerBankId;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, PortalEnvironmentConstants.AUDIT_LOG_STYLE);
    }
}
