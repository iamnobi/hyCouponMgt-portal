package com.cherri.acs_portal.dto.acs_integrator;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class CipherData {
  private String key;
  private String value = "";
}
