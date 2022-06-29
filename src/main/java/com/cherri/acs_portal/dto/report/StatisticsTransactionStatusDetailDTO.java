package com.cherri.acs_portal.dto.report;

import com.cherri.acs_portal.dto.report.CardBrandTransactionRateKpiDTO.RateType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import ocean.acs.models.data_object.entity.ReportTxStatisticsDetailDO;

@Log4j2
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsTransactionStatusDetailDTO {

    private String cardBrand;
    private String uRate;
    private String nRate;
    private String uRateKpi;
    private String nRateKpi;

    private String aresYCount;
    private String aresNCount;
    private String aresUCount;
    private String aresACount;
    private String aresCCount;
    private String aresRCount;

    private String RreqYCount;
    private String RreqNCount;
    private String RreqUCount;
    private String RreqACount;
    private String RreqRCount;

    public static StatisticsTransactionStatusDetailDTO valueOf(ReportTxStatisticsDetailDO entity,
      CardBrandTransactionRateKpiDTO cardBrandTransactionRateKpiDTO) {
        Integer uRateKpi = cardBrandTransactionRateKpiDTO
          .getKPiByCardBrandAndRateType(entity.getCardBrand(), RateType.U_RATE);
        Integer nRateKpi = cardBrandTransactionRateKpiDTO
          .getKPiByCardBrandAndRateType(entity.getCardBrand(), RateType.N_RATE);
        if (uRateKpi == null) {
            uRateKpi = 0;
        }
        if (nRateKpi == null) {
            nRateKpi = 0;
        }
        String uRateKpiText = uRateKpi + "%";
        String nRateKpiText = nRateKpi + "%";
        log.info(
          "[StatisticsTransactionStatusDetail] The cardBrand:{} N-Rate-KPI:{}, U-Rate-KPI:{}",
          entity.getCardBrand(),
          nRateKpiText,
          uRateKpiText);
        return new StatisticsTransactionStatusDetailDTO(
          entity.getCardBrand(),
          entity.getURate() + "%",
          entity.getNRate() + "%",
          uRateKpiText,
          nRateKpiText,
          //
          String.valueOf(entity.getAresYCount()),
          String.valueOf(entity.getAresNCount()),
          String.valueOf(entity.getAresUCount()),
          String.valueOf(entity.getAresACount()),
          String.valueOf(entity.getAresCCount()),
          String.valueOf(entity.getAresRCount()),
          //
          String.valueOf(entity.getRreqYCount()),
          String.valueOf(entity.getRreqNCount()),
          String.valueOf(entity.getRreqUCount()),
          String.valueOf(entity.getRreqACount()),
          String.valueOf(entity.getRreqRCount()));
    }
}
