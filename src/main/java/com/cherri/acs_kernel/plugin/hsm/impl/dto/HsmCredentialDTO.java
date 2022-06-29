package com.cherri.acs_kernel.plugin.hsm.impl.dto;

import lombok.Data;

@Data
public class HsmCredentialDTO {
  private String username;
  private String password;
  private String partition;
}
