package com.cherri.acs_portal.dto.common;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ocean.acs.commons.enumerator.ModifyType;

@Data
@Builder
@AllArgsConstructor
public class ModifiedResult {

    private Long id;
    private ModifyType type;

}
