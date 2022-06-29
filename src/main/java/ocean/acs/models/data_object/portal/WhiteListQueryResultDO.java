package ocean.acs.models.data_object.portal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.AuditStatus;

@Data
@NoArgsConstructor
public class WhiteListQueryResultDO {

    @JsonIgnore
    private Long panId;
    private Long id;
    private String cardBrand;
    private String cardNumber;
    @JsonIgnore
    private String cardNumberHash;
    @JsonIgnore
    private String cardNumberEn;
    private Long createMillis = System.currentTimeMillis();
    private AuditStatus auditStatus;

}
