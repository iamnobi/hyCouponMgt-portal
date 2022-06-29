package com.cherri.acs_kernel.plugin.dto.hsm.result;

import com.cherri.acs_kernel.plugin.dto.ResultDTO;
import lombok.Data;

@Data
public class HSMCalculateAvResultDTO extends ResultDTO {

  private String av;

  private HSMCalculateAvResultDTO(boolean isSuccess, Exception exception, String av) {
    super(isSuccess, exception);
    this.av = av;
  }

  public static HSMCalculateAvResultDTO newInstanceOfSuccessWithData(String cvv) {
    return new HSMCalculateAvResultDTO(true, null, cvv);
  }

  public static HSMCalculateAvResultDTO newInstanceOfFailure() {
    return new HSMCalculateAvResultDTO(false, null, null);
  }

  public static HSMCalculateAvResultDTO newInstanceOfExceptionHappened(Exception e) {
    return new HSMCalculateAvResultDTO(false, e, null);
  }
}
