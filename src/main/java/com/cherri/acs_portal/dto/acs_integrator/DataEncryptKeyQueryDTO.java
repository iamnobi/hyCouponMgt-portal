package com.cherri.acs_portal.dto.acs_integrator;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bouncycastle.util.encoders.Base64;

@Data
@NoArgsConstructor
public class DataEncryptKeyQueryDTO {
  private Long id;
  private String encryptAesKey;
  private String aesKey;
  private byte[] oriAesKey;

  public DataEncryptKeyQueryDTO(Long id, String encryptAesKey) {
    this.id = id;
    this.encryptAesKey = encryptAesKey;
  }

  public void setAesKey(String aesKey) {
    this.aesKey = aesKey;
    this.oriAesKey = Base64.decode(aesKey);
  }
}
