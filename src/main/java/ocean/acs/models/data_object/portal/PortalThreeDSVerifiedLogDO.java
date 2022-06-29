package ocean.acs.models.data_object.portal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortalThreeDSVerifiedLogDO {

    // private String cardNumber;
    private Long panId;
    private Long issuerBankId;
    private Boolean lockStatus;
    private Long operationTime;
    private String creator;

    public PortalThreeDSVerifiedLogDO(Long panId, Long issuerBankId, Boolean threeDSVerifiedEnabled,
            String creator, Long createMillis) {
        this.panId = panId;
        this.issuerBankId = issuerBankId;
        this.lockStatus = threeDSVerifiedEnabled;
        this.creator = creator;
        this.operationTime = createMillis;
    }

    public static PortalThreeDSVerifiedLogDO valueOf(
            ocean.acs.models.oracle.entity.ThreeDSVerifiedOperationLog e) {
        return new PortalThreeDSVerifiedLogDO(e.getPanId(), e.getIssuerBankId(),
                e.getThreeDSVerifiedEnabled(), e.getCreator(), e.getCreateMillis());
    }

    public static PortalThreeDSVerifiedLogDO valueOf(
            ocean.acs.models.sql_server.entity.ThreeDSVerifiedOperationLog e) {
        return new PortalThreeDSVerifiedLogDO(e.getPanId(), e.getIssuerBankId(),
                e.getThreeDSVerifiedEnabled(), e.getCreator(), e.getCreateMillis());
    }

}
