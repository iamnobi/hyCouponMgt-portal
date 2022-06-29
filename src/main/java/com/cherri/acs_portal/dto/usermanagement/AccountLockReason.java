package com.cherri.acs_portal.dto.usermanagement;

import com.cherri.acs_portal.constant.MessageConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountLockReason {
  EXCEED_MAX_LOGIN_ATTEMPT(MessageConstants.EXCEED_MAX_LOGIN_ATTEMPT),
  EXCEED_MAX_FORGET_MIMA(MessageConstants.EXCEED_MAX_FORGET_MIMA),
  EXCEED_MAX_UNUSED_PERIOD(MessageConstants.EXCEED_MAX_UNUSED_PERIOD),
  EXCEED_MAX_SEND_MFA_TIMES(MessageConstants.EXCEED_MAX_SEND_MFA_TIMES),
  EXCEED_MAX_VERIFY_MFA_ATTEMPT(MessageConstants.EXCEED_MAX_VERIFY_MFA_ATTEMPT);

  private final String i18n;

}

