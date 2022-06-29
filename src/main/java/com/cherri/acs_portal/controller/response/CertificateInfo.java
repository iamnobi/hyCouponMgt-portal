package com.cherri.acs_portal.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CertificateInfo {
  private long id;
  private boolean current = false;
  private long createMillis;
  private long certUploadedMillis;
  private long certExpireMillis;
}
