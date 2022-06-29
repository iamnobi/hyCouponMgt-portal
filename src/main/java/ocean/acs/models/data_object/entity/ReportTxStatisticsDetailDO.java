package ocean.acs.models.data_object.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ReportTxStatisticsDetailDO {

    private Long id;
    private Long issuerBankId;
    private Integer year;
    private Integer month;
    private Integer dayOfMonth;
    private String cardBrand;
    private Double uRate;
    private Double nRate;
    private Long aresYCount;
    private Long aresNCount;
    private Long aresUCount;
    private Long aresACount;
    private Long aresCCount;
    private Long aresRCount;
    private Long rreqYCount;
    private Long rreqNCount;
    private Long rreqUCount;
    private Long rreqACount;
    private Long rreqRCount;
    private String sysCreator;
    private Long dataMillis;
    @Builder.Default
    private Long createMillis = System.currentTimeMillis();
    private String sysUpdater;
    private Long updateMillis;

    public ReportTxStatisticsDetailDO(
      String cardBrand,
      Double uRate,
      Double nRate,
      Long aresYCount,
      Long aresNCount,
      Long aresUCount,
      Long aresACount,
      Long aresCCount,
      Long aresRCount,
      Long rreqYCount,
      Long rreqNCount,
      Long rreqUCount,
      Long rreqACount,
      Long rreqRCount) {
        this.cardBrand = cardBrand;
        this.uRate = uRate;
        this.nRate = nRate;
        this.aresYCount = aresYCount;
        this.aresNCount = aresNCount;
        this.aresUCount = aresUCount;
        this.aresACount = aresACount;
        this.aresCCount = aresCCount;
        this.aresRCount = aresRCount;
        this.rreqYCount = rreqYCount;
        this.rreqNCount = rreqNCount;
        this.rreqUCount = rreqUCount;
        this.rreqACount = rreqACount;
        this.rreqRCount = rreqRCount;
    }

    public static ReportTxStatisticsDetailDO valueOf(
      ocean.acs.models.oracle.entity.ReportTxStatisticsDetail e) {
        return new ReportTxStatisticsDetailDO(
          e.getId(),
          e.getIssuerBankId(),
          e.getYear(),
          e.getMonth(),
          e.getDayOfMonth(),
          e.getCardBrandName(),
          e.getURate(),
          e.getNRate(),
          e.getAresYCount(),
          e.getAresNCount(),
          e.getAresUCount(),
          e.getAresACount(),
          e.getAresCCount(),
          e.getAresRCount(),
          e.getRreqYCount(),
          e.getRreqNCount(),
          e.getRreqUCount(),
          e.getRreqACount(),
          e.getRreqRCount(),
          e.getSysCreator(),
          e.getDataMillis(),
          e.getCreateMillis(),
          e.getSysUpdater(),
          e.getUpdateMillis());
    }

    public static ReportTxStatisticsDetailDO valueOf(
      ocean.acs.models.sql_server.entity.ReportTxStatisticsDetail e) {
        return new ReportTxStatisticsDetailDO(
          e.getId(),
          e.getIssuerBankId(),
          e.getYear(),
          e.getMonth(),
          e.getDayOfMonth(),
          e.getCardBrand(),
          e.getURate(),
          e.getNRate(),
          e.getAresYCount(),
          e.getAresNCount(),
          e.getAresUCount(),
          e.getAresACount(),
          e.getAresCCount(),
          e.getAresRCount(),
          e.getRreqYCount(),
          e.getRreqNCount(),
          e.getRreqUCount(),
          e.getRreqACount(),
          e.getRreqRCount(),
          e.getSysCreator(),
          e.getDataMillis(),
          e.getCreateMillis(),
          e.getSysUpdater(),
          e.getUpdateMillis());
    }

    public static ReportTxStatisticsDetailDO newInstance(
      Long issuerBankId,
      String cardBrand,
      int year,
      int monthValue,
      int dayOfMonth,
      long startMillis,
      long nowMillis,
      String operator) {
        return ReportTxStatisticsDetailDO.builder()
          .issuerBankId(issuerBankId)
          .cardBrand(cardBrand)
          .year(year)
          .month(monthValue)
          .dayOfMonth(dayOfMonth)
          .dataMillis(startMillis)
          .sysCreator(operator)
          .createMillis(nowMillis)
          .build();
    }
}
