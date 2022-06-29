package com.cherri.acs_portal.controller.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.models.data_object.entity.OtpSendingSettingDO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpSendingSettingResDTO {

    private Boolean orgEnable;
    private Boolean bankEnable;
    private BankResDTO bank;
    private AuditStatus auditStatus;

    @Data
    public class BankResDTO {

        private String url = "";
        private boolean rsaPublicKeyExist;
        private boolean secretKeyExist;

        public BankResDTO(String url, byte[] jweRsaPublicKey, String jwsSecretKey) {
            this.url = null != url ? url : "";
            this.rsaPublicKeyExist = (null != jweRsaPublicKey);
            this.secretKeyExist = (null != jwsSecretKey && jwsSecretKey.length() > 0);
        }
    }

    public static OtpSendingSettingResDTO valueOf(OtpSendingSettingDO entity) {
        OtpSendingSettingResDTO response = new OtpSendingSettingResDTO();
        response.setOrgEnable(entity.getOrgEnable());
        response.setBankEnable(entity.getBankEnable());
        response.setBank(response.new BankResDTO(entity.getBankUrl(), entity.getJweRsaPublicKey(),
          entity.getJwsSecretKey()));
        response.setAuditStatus(AuditStatus.getStatusBySymbol(entity.getAuditStatus()));
        return response;
    }

}
