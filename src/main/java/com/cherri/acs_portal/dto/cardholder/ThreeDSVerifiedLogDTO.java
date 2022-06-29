package com.cherri.acs_portal.dto.cardholder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.models.data_object.entity.ThreeDSVerifiedOperationLogDO;
import ocean.acs.models.data_object.portal.PortalThreeDSVerifiedLogDO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThreeDSVerifiedLogDTO {

    private String cardNumber;
    private Long panId;
    private Long issuerBankId;
    private Boolean lockStatus;
    private Long operationTime;
    private String creator;

    public ThreeDSVerifiedLogDTO(
      Long panId,
      Long issuerBankId,
      Boolean threeDSVerifiedEnabled,
      String creator,
      Long createMillis) {
        this.panId = panId;
        this.issuerBankId = issuerBankId;
        this.lockStatus = threeDSVerifiedEnabled;
        this.creator = creator;
        this.operationTime = createMillis;
    }

    public static ThreeDSVerifiedLogDTO valueOf(ThreeDSVerifiedOperationLogDO entity) {
        return new ThreeDSVerifiedLogDTO(
          entity.getPanId(),
          entity.getIssuerBankId(),
          entity.getThreeDSVerifiedEnabled(),
          entity.getCreator(),
          entity.getCreateMillis());
    }

    public static ThreeDSVerifiedLogDTO valueOf(PortalThreeDSVerifiedLogDO entity) {
        return new ThreeDSVerifiedLogDTO(
          entity.getPanId(),
          entity.getIssuerBankId(),
          entity.getLockStatus(),
          entity.getCreator(),
          entity.getOperationTime());
    }
}
