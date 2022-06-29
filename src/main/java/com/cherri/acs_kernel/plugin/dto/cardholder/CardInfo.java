package com.cherri.acs_kernel.plugin.dto.cardholder;

import java.util.Map;
import lombok.Data;

@Data
public class CardInfo {
    private String cardNumber;
    private String cardType;        // credit, debit
    private Map<String, String> cardAttribute; // phone, email, blahblahblah
}
