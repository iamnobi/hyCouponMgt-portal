package com.cherri.acs_portal.config;

import com.cherri.acs_portal.filter.DisableOptionsMethodInterceptor;
import com.cherri.acs_portal.filter.EmptyFormDataFilter;
import com.cherri.acs_portal.filter.MfaCheckInterceptor;
import com.cherri.acs_portal.filter.HttpHeaderInterceptor;
import com.cherri.acs_portal.filter.UpdateMimaCheckInterceptor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.Locale;
import java.util.TimeZone;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.ErrorPageFilter;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.i18n.TimeZoneAwareLocaleContext;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.i18n.LocaleContextResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
@PropertySource("classpath:application.properties")
@Order(3)
public class AppConfig implements WebMvcConfigurer {

    private final MfaCheckInterceptor mfaCheckInterceptor;
    private final UpdateMimaCheckInterceptor updateMimaCheckInterceptor;
    private final HttpHeaderInterceptor httpHeaderInterceptor;
    private final DisableOptionsMethodInterceptor disableOptionsMethodInterceptor;

    @Autowired
    public AppConfig(
        MfaCheckInterceptor mfaCheckInterceptor,
        UpdateMimaCheckInterceptor updateMimaCheckInterceptor,
        HttpHeaderInterceptor httpHeaderInterceptor,
        DisableOptionsMethodInterceptor disableOptionsMethodInterceptor) {
        this.mfaCheckInterceptor = mfaCheckInterceptor;
        this.updateMimaCheckInterceptor = updateMimaCheckInterceptor;
        this.httpHeaderInterceptor = httpHeaderInterceptor;
        this.disableOptionsMethodInterceptor = disableOptionsMethodInterceptor;
    }

    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.build();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        objectMapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
        return objectMapper;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource =
          new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver() {
        LocaleResolver localeResolver = new LocaleResolver() {
            @Override
            public Locale resolveLocale(HttpServletRequest request) {
                // x-display-language ex: en, zh-TW
                String language = request.getHeader("x-display-language");
                if (StringUtils.isEmpty(language)) {
                    return new Locale("en");
                }
                String[] split = language.split("-");
                if (split.length == 1) {
                    // language = en
                    return new Locale(split[0]);
                } else {
                    // language = zh-TW
                    return new Locale(split[0], split[1]);
                }
            }

            @Override
            public void setLocale(HttpServletRequest request, HttpServletResponse response,
                Locale locale) {
                LocaleContextHolder.setLocale(locale);
            }
        };
        return localeResolver;
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    @Override
    public Validator getValidator() {
        return validator();
    }

    @Bean
    public FilterRegistrationBean registerEmptyFormDataFilter(ErrorPageFilter filter) {
        FilterRegistrationBean<EmptyFormDataFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new EmptyFormDataFilter());
        registrationBean.addUrlPatterns(
          "/system-setting/card-brand/logo/*",
          "/system-setting/issuer/logo/*",
          "/system-setting/certificate/ca/*",
          "/system-setting/certificate/ssl/*");
        return registrationBean;
    }

    @Bean
    public ErrorPageFilter errorPageFilter() {
        return new ErrorPageFilter();
    }

    @Bean
    public FilterRegistrationBean disableSpringBootErrorFilter(ErrorPageFilter filter) {
        FilterRegistrationBean<ErrorPageFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(filter);
        filterRegistrationBean.setEnabled(false);
        return filterRegistrationBean;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
            .addInterceptor(mfaCheckInterceptor)
            .addPathPatterns("/auth/**")
            .excludePathPatterns("/auth/mfa/verification", "/auth/mfa/send");

        registry
            .addInterceptor(updateMimaCheckInterceptor)
            .addPathPatterns("/auth/**")
            .excludePathPatterns(
                "/auth/mfa/verification",
                "/auth/mfa/send",
                "/auth/account-management/user/mima",
                "/auth/permission/user/**",
                "/auth/permission/module/setting/get",
                "/auth/system-setting/card-brand-list",
                "/auth/policy/mima/ui/desc/*");

        registry.addInterceptor(httpHeaderInterceptor);
        registry.addInterceptor(disableOptionsMethodInterceptor);
    }
}
