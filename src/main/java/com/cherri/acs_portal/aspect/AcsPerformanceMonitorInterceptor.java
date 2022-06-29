package com.cherri.acs_portal.aspect;

import lombok.extern.log4j.Log4j2;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.springframework.aop.interceptor.AbstractMonitoringInterceptor;

@Log4j2
public class AcsPerformanceMonitorInterceptor extends AbstractMonitoringInterceptor {

  public AcsPerformanceMonitorInterceptor() {}

  public AcsPerformanceMonitorInterceptor(boolean useDynamicLogger) {
    setUseDynamicLogger(useDynamicLogger);
  }

  @Override
  protected Object invokeUnderTrace(MethodInvocation invocation, Log defaultLog) throws Throwable {
    String name = createInvocationTraceName(invocation);
    long start = System.currentTimeMillis();
    try {
      return invocation.proceed();
    } finally {
      long end = System.currentTimeMillis();
      double time = ((double) (end - start) / 1000);
      log.trace("[invokeUnderTrace] Method " + name + " execution lasted:" + time + " second.");
    }
  }
}
