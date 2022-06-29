package com.cherri.acs_portal.dto.bank;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TradingChannel {

    private String cardBrand;
    private boolean enabled;
}
