package com.cherri.acs_kernel.plugin.hsm.impl.config;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.annotation.Validated;

@Validated
@Log4j2
@Data
@Configuration
@PropertySource("classpath:plugin-hsm-aws-cloud-hsm.properties")
@ConfigurationProperties(prefix = "cloud-hsm-plugin")
@ComponentScan("com.cherri.acs_kernel.plugin.hsm.impl")
public class CloudHsmPluginConfig {

  /** The region of AWS Secret Manager */
  @NotEmpty private String awsSecretManagerRegion;

  /** The secret name of HSM credential in AWS Secret Manager */
  @NotEmpty private String hsmCredentialSecretName;

  /** HSM登入失敗重試的時間 */
  private long hsmLoginRetryTimeoutMillis = 1000L;

  /**
   * Name of this plugin which will shows in ACS Portal
   */
  private String pluginName = "AWS CloudHSM";

  @PostConstruct
  public void logConfigs() {
    log.info("{}", this);
  }
}
