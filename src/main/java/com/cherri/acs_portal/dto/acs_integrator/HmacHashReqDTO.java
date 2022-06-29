package com.cherri.acs_portal.dto.acs_integrator;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HmacHashReqDTO {

    /** HMAC hash key */
    private String key;
    /** 雜湊前的原始字串 */
    private String origin;

}
