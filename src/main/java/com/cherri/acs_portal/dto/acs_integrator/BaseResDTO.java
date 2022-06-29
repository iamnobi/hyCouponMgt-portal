package com.cherri.acs_portal.dto.acs_integrator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(
    value = {"status", "msg", "empty", "success", "failed", "duplicateData"},
    allowSetters = true)
public class BaseResDTO {
  private String status;

  private String msg;

  public boolean isSuccess() {
    return "0000".equals(status);
  }

  public boolean isFailed() {
    return !isSuccess();
  }

  public boolean isEmpty() {
    return "0016".equals(status);
  }

  public boolean isDuplicateData() {
    return "0015".equals(status);
  }
}
