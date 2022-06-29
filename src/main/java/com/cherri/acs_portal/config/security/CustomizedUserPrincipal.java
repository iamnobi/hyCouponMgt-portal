package com.cherri.acs_portal.config.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

/**
 * This is a customized user principal of UserDetail for Spring Security Established based on
 * multiple bank access
 *
 * @author edward.wu@cherri.tech
 */
public class CustomizedUserPrincipal implements UserDetails {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private Set<GrantedAuthority> authorities;
    @Getter
    private Long issuerBankId;
    @Getter
    private Set<String> roles;

    public CustomizedUserPrincipal(String username, String password, Long issuerBankId,
      Set<String> roles) {
        this.username = username;
        this.password = password;
        this.authorities = new HashSet<GrantedAuthority>(roles.size());
        for (String role : roles) {
            Assert.isTrue(!role.startsWith("ROLE_"),
              () -> role + " cannot start with ROLE_ (it is automatically added)");
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }
        this.issuerBankId = issuerBankId;
        this.roles = roles;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
