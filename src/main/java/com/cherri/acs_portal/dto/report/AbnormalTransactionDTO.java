package com.cherri.acs_portal.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class AbnormalTransactionDTO {

    private Key key;
    private String merchantName;
    private Long count;

    @Data
    @AllArgsConstructor
    public static class Key {

        private Long issuerBankId;
        private String merchantId;
    }

}
