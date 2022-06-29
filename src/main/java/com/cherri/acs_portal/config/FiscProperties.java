package com.cherri.acs_portal.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "fisc")
@Data
@NoArgsConstructor
public class FiscProperties {

    private BankHandlingFee bankHandlingFee;
    private Housekeeping houseKeeping;

    @Data
    @NoArgsConstructor
    public static class BankHandlingFee {

        private String exportDirPath;
        private String uploadCmd;
    }

    @Data
    @NoArgsConstructor
    public static class Housekeeping {

        private String fileUploadPath;
        private Integer backupDays;
        private Integer cleanDays;
        private Prod41 prod41;

        @Data
        @NoArgsConstructor
        public static class Prod41 {

            private Datasource datasource;

            @Data
            @NoArgsConstructor
            public static class Datasource {

                private String jdbcUrl;
                private String username;
                private String password;
            }
        }
    }

}
