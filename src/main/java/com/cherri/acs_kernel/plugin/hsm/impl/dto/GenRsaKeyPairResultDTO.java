package com.cherri.acs_kernel.plugin.hsm.impl.dto;

import java.security.interfaces.RSAPublicKey;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GenRsaKeyPairResultDTO {
  private String privateKeyId;
  private RSAPublicKey publicKey;
}
