package ocean.acs.models.sql_server.entity;

import lombok.*;
import ocean.acs.models.entity.DBKey;
import ocean.acs.models.entity.SysOperatorInfo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = DBKey.TABLE_CURRENCY)
public class Currency extends SysOperatorInfo {

    private static final long serialVersionUID = 1L;

    @Id
    @NonNull
    @Column(name = DBKey.COL_CURRENCY_CODE)
    private String code;

    @NonNull
    @Column(name = DBKey.COL_CURRENCY_ALPHA)
    private String alpha;

    @Column(name = DBKey.COL_CURRENCY_EXPONENT)
    private Integer exponent;

    @Column(name = DBKey.COL_CURRENCY_USD_EXCHANGE_RATE)
    private Double usdExchangeRate;

}
