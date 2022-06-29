package com.cherri.acs_portal.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
@Order(2)
@PropertySource(value = {"classpath:application.properties"})
public class Prod41Config {

    @ConfigurationProperties(prefix = "fisc.housekeeping.prod41.datasource")
    @Bean(name = "prod41DataSource")
    public DataSource prod41DataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "prod41JdbcTempldate")
    public JdbcTemplate jdbcTemplate(
      @Qualifier("prod41DataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "prod41NamedParameterJdbcTemplate")
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(
      @Qualifier("prod41DataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

}
