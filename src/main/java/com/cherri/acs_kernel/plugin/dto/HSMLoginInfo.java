package com.cherri.acs_kernel.plugin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HSMLoginInfo {
    private long slotId;
    private String password;
}
