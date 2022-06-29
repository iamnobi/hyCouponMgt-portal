package com.cherri.acs_portal.config;

import com.cherri.acs_portal.job.OceanJobFactory;
import java.io.IOException;
import java.util.Properties;
import javax.sql.DataSource;
import lombok.extern.log4j.Log4j2;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Log4j2
@Configuration
public class QuartzConfig {

    private final DataSource dataSource;

    private final OceanJobFactory oceanJobFactory;
    private final String databaseType;

    @Autowired
    public QuartzConfig(
      DataSource dataSource,
      OceanJobFactory oceanJobFactory,
      @Value("${spring.jpa.database}") String databaseType) {
        this.dataSource = dataSource;
        this.oceanJobFactory = oceanJobFactory;
        this.databaseType = databaseType;
    }

    @Bean
    public Scheduler scheduler(@Value("${scheduler.enabled}") Boolean schedulerEnabled)
      throws Exception {
        Scheduler scheduler = schedulerFactoryBean().getScheduler();
        scheduler.start();
        if (schedulerEnabled) {
            scheduler.resumeAll();
        } else {
            scheduler.pauseAll();
        }
        return scheduler;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setSchedulerName("Acs_Portal_Scheduler");
        factory.setDataSource(dataSource);
        factory.setOverwriteExistingJobs(true);
        factory.setApplicationContextSchedulerContextKey("applicationContext");
        factory.setQuartzProperties(quartzProperties());
        factory.setJobFactory(oceanJobFactory);
        return factory;
    }

    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        Properties properties = propertiesFactoryBean.getObject();
        if ("sql_server".equals(databaseType)) {
            log.info(
              "[quartzProperties] databaseType = sql_server. set "
                + "org.quartz.jobStore.driverDelegateClass=org.quartz.impl.jdbcjobstore.MSSQLDelegate");
            properties.setProperty(
              "org.quartz.jobStore.driverDelegateClass",
              "org.quartz.impl.jdbcjobstore.MSSQLDelegate");
        }
        return properties;
    }
}
