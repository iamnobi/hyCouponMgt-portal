package com.cherri.acs_portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.models.oracle.entity.AcquirerBank3dsRefNum;

/**
 * AcquirerBank3dsRefNumDTO
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AcquirerBank3dsRefNumDTO {
  private Long id;
  private String sdkRefNumber;

  public static AcquirerBank3dsRefNumDTO valueOf(AcquirerBank3dsRefNum acquirerBank3dsRefNum) {
    return new AcquirerBank3dsRefNumDTO(
            acquirerBank3dsRefNum.getId(),
            acquirerBank3dsRefNum.getSdkReferenceNumber()
    );
  }


}
