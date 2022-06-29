package com.cherri.acs_portal.controller.request;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResetMimaRequest {

  @NotEmpty(message = "{column.notempty}")
  private String token;

  @NotEmpty(message = "{column.notempty}")
  private String mima;
}
