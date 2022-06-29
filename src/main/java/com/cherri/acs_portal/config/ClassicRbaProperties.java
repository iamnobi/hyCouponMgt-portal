package com.cherri.acs_portal.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "classic-rba")
@Data
@NoArgsConstructor
public class ClassicRbaProperties {

    /**
     * 累積交易金額門檻計算:上限金額 - 可設定範圍 預設：1 ~ 10,000
     */
    private CumulativeAmountInterval cumulativeAmountInterval =
      CumulativeAmountInterval.builder().min(1L).max(10000L).build();
    /**
     * 累積交易金額門檻計算:統計時間區間 - 可設定範圍 預設：5 ~ 900
     */
    private CumulativePeriodInterval cumulativePeriodInterval =
      CumulativePeriodInterval.builder().min(5L).max(900L).build();

    private EnabledModules enabledModules = new EnabledModules();

    private boolean showModuleCode = true;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CumulativeAmountInterval {

        private Long min;
        private Long max;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CumulativePeriodInterval {

        private Long min;
        private Long max;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EnabledModules {

        private boolean APT = false;
        private boolean CDC = false;
        private boolean CAC = false;
        private boolean CTF = false;
        private boolean LCC = false;
        private boolean BLC = false;
        private boolean VPN = false;
        private boolean PXY = false;
        private boolean PBC = false;
        private boolean DVC = false;
        private boolean MCC = false;
        private boolean RPR = false;
    }
}
