package com.cherri.acs_portal.manager.impl;

import com.cherri.acs_portal.annotation.LogInfo;
import com.cherri.acs_portal.manager.LdapManager;
import java.util.Hashtable;
import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class LdapManagerImpl implements LdapManager {

    private static int sendTo = 0;

    /** Localhost LDAP URL: ldap://localhost:8380/dc=springframework,dc=org */
    //    @Value("${ocean.ldap.url}")
    private String ldapUrl;

    /** Localhost LDAP people dns: ou=people,dc=springframework,dc=org */
    //    @Value("${ocean.ldap.people.dns}")
    private String peopleDns;

    private static final String LDAP_PROTOCAL = "LDAPS://";
    private static final String LDAP_CTX_FACTORY_FULL_QUALIFIED_NAME = "com.sun.jndi.ldap.LdapCtxFactory";
    private static final String SSL_SOCKET_FACTORY_FULL_QUALIFIED_NAME = "com.cherri.acs_portal.ssl.FiscLdapDummySSLSocketFactory";

    @LogInfo
    @Override
    public boolean ldapAuth(String account, String password) {
        String[] urls = ldapUrl.split(";");
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, LDAP_CTX_FACTORY_FULL_QUALIFIED_NAME);
        String url = urls[sendTo % urls.length]; // modify "ldap://"+urlAry[sendTo%ldap_count]; by jc
        env.put(Context.PROVIDER_URL, url);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");

        if(StringUtils.isNoneBlank(peopleDns)){
            env.put(Context.SECURITY_PRINCIPAL, String.format("uid=%s,%s", account, peopleDns));
        } else {
            env.put(Context.SECURITY_PRINCIPAL, String.format("uid=%s", account));
        }

        env.put(Context.SECURITY_CREDENTIALS, password);
        if (StringUtils.isNotEmpty(ldapUrl) && ldapUrl.toUpperCase().startsWith(LDAP_PROTOCAL)) {

            env.put(Context.SECURITY_PROTOCOL, "ssl");
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put("java.naming.ldap.factory.socket", SSL_SOCKET_FACTORY_FULL_QUALIFIED_NAME);
        }

        sendTo++;

        LdapContext ctx = null;
        try {
            log.info("[ldapAuth] info message=connect to FISC LDAP serer start...");
            ctx = new InitialLdapContext(env, null); // remember -Dcom.sun.jndi.ldap.object.disableEndpointIdentification=true
        } catch (AuthenticationException e) {
            log.warn("[ldapAuth] Invalid account or password", e);
            return false;
        } catch (CommunicationException e) {
            log.warn("[ldapAuth] Communication error", e);
            return false;
        } catch (Exception e) {
            log.error("[ldapAuth] unknown exception", e);
            return false;
        } finally {
            log.debug("[ldapAuth] account={}, url={}", account, url);
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (NamingException e) {
                    log.error("[ldapAuth] LdapContext close error", e);
                }
            }
        }
        return true;

    }

}
