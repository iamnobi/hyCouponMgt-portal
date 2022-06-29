package ocean.acs.models.sql_server.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.models.entity.DBKey;
import ocean.acs.models.entity.OperatorInfo;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = DBKey.TABLE_CARD_BRAND)
public class CardBrand extends OperatorInfo {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = DBKey.COL_CARD_BRAND_NAME)
    private String name;

    @Column(name = DBKey.COL_CARD_BRAND_PATTERN)
    private String pattern;

    @Column(name = DBKey.COL_CARD_BRAND_DISPLAY_ORDER)
    private Long displayOrder;

    @Column(name = DBKey.COL_CARD_BRAND_DISPLAY_NAME)
    private String displayName;
}
