package ocean.acs.models.data_object.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import ocean.acs.commons.enumerator.AuditActionType;
import ocean.acs.commons.enumerator.AuditFunctionType;

/**
 * AuditingDO
 *
 * @author Alan Chen
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class AuditingDO extends OperatorInfoDO {

    private Long id;
    private Long issuerBankId;
    private AuditFunctionType functionType;
    private String actionType;
    private String auditStatus;
    private String draftContent;
    private String fileName;
    private byte[] fileContent;
    private String auditComment;

    public AuditingDO(Long id, Long issuerBankId, AuditFunctionType auditFunctionType,
            String actionType, String auditStatus, String draftContent, String fileName,
            byte[] fileContent, String auditComment, String creator, Long createMillis,
            String updater, Long updateMillis, Boolean deleteFlag, String deleter,
            Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.issuerBankId = issuerBankId;
        this.functionType = auditFunctionType;
        this.actionType = actionType;
        this.auditStatus = auditStatus;
        this.draftContent = draftContent;
        this.fileName = fileName;
        this.fileContent = fileContent;
        this.auditComment = auditComment;
    }

    public AuditingDO(AuditFunctionType functionType, AuditActionType actionType, String draftContent) {
        this.functionType = functionType;
        this.actionType = actionType.getTypeSymbol();
        this.draftContent = draftContent;
    }

    public static AuditingDO valueOf(ocean.acs.models.oracle.entity.Auditing e) {
        return new AuditingDO(e.getId(), e.getIssuerBankId(), e.getFunctionType(),
                e.getActionType(), e.getAuditStatus(), e.getDraftContent(), e.getFileName(),
                e.getFileContent(), e.getAuditComment(), e.getCreator(), e.getCreateMillis(),
                e.getUpdater(), e.getUpdateMillis(), e.getDeleteFlag(), e.getDeleter(),
                e.getDeleteMillis());
    }
    
    public static AuditingDO valueOf(ocean.acs.models.sql_server.entity.Auditing e) {
        return new AuditingDO(e.getId(), e.getIssuerBankId(), e.getFunctionType(),
                e.getActionType(), e.getAuditStatus(), e.getDraftContent(), e.getFileName(),
                e.getFileContent(), e.getAuditComment(), e.getCreator(), e.getCreateMillis(),
                e.getUpdater(), e.getUpdateMillis(), e.getDeleteFlag(), e.getDeleter(),
                e.getDeleteMillis());
    }

}
