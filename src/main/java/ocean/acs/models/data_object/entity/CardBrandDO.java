package ocean.acs.models.data_object.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardBrandDO {
    private String name;
    private String pattern;
    private long displayOrder;
    private String displayName;

    public static CardBrandDO valueOf(ocean.acs.models.oracle.entity.CardBrand e) {
        return new CardBrandDO(e.getName(), e.getPattern(), e.getDisplayOrder(), e.getDisplayName());
    }
    
    public static CardBrandDO valueOf(ocean.acs.models.sql_server.entity.CardBrand e) {
        return new CardBrandDO(e.getName(), e.getPattern(), e.getDisplayOrder(), e.getDisplayName());
    }
}
