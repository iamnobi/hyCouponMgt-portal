package com.cherri.acs_kernel.plugin.dto.authentication.invoke;

import com.cherri.acs_kernel.plugin.dto.InvokeDTO;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class InitializeInvokeDTO extends InvokeDTO {

    @Builder
    public InitializeInvokeDTO(
            Map<String, String> systemPropertiesMap) {
        super(systemPropertiesMap);
    }
}
