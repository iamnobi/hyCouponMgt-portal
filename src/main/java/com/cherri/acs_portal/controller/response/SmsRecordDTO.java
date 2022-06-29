package com.cherri.acs_portal.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsRecordDTO {

    private String smsStatus;
    private Long updateMillis;
}
