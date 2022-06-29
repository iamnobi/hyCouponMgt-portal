package com.cherri.acs_kernel.plugin.dto.cardholder.invoke;

import com.cherri.acs_kernel.plugin.dto.InvokeDTO;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CardholderInitializeInvokeDTO extends InvokeDTO {

    @Builder
    public CardholderInitializeInvokeDTO(Map<String, String> systemPropertiesMap) {
        super(systemPropertiesMap);
    }
}
