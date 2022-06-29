package com.cherri.acs_portal.controller;

import com.cherri.acs_portal.constant.SessionAttributes;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Component
public class ContextProvider {

  @Autowired
  private HttpSession httpSession;

  public String getUserAccount() {
    return (String) httpSession.getAttribute(SessionAttributes.ACCOUNT);
  }

  HttpSession getHttpSession() {
    return httpSession;
  }

  public Long getIssuerBankId(AuditableDTO dto) {
    return Optional.ofNullable(dto.getIssuerBankId()).orElse(this.getIssuerBankId());
  }

  public Long getIssuerBankId() {
    return (Long) getHttpSession().getAttribute(SessionAttributes.ISSUER_BANK_ID);
  }

  public boolean canSeePan() {
    return (boolean) getHttpSession().getAttribute(SessionAttributes.CAN_SEE_PAN);
  }

  protected boolean isSystemAdmin() {
    return (boolean) httpSession.getAttribute(SessionAttributes.SYSTEM_ADMIN);
  }
}
