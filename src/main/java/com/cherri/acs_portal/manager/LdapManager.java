package com.cherri.acs_portal.manager;

public interface LdapManager {
    
    /** This method is provided by FISC's com.hitrust.visa3d.ias.system.DoLogin.java */
    boolean ldapAuth(String account, String password);

}
