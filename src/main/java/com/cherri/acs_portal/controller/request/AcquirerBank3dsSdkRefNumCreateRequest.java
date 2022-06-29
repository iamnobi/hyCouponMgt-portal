package com.cherri.acs_portal.controller.request;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AcquirerBank3dsSdkRefNumCreateRequest
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AcquirerBank3dsSdkRefNumCreateRequest {
    @NotNull
    private String sdkRefNumber;
}
