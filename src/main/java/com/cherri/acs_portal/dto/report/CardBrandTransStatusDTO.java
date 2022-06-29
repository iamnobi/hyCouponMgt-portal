package com.cherri.acs_portal.dto.report;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class CardBrandTransStatusDTO {
  private String cardBrand;
    private CardBrandTransStatusDTO.TransStatus aRes;
  private CardBrandTransStatusDTO.TransStatus rReq;

  private CardBrandTransStatusDTO(
    String cardBrand,
    CardBrandTransStatusDTO.TransStatus aRes,
    CardBrandTransStatusDTO.TransStatus rReq) {
    this.cardBrand = cardBrand;
    this.aRes = aRes;
    this.rReq = rReq;
  }

  public static CardBrandTransStatusDTO newInstance(
    String cardBrand,
    CardBrandTransStatusDTO.TransStatus aRes,
    CardBrandTransStatusDTO.TransStatus rReq) {
    return new CardBrandTransStatusDTO(cardBrand, aRes, rReq);
  }

    public static CardBrandTransStatusDTO empty(String cardBrand) {
        return new CardBrandTransStatusDTO(cardBrand, TransStatus.empty(), TransStatus.empty());
    }

  @Getter
  public static class TransStatus {
    private final Long y;
    private final Long n;
    private final Long u;
    private final Long a;
    private final Long c;
    private final Long r;

    private TransStatus() {
      this.y = 0L;
      this.n = 0L;
      this.u = 0L;
      this.a = 0L;
      this.c = 0L;
      this.r = 0L;
    }

    private TransStatus(long y, long n, long c, long r, long u) {
      this.y = y;
      this.n = n;
      this.u = u;
      this.a = 0L;
      this.c = c;
      this.r = r;
    }

    private TransStatus(long y, long n) {
      this.y = y;
      this.n = n;
      this.u = 0L;
      this.a = 0L;
      this.c = 0L;
      this.r = 0L;
    }

    public static CardBrandTransStatusDTO.TransStatus newInstanceForARes(
        long y, long n, long c, long r, long u) {
      return new CardBrandTransStatusDTO.TransStatus(y, n, c, r, u);
    }

    public static CardBrandTransStatusDTO.TransStatus newInstanceForRReq(long y, long n) {
      return new CardBrandTransStatusDTO.TransStatus(y, n);
    }

    public static CardBrandTransStatusDTO.TransStatus empty() {
      return new CardBrandTransStatusDTO.TransStatus();
    }
  }
}
