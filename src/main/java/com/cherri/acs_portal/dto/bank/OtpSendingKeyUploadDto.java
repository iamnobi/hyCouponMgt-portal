package com.cherri.acs_portal.dto.bank;

import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.models.data_object.entity.OtpSendingSettingDO;

/**
 * Uploading JWE RSA-public-key and JWS secret-key will use this DTO together to transfer resource
 * to Service layer JWE RSA-public-key convert to byte array saving in file-content JWS secret-key
 * save in field 'jwsSecretKey'
 *
 * @author edward.wu@cherri.tech
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class OtpSendingKeyUploadDto extends AuditableDTO {

    private static final long serialVersionUID = 1L;

    /**
     * Base64 String
     */
    private String jwsSecretKey;

    private AuditStatus auditStatus;
    private String userAccount;

    @JsonIgnore
    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.BANK_OTP_KEY_UPLOAD;
    }

    public static OtpSendingKeyUploadDto valueOf(Long issuerBankId, String fileName,
      byte[] jweRsaPublicKey) {
        OtpSendingKeyUploadDto dto = new OtpSendingKeyUploadDto();
        dto.setIssuerBankId(issuerBankId);
        dto.setFileName(fileName);
        dto.setFileContent(jweRsaPublicKey);
        return dto;
    }

    public static OtpSendingKeyUploadDto valueOf(Long issuerBankId,
      String jwsSecretKeyBase64) {
        OtpSendingKeyUploadDto dto = new OtpSendingKeyUploadDto();
        dto.setIssuerBankId(issuerBankId);
        dto.setJwsSecretKey(jwsSecretKeyBase64);
        return dto;
    }

    public static OtpSendingKeyUploadDto valueOf(OtpSendingSettingDO entity) {
        OtpSendingKeyUploadDto dto = new OtpSendingKeyUploadDto();
        dto.setId(entity.getId());
        dto.setIssuerBankId(entity.getIssuerBankId());
        dto.setFileContent(entity.getJweRsaPublicKey());
        dto.setJwsSecretKey(entity.getJwsSecretKey());
        dto.setAuditStatus(AuditStatus.getStatusBySymbol(entity.getAuditStatus()));
        return dto;
    }

}
