package com.cherri.acs_portal.dto.rba;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClassicRbaStatusDto {
  private String ddcaDelay;
  private String ddcaStatus;
}
