package com.cherri.acs_portal.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckIdleDayRequest {

    private Long issuerBankId;
    private String account;

}
