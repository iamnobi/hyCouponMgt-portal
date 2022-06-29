package com.cherri.acs_portal.dto.report;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsTransactionStatusReportDTO {

  @Data
  public static class CardBrandReport {

    private String cardBrand;
    private String uRateKpi = "N/A";
    private String uRate = "N/A";
    private String nRateKpi = "N/A";
    private String nRate = "N/A";
    private String aresYCount = "0";
    private String aresNCount = "0";
    private String aresUCount = "0";
    private String aresACount = "0";
    private String aresCCount = "0";
    private String aresRCount = "0";
    private String rreqYCount = "0";
    private String rreqNCount = "0";
    private String rreqUCount = "0";
    private String rreqACount = "0";
    private String rreqRCount = "0";

    public CardBrandReport(String cardBrand) {
      this.cardBrand = cardBrand;
    }
  }

  // 交易狀態統計報表相關欄位_start
  private Long issuerBankId;
  private String issuerBankName;
  private String year;
  private String month;
  private String day;
  /** 總交易量 */
  private String total = "0";
  /** OTP動態密碼筆數 */
  private String otpCount = "0";
  /** 身份驗證失敗 */
  private String n = "0";
  /** 尚未經過身份驗證 */
  private String a = "0";
  /** 身份驗證成功 */
  private String y = "0";
  /** 挑戰 -> 身份驗證成功 */
  private String cy = "0";
  /** 挑戰 -> 身份驗證失敗 */
  private String cn = "0";
  /** 交易拒絕 */
  private String r = "0";
  /** 系統異常 */
  private String u = "0";
  // 交易狀態統計報表相關欄位_end
  private List<CardBrandReport> cardBrandReportList = new ArrayList<>();

  public static StatisticsTransactionStatusReportDTO newInstance(
      StatisticsTransactionStatusDTO txStatus,
      List<StatisticsTransactionStatusDetailDTO> txStatusDetailList) {
    StatisticsTransactionStatusReportDTO report = new StatisticsTransactionStatusReportDTO();
    appendTransactionStatusData(report, txStatus);
    appendTransactionStatusDetailData(
        report, txStatusDetailList);
    return report;
  }

  public static StatisticsTransactionStatusReportDTO appendTransactionStatusData(
      StatisticsTransactionStatusReportDTO reportDto, StatisticsTransactionStatusDTO dto) {
    reportDto.setIssuerBankId(dto.getIssuerBankId());
    reportDto.setIssuerBankName(dto.getIssuerBankName());
    reportDto.setYear(String.valueOf(dto.getYear()));
    reportDto.setMonth(String.valueOf(dto.getMonth()));
    String dayText = dto.getDay() == null ? "" : String.valueOf(dto.getDay());
    reportDto.setDay(dayText);
    reportDto.setTotal(dto.getTotal());
    reportDto.setOtpCount(dto.getOtpCount());
    reportDto.setN(dto.getN());
    reportDto.setA(dto.getA());
    reportDto.setY(dto.getY());
    reportDto.setCy(dto.getCy());
    reportDto.setCn(dto.getCn());
    reportDto.setR(dto.getR());
    reportDto.setU(dto.getU());
    return reportDto;
  }

  public static StatisticsTransactionStatusReportDTO appendTransactionStatusDetailData(
      StatisticsTransactionStatusReportDTO reportDto,
      List<StatisticsTransactionStatusDetailDTO> dtoList) {
    for (StatisticsTransactionStatusDetailDTO dto : dtoList) {
      CardBrandReport cardBrandReport = new CardBrandReport(dto.getCardBrand());
      cardBrandReport.setURate(dto.getURate());
      cardBrandReport.setURateKpi(dto.getURateKpi());
      cardBrandReport.setNRate(dto.getNRate());
      cardBrandReport.setNRateKpi(dto.getNRateKpi());
      cardBrandReport.setAresYCount(dto.getAresYCount());
      cardBrandReport.setAresNCount(dto.getAresNCount());
      cardBrandReport.setAresUCount(dto.getAresUCount());
      cardBrandReport.setAresACount(dto.getAresACount());
      cardBrandReport.setAresCCount(dto.getAresCCount());
      cardBrandReport.setAresRCount(dto.getAresRCount());
      cardBrandReport.setRreqYCount(dto.getRreqYCount());
      cardBrandReport.setRreqNCount(dto.getRreqNCount());
      cardBrandReport.setRreqUCount(dto.getRreqUCount());
      cardBrandReport.setRreqACount(dto.getRreqACount());
      cardBrandReport.setRreqRCount(dto.getRreqRCount());
      reportDto.getCardBrandReportList().add(cardBrandReport);
    }
    return reportDto;
  }
}
