package com.cherri.acs_portal.dto.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.text.DecimalFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SimpleStatisticsTransactionStatusDTO {
  private Long issuerBankId;
  private String issuerBankName;
  private String year;
  private String month;
  private String day;
  /** 總交易量 */
  private String total = "0";
  /** 身份驗證失敗 */
  private String n = "0";
  /** 尚未經過身份驗證 */
  private String a = "0";
  /** 身份驗證成功 */
  private String y = "0";
  /** 交易拒絕 */
  private String r = "0";
  /** 系統異常 */
  private String u = "0";
  @JsonProperty("urate")
  private String uRate = "0";
  @JsonProperty("nrate")
  private String nRate = "0";

  public static SimpleStatisticsTransactionStatusDTO valueOf(ReportTxStatisticsDTO dto) {
    String nRateStr = "0.0";
    String uRateStr = "0.0";

    if (dto.getTotal() != 0) {
      DecimalFormat decimalFormat = new DecimalFormat("#.###");
      final Double nRate =
          ((double) (dto.getNCount() + dto.getCnCount() + dto.getRCount()) / dto.getTotal()) * 100;

      final Double uRate =
          ((double) (dto.getUCount()) / dto.getTotal()) * 100;

      nRateStr = decimalFormat.format(nRate);
      uRateStr = decimalFormat.format(uRate);
    }

    return new SimpleStatisticsTransactionStatusDTO(
        dto.getIssuerBankId(),
        null,
        String.valueOf(dto.getYear()),
        String.valueOf(dto.getMonth()),
        String.valueOf(dto.getDayOfMonth()),
        String.valueOf(dto.getTotal()),
        String.valueOf(dto.getNCount() + dto.getCnCount()),
        String.valueOf(dto.getACount()),
        String.valueOf(dto.getYCount() + dto.getCyCount()),
        String.valueOf(dto.getRCount()),
        String.valueOf(dto.getUCount()),
        uRateStr.concat("%"),
        nRateStr.concat("%"));
  }
}
