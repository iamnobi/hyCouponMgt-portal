package ocean.acs.commons;

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
@PropertySource("classpath:acs-common.properties")
@ConfigurationProperties(prefix = "acs-common")
@ComponentScan("ocean.acs.commons")
public class AcsCommonPluginConfig {

}
