package com.cherri.acs_portal.manager;

import com.cherri.acs_portal.constant.SessionAttributes;
import java.util.Set;
import javax.servlet.http.HttpSession;
import ocean.acs.commons.enumerator.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionManager {
  @Autowired private HttpSession session;

  @SuppressWarnings("unchecked")
  public boolean hasPermission(Permission targetPermission) {
    Set<String> permissionSet = (Set<String>) session.getAttribute(SessionAttributes.PERMISSION);
    return permissionSet != null && permissionSet.contains(targetPermission.toString());
  }
}
