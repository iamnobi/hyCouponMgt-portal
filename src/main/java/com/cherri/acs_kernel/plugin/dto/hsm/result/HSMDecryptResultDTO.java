package com.cherri.acs_kernel.plugin.dto.hsm.result;

import com.cherri.acs_kernel.plugin.dto.ResultDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class HSMDecryptResultDTO extends ResultDTO {

  private byte[] plainText;

  private HSMDecryptResultDTO(boolean isSuccess, Exception exception, byte[] plainText) {
    super(isSuccess, exception);
    this.plainText = plainText;
  }

  public static HSMDecryptResultDTO newInstanceOfSuccessWithData(byte[] plainText) {
    return new HSMDecryptResultDTO(true, null, plainText);
  }

  public static HSMDecryptResultDTO newInstanceOfFailure() {
    return new HSMDecryptResultDTO(false, null, null);
  }

  public static HSMDecryptResultDTO newInstanceOfExceptionHappened(Exception e) {
    return new HSMDecryptResultDTO(false, e, null);
  }
}
