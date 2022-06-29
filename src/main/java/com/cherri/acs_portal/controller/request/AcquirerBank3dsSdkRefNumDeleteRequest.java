package com.cherri.acs_portal.controller.request;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AcquirerBank3dsSdkRefNumDeleteRequest
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AcquirerBank3dsSdkRefNumDeleteRequest {
    @NotNull
    private Long id;
}
