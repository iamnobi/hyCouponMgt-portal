package com.cherri.acs_portal.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsTransactionStatusDTO {
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

  public StatisticsTransactionStatusDTO(
      Long issuerBankId,
      String issuerBankName,
      String total,
      String otpCount,
      String n,
      String a,
      String y,
      String cy,
      String cn,
      String r,
      String u) {
    this.issuerBankId = issuerBankId;
    this.issuerBankName = issuerBankName;
    this.total = total;
    this.otpCount = otpCount;
    this.n = n;
    this.a = a;
    this.y = y;
    this.cy = cy;
    this.cn = cn;
    this.r = r;
    this.u = u;
  }

  public static StatisticsTransactionStatusDTO valueOf(ReportTxStatisticsDTO dto) {
    return new StatisticsTransactionStatusDTO(
        dto.getIssuerBankId(),
        null,
        String.valueOf(dto.getYear()),
        String.valueOf(dto.getMonth()),
        String.valueOf(dto.getDayOfMonth()),
        String.valueOf(dto.getTotal()),
        String.valueOf(dto.getOtpCount()),
        String.valueOf(dto.getNCount()),
        String.valueOf(dto.getACount()),
        String.valueOf(dto.getYCount()),
        String.valueOf(dto.getCyCount()),
        String.valueOf(dto.getCnCount()),
        String.valueOf(dto.getRCount()),
        String.valueOf(dto.getUCount()));
  }
}
