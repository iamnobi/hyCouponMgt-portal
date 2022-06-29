package com.cherri.acs_portal;

import javax.servlet.MultipartConfigElement;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.DispatcherServlet;

@ComponentScan(
  basePackages = {"com.cherri.acs_kernel.plugin", "com.cherri.acs_portal"}
)
@SpringBootApplication
public class AcsPortalApplication extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(AcsPortalApplication.class);
  }

  @Bean
  public DispatcherServletRegistrationBean dispatcherServletRegistration(
      DispatcherServlet dispatcherServlet, ObjectProvider<MultipartConfigElement> multipartConfig) {
    DispatcherServletRegistrationBean registration =
        new DispatcherServletRegistrationBean(dispatcherServlet, "/*");
    registration.setName("dispatcherServlet");
    registration.setLoadOnStartup(-1);
    multipartConfig.ifAvailable(registration::setMultipartConfig);
    return registration;
  }

  public static void main(String[] args) {
    SpringApplication.run(AcsPortalApplication.class, args);
  }
}
