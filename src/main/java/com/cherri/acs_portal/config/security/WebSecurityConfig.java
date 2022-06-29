package com.cherri.acs_portal.config.security;

import com.cherri.acs_portal.service.AcsPortalUserDetailsServiceImpl;
import com.cherri.acs_portal.service.AuditLogService;
import com.cherri.acs_portal.service.MimaPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;

import java.util.HashSet;
import java.util.Set;


@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
class WebSecurityConfig {

    private static final String LOGIN_URL = "/login";
    private static final String LOGOUT_URL = "/logout";

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new AcsPortalPasswordEncoder();
    }

    @Configuration
    @Order(1)
    public static class AcsPortalLoginWebSecurityConfigurerAdapter extends
      WebSecurityConfigurerAdapter {

        private final AcsPortalUserDetailsServiceImpl acsPortalUserDetailsServiceImpl;
        private final AcsPortalLogoutHandler acsPortalLogoutHandler;
        private final AcsPortalLogoutSuccessHandler acsPortalLogoutSuccessHandler;
        private final AcsPortalAccessDeniedHandler acsPortalAccessDeniedHandler;
        private final AuditLogService auditLogService;
        private final MimaPolicyService mimaPolicyService;

        @Autowired
        public AcsPortalLoginWebSecurityConfigurerAdapter(
            AcsPortalUserDetailsServiceImpl acsPortalUserDetailsServiceImpl,
            AcsPortalLogoutHandler acsPortalLogoutHandler,
            AcsPortalLogoutSuccessHandler acsPortalLogoutSuccessHandler,
            AcsPortalAccessDeniedHandler acsPortalAccessDeniedHandler,
            AuditLogService auditLogService,
            MimaPolicyService mimaPolicyService) {
            this.acsPortalUserDetailsServiceImpl = acsPortalUserDetailsServiceImpl;
            this.acsPortalLogoutHandler = acsPortalLogoutHandler;
            this.acsPortalLogoutSuccessHandler = acsPortalLogoutSuccessHandler;
            this.acsPortalAccessDeniedHandler = acsPortalAccessDeniedHandler;
            this.auditLogService = auditLogService;
            this.mimaPolicyService = mimaPolicyService;
        }

        @Override
        public void configure(WebSecurity web) {
            Set<String> patternSet = new HashSet<>();
            patternSet.add("/favicon.ico");
            patternSet.add("/css/**");
            patternSet.add("/fonts/**");
            patternSet.add("/img/**");
            patternSet.add("/js/**");

            String[] antPatterns = patternSet.toArray(new String[0]);
            web.ignoring().antMatchers(antPatterns);
        }

        /**
         * User Authentication implement
         * {@link com.cherri.acs_portal.service.AcsPortalUserDetailsServiceImpl}
         *
         * @param auth AuthenticationManagerBuilder
         * @throws Exception exception
         */
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(acsPortalUserDetailsServiceImpl)
              .passwordEncoder(new AcsPortalPasswordEncoder());
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            /* Request authentication */

            http.requestMatcher(request -> {
                        String requestURI = request.getRequestURI().substring(request.getContextPath().length());
                        return requestURI.startsWith("/auth/")
                                || requestURI.equals(LOGIN_URL)
                                || requestURI.equals(LOGOUT_URL);
                    })
              .authorizeRequests()
              .anyRequest().authenticated()

              /* Login */
              .and().formLogin()
              .loginProcessingUrl(LOGIN_URL)
              .usernameParameter("account")
              .passwordParameter("password")
              .successHandler(new AcsPortalAuthSuccessHandler(auditLogService, mimaPolicyService))
              .failureHandler(new AcsPortalAuthFailureHandler())

              /* Exception */
              .and().exceptionHandling()
              .authenticationEntryPoint(new AcsPortalAuthEntryPoint())
              .accessDeniedHandler(acsPortalAccessDeniedHandler)

              /* CSRF */
              .and().csrf()
              .requireCsrfProtectionMatcher(AnyRequestMatcher.INSTANCE)
              .ignoringAntMatchers(LOGIN_URL, LOGOUT_URL)

              /* Logout */
              .and().logout()
              .logoutUrl(LOGOUT_URL)
              .addLogoutHandler(acsPortalLogoutHandler)
              .logoutSuccessHandler(acsPortalLogoutSuccessHandler)
              .logoutRequestMatcher(new AntPathRequestMatcher(LOGOUT_URL));
        }
    }
}
