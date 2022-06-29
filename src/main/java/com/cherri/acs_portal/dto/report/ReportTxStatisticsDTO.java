package com.cherri.acs_portal.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ocean.acs.models.data_object.portal.PortalReportTxStatisticsDO;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportTxStatisticsDTO {

    private Long issuerBankId;
    private Integer year;
    private Integer month;
    private Integer dayOfMonth;
    private Long total;
    private Long otpCount;
    private Integer nCount;
    private Integer aCount;
    private Integer yCount;
    private Integer cyCount;
    private Integer cnCount;
    private Integer rCount;
    private Integer uCount;

    public static ReportTxStatisticsDTO valueOf(PortalReportTxStatisticsDO d) {
        return ReportTxStatisticsDTO.builder()
          .issuerBankId(d.getIssuerBankId())
          .year(d.getYear())
          .month(d.getMonth())
          .dayOfMonth(d.getDayOfMonth())
          .total(d.getTotal())
          .otpCount(d.getOtpCount())
          .nCount(d.getNCount())
          .aCount(d.getACount())
          .yCount(d.getYCount())
          .cyCount(d.getCyCount())
          .cnCount(d.getCnCount())
          .rCount(d.getRCount())
          .uCount(d.getUCount())
          .build();
    }
}
