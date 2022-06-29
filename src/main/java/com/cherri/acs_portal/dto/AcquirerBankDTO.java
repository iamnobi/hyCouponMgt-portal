package com.cherri.acs_portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ocean.acs.models.oracle.entity.AcquirerBank;

@Data
@AllArgsConstructor
public class AcquirerBankDTO {

  private Long id;
  private String name;
  private String threeDSServerRefNumber;
  private String threeDSOperatorId;

  public static AcquirerBankDTO valueOf(AcquirerBank entity) {
    return new AcquirerBankDTO(
        entity.getId(),
        entity.getName(),
        entity.getThreeDSServerRefNumber(),
        entity.getThreeDSOperatorId()
    );
  }
}
