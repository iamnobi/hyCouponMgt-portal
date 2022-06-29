package com.cherri.acs_portal.dto.system;

import com.cherri.acs_portal.controller.request.KeyOperationReqDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.validator.validation.HexKeyValidation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.models.data_object.entity.SecretKeyDO;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@NoArgsConstructor
public class SecretKeyDTO extends AuditableDTO {

    private String cardBrand;
    @HexKeyValidation
    private String keyA;
    @HexKeyValidation
    private String keyB;
    private AuditStatus auditStatus;
    private String operator;

    public SecretKeyDTO(KeyOperationReqDTO reqDto) {
        this.id = reqDto.getId();
        this.issuerBankId = reqDto.getIssuerBankId();
        this.cardBrand = reqDto.getCardBrand();
        this.keyA = reqDto.getKeyA();
        this.keyB = reqDto.getKeyB();
    }

    public SecretKeyDTO(
      Long id,
      Long issuerBankId,
      String cardBrand,
      String keyA,
      String keyB,
      AuditStatus auditStatus) {
        this.id = id;
        this.issuerBankId = issuerBankId;
        this.cardBrand = cardBrand;
        this.keyA = keyA;
        this.keyB = keyB;
        this.auditStatus = auditStatus;
    }

    public static SecretKeyDTO valueOf(SecretKeyDO entity) {
        return new SecretKeyDTO(
          entity.getId(),
          entity.getIssuerBankId(),
          entity.getCardBrand(),
          entity.getKeyA(),
          entity.getKeyB(),
          AuditStatus.getStatusBySymbol(entity.getAuditStatus()));
    }

    @JsonIgnore
    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.SYS_KEY;
    }
}
