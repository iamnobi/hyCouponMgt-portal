package com.cherri.acs_portal.dto.bank;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigInteger;
import java.util.List;

@Data
@NoArgsConstructor
@ToString
public class IssuerBinRangeQueryDto {
  private String cardBrand;
  private String cardType;
  private List<BinRanges> binRangesArray;

  public IssuerBinRangeQueryDto(String cardBrand, String cardType, List<BinRanges> binRangesArray) {
    this.cardBrand = cardBrand;
    this.cardType = cardType;
    this.binRangesArray = binRangesArray;
  }

  @Data
  @NoArgsConstructor
  @ToString
  public static class BinRanges {
    private BigInteger startBin;
    private BigInteger endBin;

    public BinRanges(BigInteger startBin, BigInteger endBin) {
      this.startBin = startBin;
      this.endBin = endBin;
    }
  }
}
