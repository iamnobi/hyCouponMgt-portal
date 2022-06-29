package com.cherri.acs_portal.controller.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

// @DateRangeValidation(startDateFieldName = "startDate", endDateFieldName = "endDate")
@Data
@NoArgsConstructor
public class ClassicRbaReportReqDto {

    private Long issuerBankId;

    @Positive
    @NotNull(message = "{column.notempty}")
    private Integer year;

    @Positive
    @NotNull(message = "{column.notempty}")
    private Integer month;

    @Positive
    private Integer day;

    public boolean isMonthlyReport() {
        return null == day;
    }
}
