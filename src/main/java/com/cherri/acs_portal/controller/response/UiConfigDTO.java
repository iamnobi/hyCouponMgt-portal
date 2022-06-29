package com.cherri.acs_portal.controller.response;

import com.cherri.acs_portal.config.ClassicRbaProperties;
import com.cherri.acs_portal.config.ClassicRbaProperties.EnabledModules;
import com.cherri.acs_portal.dto.system.CavvImportMode;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class UiConfigDTO {

    private Boolean isMultipleBank;
    private Boolean isLdapMode;
    private Boolean isMfa;
    private String mfaSystem;
    private RbaSettings rbaSettings;
    private CavvImportMode cavvImportMode;
    private boolean attemptSettingSelectCurrency;
    private String systemCurrency;
    private int systemCurrencyCode;
    private String cavvEncryptPublicKey;
    private String recaptchaSiteKey;

    // Lombok builder override
    public static class UiConfigDTOBuilder {

        public UiConfigDTOBuilder rbaSettings(ClassicRbaProperties classicRbaProperties) {
            this.rbaSettings =
              RbaSettings.builder()
                .showModuleCode(classicRbaProperties.isShowModuleCode())
                .enabledModules(EnabledRbaModules.valueOf(classicRbaProperties.getEnabledModules()))
                .build();
            return this;
        }
    }

    @Data
    @Builder
    public static class RbaSettings {

        private boolean showModuleCode;
        @JsonProperty("showModules")
        private EnabledRbaModules enabledModules;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EnabledRbaModules {

        @JsonProperty("APT")
        private boolean apt;

        @JsonProperty("CDC")
        private boolean cdc;

        @JsonProperty("CAC")
        private boolean cac;

        @JsonProperty("CTF")
        private boolean ctf;

        @JsonProperty("LCC")
        private boolean lcc;

        @JsonProperty("BLC")
        private boolean blc;

        @JsonProperty("VPN")
        private boolean vpn;

        @JsonProperty("PXY")
        private boolean pxy;

        @JsonProperty("PBC")
        private boolean pbc;

        @JsonProperty("DVC")
        private boolean dvc;

        @JsonProperty("MCC")
        private boolean mcc;

        @JsonProperty("RPR")
        private boolean rpr;

        public static EnabledRbaModules valueOf(EnabledModules v) {
            return EnabledRbaModules.builder()
              .apt(v.isAPT())
              .cdc(v.isCDC())
              .cac(v.isCAC())
              .ctf(v.isCTF())
              .lcc(v.isLCC())
              .blc(v.isBLC())
              .vpn(v.isVPN())
              .pxy(v.isPXY())
              .pbc(v.isPBC())
              .dvc(v.isDVC())
              .mcc(v.isMCC())
              .rpr(v.isRPR())
              .build();
        }
    }
}
