package com.cherri.acs_portal.dto.bank;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class IssuerTradingChannelUpdateResultDto {
    private int status;
    private String message;
}