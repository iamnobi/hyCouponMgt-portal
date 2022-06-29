package com.cherri.acs_portal.dto.bank;

import com.cherri.acs_portal.dto.system.CardBrandDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import ocean.acs.commons.enumerator.CardType;

@Data
@AllArgsConstructor
@ToString
public class CardBrandTypeDto {

    private CardBrandDTO cardBrand;
    private CardType cardType;
}
