package com.cherri.acs_portal.dto.acs_kernel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ocean.acs.commons.enumerator.ResultStatus;

@Data
@ToString
@NoArgsConstructor
public class HealthCheckResultDTO {

    private Integer status = ResultStatus.SERVER_ERROR.getCode();
    private String message = "UNKNOWN";
    private ResData data = new ResData(false);

    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResData {

        private Boolean databaseStatus;
    }
}
