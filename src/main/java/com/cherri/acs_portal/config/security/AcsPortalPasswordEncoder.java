package com.cherri.acs_portal.config.security;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

@Log4j2
public class AcsPortalPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (StringUtils.isAnyBlank(rawPassword, encodedPassword)) {
            return false;
        }

        try {
            return rawPassword.toString().equals(encodedPassword);
        } catch (Exception e) {
            log.error("[matches] unknown exception", e);
        }
        return false;
    }

}
