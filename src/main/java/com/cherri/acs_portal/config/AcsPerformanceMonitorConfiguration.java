package com.cherri.acs_portal.config;

import com.cherri.acs_portal.aspect.AcsPerformanceMonitorInterceptor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
@Aspect
/**
 * Enabled by changing application.properties
 * "logging.level.com.cherri.acs_portal.aspect.AcsPerformanceMonitorInterceptor" to "TRACE"
 */
public class AcsPerformanceMonitorConfiguration {

    // Monitor all acs_portal
    @Pointcut("within(com.cherri.acs_portal.*.*)")
    // Monitor multiple packages of acs_portal
    //  @Pointcut("within(com.cherri.acs_portal.controller.*) ||
    // within(com.cherri.acs_portal.service.*)")
    public void monitor() {
    }

    @Bean
    public AcsPerformanceMonitorInterceptor performanceMonitorInterceptor() {
        return new AcsPerformanceMonitorInterceptor(false);
    }

    @Bean
    public Advisor performanceMonitorAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(
          "com.cherri.acs_portal.config.AcsPerformanceMonitorConfiguration.monitor()");
        return new DefaultPointcutAdvisor(pointcut, performanceMonitorInterceptor());
    }
}
