package com.cherri.acs_portal.config.security;

import com.cherri.acs_portal.aspect.AuditLogHandler;
import com.cherri.acs_portal.aspect.AuditLogMethodNameEnum;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class AcsPortalLogoutHandler implements LogoutHandler {

    @Override
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.LOGOUT)
    public void logout(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {
        // do nothing, this is just for AuditLogHandler Aspect to log action.
    }
}
