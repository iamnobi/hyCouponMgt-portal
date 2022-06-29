package com.cherri.acs_portal.dto.veLog;

import lombok.Data;
import ocean.acs.models.oracle.entity.VELogPortal;

@Data
public class VELogQueryResultDTO {
  private String id;
  private String issuerName;
  private String cardNumber;
  private long vereqTimeMillis;
  private String vereqStatus;
  private String merchantId;
  private String acctId;
  private String ireqCode;
  private boolean pareqReceived;

  public static VELogQueryResultDTO valueOf(VELogPortal veLogPortal) {
    VELogQueryResultDTO dto = new VELogQueryResultDTO();
    dto.setId(veLogPortal.getId());
    dto.setIssuerName(
        veLogPortal.getIssuerBank() == null ? null : veLogPortal.getIssuerBank().getName());
    dto.setCardNumber(
        veLogPortal.getPanInfo() == null
            ? veLogPortal.getMaskCardNumber()
            : veLogPortal.getPanInfo().getCardNumber());
    dto.setVereqTimeMillis(veLogPortal.getCreateMillis());
    dto.setVereqStatus(veLogPortal.getChEnrolled());
    dto.setMerchantId(veLogPortal.getMerchantMerId());
    dto.setAcctId(veLogPortal.getChAcctId());
    dto.setIreqCode(veLogPortal.getErrorCode());
    dto.setPareqReceived(veLogPortal.getPaLog() != null);
    return dto;
  }
}
