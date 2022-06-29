package com.cherri.acs_kernel.plugin.dto.hsm.result;

import com.cherri.acs_kernel.plugin.dto.ResultDTO;
import lombok.Data;

@Data
public class HSMSignResultDTO extends ResultDTO {

  private byte[] signature;

  private HSMSignResultDTO(boolean isSuccess, Exception exception, byte[] signature) {
    super(isSuccess, exception);
    this.signature = signature;
  }

  public static HSMSignResultDTO newInstanceOfSuccessWithData(byte[] signature) {
    return new HSMSignResultDTO(true, null, signature);
  }

  public static HSMSignResultDTO newInstanceOfFailure() {
    return new HSMSignResultDTO(false, null, null);
  }

  public static HSMSignResultDTO newInstanceOfExceptionHappened(Exception e) {
    return new HSMSignResultDTO(false, e, null);
  }
}
