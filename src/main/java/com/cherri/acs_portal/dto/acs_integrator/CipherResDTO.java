package com.cherri.acs_portal.dto.acs_integrator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class CipherResDTO extends BaseResDTO {
  private List<CipherData> data;

  public Optional<CipherData> getCipherData(Long cipherKey) {
    return getCipherData(cipherKey.toString());
  }

  public Optional<CipherData> getCipherData(String cipherKey) {
    if (isEmptyData()) {
      return Optional.empty();
    }
    return data.stream().filter(d -> d.getKey().equals(cipherKey)).findFirst();
  }

  public boolean isEmptyData() {
    return data == null || data.isEmpty();
  }
  public boolean isNotEmptyData() {
    return !isEmptyData();
  }
}
