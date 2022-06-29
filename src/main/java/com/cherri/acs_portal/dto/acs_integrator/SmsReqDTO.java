package com.cherri.acs_portal.dto.acs_integrator;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/** 向ACS Integrator查詢簡訊紀錄請求參數物件 */
@AllArgsConstructor
@ToString
@Data
public class SmsReqDTO {

  @JsonProperty("kernelTransId")
  private String acsTransId;
}
