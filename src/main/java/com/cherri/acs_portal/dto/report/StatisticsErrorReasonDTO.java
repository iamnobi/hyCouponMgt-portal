package com.cherri.acs_portal.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ocean.acs.models.data_object.portal.ComplexStatisticsErrorReasonDO;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticsErrorReasonDTO {

    private String issuerBankName;
    private Integer year;
    private Integer month;
    private Integer day;
    private String reason1;
    private String reasonPercentage1;
    private String reason2;
    private String reasonPercentage2;
    private String reason3;
    private String reasonPercentage3;

    public static StatisticsErrorReasonDTO newInstance(
      String issuerBankName,
      Integer year,
      Integer month,
      Integer day,
      String reason1,
      Double reasonPercentage1,
      String reason2,
      Double reasonPercentage2,
      String reason3,
      Double reasonPercentage3) {
        return new StatisticsErrorReasonDTO(
          issuerBankName,
          year,
          month,
          day,
          reason1,
          reasonPercentage1 + "%",
          reason2,
          reasonPercentage2 + "%",
          reason3,
          reasonPercentage3 + "%");
    }

    public static StatisticsErrorReasonDTO valueOf(ComplexStatisticsErrorReasonDO d) {
        return StatisticsErrorReasonDTO.builder()
          .issuerBankName(d.getIssuerBankName())
          .year(d.getYear())
          .month(d.getMonth())
          .day(d.getDay())
          .reason1(d.getReason1())
          .reasonPercentage1(d.getReasonPercentage1())
          .reason2(d.getReason2())
          .reasonPercentage2(d.getReasonPercentage2())
          .reason3(d.getReason3())
          .reasonPercentage3(d.getReasonPercentage3())
          .build();
    }
}
