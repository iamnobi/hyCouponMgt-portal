package com.cherri.acs_portal.controller.response;

import lombok.Builder;
import lombok.Data;
import ocean.acs.models.data_object.entity.BankDataKeyDO;

@Data
@Builder
public class BankDataKeyResDTO {

    private String rsaPrivateKeyId;
    private String rsaPublicKey;

    public static BankDataKeyResDTO valueOf(BankDataKeyDO bankDataKey) {
        return BankDataKeyResDTO.builder()
          .rsaPrivateKeyId(bankDataKey.getRsaPrivateKyId())
          .rsaPublicKey(bankDataKey.getRsaPublicKy())
          .build();
    }
}
