package ocean.acs.models.data_object.portal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class DeleteDataDO extends AuditableDO {

    private String user;

    @JsonIgnore private AuditStatus auditStatus;

    private Integer version;

    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.UNKNOWN;
    }

    public static DeleteDataDO valueOf(
            Long id,
            Long issuerBankId,
            byte[] fileContent,
            String fileName,
            String user,
            Integer version,
            AuditStatus auditStatus) {
        DeleteDataDO deleteDataDO = DeleteDataDO.builder().build();
        deleteDataDO.setId(id);
        deleteDataDO.setIssuerBankId(issuerBankId);
        deleteDataDO.setFileContent(fileContent);
        deleteDataDO.setFileName(fileName);
        deleteDataDO.setUser(user);
        deleteDataDO.setVersion(version);
        deleteDataDO.setAuditStatus(auditStatus);

        return deleteDataDO;
    }
}
