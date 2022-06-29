package com.cherri.acs_portal.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckMimaIsExpiredRequest {

    private Long issuerBankId;
    private String account;

}
