package ocean.acs.models.data_object.portal;

import java.io.Serializable;
import java.util.Optional;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.utils.StringCustomizedUtils;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class AuditableDO implements Serializable {

    protected Long id;
    protected Long issuerBankId;
    @JsonIgnore
    protected String fileName;
    @JsonIgnore
    protected byte[] fileContent;
    protected Integer version;

    @JsonIgnore
    public Optional<AuditFileDO> getAuditFile() {
        if (fileContent == null || StringCustomizedUtils.isEmpty(fileName)) {
            return Optional.empty();
        }

        AuditFileDO fileDTO = new AuditFileDO();
        fileDTO.setContent(fileContent);
        fileDTO.setName(fileName);

        return Optional.of(fileDTO);
    }

    public abstract void setAuditStatus(AuditStatus auditStatus);

    @JsonIgnore
    public abstract AuditFunctionType getFunctionType();

}
