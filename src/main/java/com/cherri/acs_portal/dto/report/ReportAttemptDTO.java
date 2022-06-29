package com.cherri.acs_portal.dto.report;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ocean.acs.models.data_object.portal.PortalReportAttemptDO;

@Data
@ToString
@NoArgsConstructor
@Builder
public class ReportAttemptDTO {

    private Long issuerBankId;

    private Integer year;

    private Integer month;

    private Integer dayOfMonth;

    private Integer permittedCount;

    private Integer realTriesCount;

    private Double percentage;

    public ReportAttemptDTO(
      Long issuerBankId,
      Integer year,
      Integer month,
      Integer dayOfMonth,
      Integer permittedCount,
      Integer realTriesCount,
      Double percentage) {
        this.issuerBankId = issuerBankId;
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.permittedCount = permittedCount;
        this.realTriesCount = realTriesCount;
        this.percentage = percentage;
    }

    public static ReportAttemptDTO valueOf(PortalReportAttemptDO d) {
        return ReportAttemptDTO.builder()
          .issuerBankId(d.getIssuerBankId())
          .year(d.getYear())
          .month(d.getMonth())
          .dayOfMonth(d.getDayOfMonth())
          .permittedCount(d.getPermittedCount())
          .realTriesCount(d.getRealTriesCount())
          .percentage(d.getPercentage())
          .build();
    }
}
