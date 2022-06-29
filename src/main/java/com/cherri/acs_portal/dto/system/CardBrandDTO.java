package com.cherri.acs_portal.dto.system;

import lombok.Builder;
import lombok.Data;
import ocean.acs.commons.enumerator.CavvKeyAlgType;
import ocean.acs.models.data_object.entity.CardBrandDO;

@Data
@Builder
public class CardBrandDTO {

    private String name;
    private String pattern;
    private long displayOrder;
    private String displayName;
    private int cavvKeyNum;
    private CavvKeyAlgType cavvKeyAlgType;

    // 相容 enum CardBrand
    public String name() {
        return name;
    }

    // 相容 enum CardBrand
    public String toString() {
        return name;
    }

    public static CardBrandDTO valueOf(CardBrandDO cardBrand) {
        return CardBrandDTO.builder()
          .name(cardBrand.getName())
          .pattern(cardBrand.getPattern())
          .displayOrder(cardBrand.getDisplayOrder())
          .displayName(cardBrand.getDisplayName())
          .build();
    }
}
