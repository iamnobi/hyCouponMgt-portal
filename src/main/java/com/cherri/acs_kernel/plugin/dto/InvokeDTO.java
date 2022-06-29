package com.cherri.acs_kernel.plugin.dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvokeDTO {

    private Map<String, String> systemPropertiesMap;
}
