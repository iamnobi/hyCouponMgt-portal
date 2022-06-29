package com.cherri.acs_portal.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ocean.acs.models.data_object.entity.AbnormalTransactionDO;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AbnormalTransactionResultDTO {

    private Long id;
    private String merchantID;
    private String merchantName;
    private String uRate;
    private String nRate;

    public static AbnormalTransactionResultDTO valueOf(AbnormalTransactionDO entity) {
        return new AbnormalTransactionResultDTO(
          entity.getId(),
          entity.getMerchantId(),
          entity.getMerchantName(),
          entity.getURate() + "%",
          entity.getNRate() + "%");
    }
}
