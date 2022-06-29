package com.cherri.acs_portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ocean.acs.models.oracle.entity.AcquirerBank;
import ocean.acs.models.oracle.entity.AcquirerBankAcquirerBin;

@Data
@AllArgsConstructor
public class AcquirerBinDTO {

  private Long id;
  private String acquirerBin;

  public static AcquirerBinDTO valueOf(AcquirerBankAcquirerBin entity) {
    return new AcquirerBinDTO(entity.getId(), entity.getAcquirerBin());
  }
}
