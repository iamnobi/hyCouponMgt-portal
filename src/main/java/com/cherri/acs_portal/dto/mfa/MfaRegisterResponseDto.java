package com.cherri.acs_portal.dto.mfa;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MfaRegisterResponseDto {

  private byte[] qrCodeString;
}
