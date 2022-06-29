package com.cherri.acs_portal.config;

import com.cherri.acs_kernel.plugin.dto.hsm.invoke.HSMDecryptInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.result.HSMDecryptResultDTO;
import com.cherri.acs_kernel.plugin.enumerator.HSMEncryptDecryptMechanism;
import com.cherri.acs_kernel.plugin.hsm.impl.HSMImplBase;
import com.zaxxer.hikari.HikariDataSource;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Log4j2
@Configuration
@Order(1)
@EntityScan("ocean.acs.models.${spring.jpa.database}.entity")
@ComponentScan(basePackages = {"ocean.acs.models.${spring.jpa.database}.dao.impl"})
@EnableJpaRepositories(basePackages = "ocean.acs.models.${spring.jpa.database}.repository")
@PropertySource(value = {"classpath:application.properties"})
public class DbConfig {

    @Lazy
    @Autowired
    private HSMImplBase hsmImplBase;
    @Autowired
    private Environment environment;
    private int maxRows;

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Autowired
    public DbConfig(@Value("${pagination.max.rows:1000}") Integer maxRows) {
        this.maxRows = maxRows;
    }

    @Primary
    @Bean(name = "dsDataSource")
    public DataSource dsDataSource() {
        HikariDataSource dataSource = (HikariDataSource) DataSourceBuilder.create().build();
        String driverClassName = environment.getProperty("spring.datasource.driver-class-name");
        String jdbcUrl = environment.getProperty("spring.datasource.url");
        String username = environment.getProperty("spring.datasource.username");

        boolean enableProtectDbPassword = Boolean.TRUE.equals(environment.getProperty("hsm.enable-protect-db-password", Boolean.class));
        log.info("enableProtectDbPassword: {}", enableProtectDbPassword);
        if (enableProtectDbPassword) {
            String encryptedMima = environment.getProperty("spring.datasource.password");
            HSMDecryptResultDTO result = decryptDatabaseMima(encryptedMima);
            dataSource.setPassword(new String(result.getPlainText(), StandardCharsets.UTF_8));
        } else {
            String mima = environment.getProperty("spring.datasource.password");
            dataSource.setPassword(mima);
        }
        dataSource.setDriverClassName(driverClassName);
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUsername(username);
        return dataSource;
    }

    private HSMDecryptResultDTO decryptDatabaseMima(String encryptedMima) {
        if (encryptedMima == null) {
            throw new RuntimeException("Database password not found in application.properties.");
        }

        String dbKeyId = environment.getProperty("hsm.db.key.id");
        log.info("hsm.db.key.id: {}", dbKeyId);

        log.info("Database password decrypt start.");
        HSMDecryptResultDTO result = hsmImplBase.decrypt(HSMDecryptInvokeDTO.builder()
            .hsmMechanism(HSMEncryptDecryptMechanism.CKM_AES_CBC)
            .cipherText(Base64.getDecoder().decode(encryptedMima))
            .keyId(dbKeyId).build()
        );
        if (result.isExceptionHappened()) {
            log.error("{}", result.getException().getMessage(), result.getException());
            throw new RuntimeException(result.getException().getMessage());
        }
        log.info("Database password decrypt end.");
        return result;
    }

    @Primary
    @Bean(name = "acsPortalJdbcTemplate")
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setMaxRows(maxRows);
        return jdbcTemplate;
    }

    @Primary
    @Bean(name = "acsPortalNamedParameterJdbcTemplate")
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate =
          new NamedParameterJdbcTemplate(dataSource);
        ((JdbcTemplate) namedParameterJdbcTemplate.getJdbcOperations()).setMaxRows(maxRows);
        return namedParameterJdbcTemplate;
    }

    @Primary
    @Bean
    public PlatformTransactionManager transactionManager(
      DataSource dataSource, EntityManagerFactory entityManager) {
        JpaTransactionManager jpaTxManager = new JpaTransactionManager();
        jpaTxManager.setDataSource(dataSource);
        jpaTxManager.setEntityManagerFactory(entityManager);
        return jpaTxManager;
    }
}
