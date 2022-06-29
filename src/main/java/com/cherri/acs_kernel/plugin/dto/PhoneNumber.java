package com.cherri.acs_kernel.plugin.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PhoneNumber {
    private PhoneNumberPrefix phoneNumberPrefix;
    private String number;

    public String getPhoneNumber() {
        return this.phoneNumberPrefix.getCountryPhoneNumberPreffix() + this.number;
    }
}
