package com.cherri.acs_portal.dto.rba;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassicRbaStatus {

    private String name;
    private String frictionless;
    private String challenge;
    private String reject;

}
