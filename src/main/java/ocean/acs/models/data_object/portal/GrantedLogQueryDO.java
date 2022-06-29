package ocean.acs.models.data_object.portal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ocean.acs.commons.constant.PortalEnvironmentConstants;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotNull;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GrantedLogQueryDO extends PageQueryDO {

    @NotNull(message = "{column.notempty}")
    private Long panId;
    private Long issuerBankId;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, PortalEnvironmentConstants.AUDIT_LOG_STYLE);
    }

    public static GrantedLogQueryDO newInstance(Long panId, Long issuerBankId){
        return GrantedLogQueryDO.builder()
          .panId(panId)
          .issuerBankId(issuerBankId)
          .build();
    }
}
