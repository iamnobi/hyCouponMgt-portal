package com.cherri.acs_kernel.plugin.dto.hsm.result;

import com.cherri.acs_kernel.plugin.dto.ResultDTO;
import lombok.Data;

@Data
public class HSMCalculateCvvResultDTO extends ResultDTO {

  private String cvv;

  private HSMCalculateCvvResultDTO(boolean isSuccess, Exception exception, String cvv) {
    super(isSuccess, exception);
    this.cvv = cvv;
  }

  public static HSMCalculateCvvResultDTO newInstanceOfSuccessWithData(String cvv) {
    return new HSMCalculateCvvResultDTO(true, null, cvv);
  }

  public static HSMCalculateCvvResultDTO newInstanceOfFailure() {
    return new HSMCalculateCvvResultDTO(false, null, null);
  }

  public static HSMCalculateCvvResultDTO newInstanceOfExceptionHappened(Exception e) {
    return new HSMCalculateCvvResultDTO(false, e, null);
  }
}
