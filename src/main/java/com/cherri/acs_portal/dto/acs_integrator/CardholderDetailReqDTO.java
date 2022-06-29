package com.cherri.acs_portal.dto.acs_integrator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ocean.acs.commons.enumerator.CardType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CardholderDetailReqDTO {

    private String cardNumber;
    private String bankCode;
    private CardType cardType;
    private String identityNumber;
}
