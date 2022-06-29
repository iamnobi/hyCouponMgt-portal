package ocean.acs.models.data_object.portal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.commons.constant.PortalEnvironmentConstants;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdsQueryDO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long issuerBankId;

    @NotNull(message = "{column.notempty}")
    private List<Long> ids;

    public void addId(Long id) {
        if (ids == null) {
            ids = new ArrayList<>();
        }
        ids.add(id);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, PortalEnvironmentConstants.AUDIT_LOG_STYLE);
    }
}
