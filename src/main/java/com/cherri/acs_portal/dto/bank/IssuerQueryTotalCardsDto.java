package com.cherri.acs_portal.dto.bank;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class IssuerQueryTotalCardsDto {
    private String cardBrand;
    private int cardType;
    private BinRanges[] binRangesArray;

    public IssuerQueryTotalCardsDto(String cardBrand, int cardType, BinRanges[] binRangesArray) {
        this.cardBrand = cardBrand;
        this.cardType = cardType;
        this.binRangesArray = binRangesArray;
    }

    @Data
    @NoArgsConstructor
    @ToString
    public static class BinRanges {
        Long startBin;
        Long endBin;

        public BinRanges(Long startBin, Long endBin) {
            this.startBin = startBin;
            this.endBin = endBin;
        }
    }
}
