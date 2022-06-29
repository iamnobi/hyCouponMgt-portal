package ocean.acs.models.sql_server.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ocean.acs.commons.enumerator.AuditActionType;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.models.data_object.entity.AuditingDO;
import ocean.acs.models.entity.DBKey;
import ocean.acs.models.entity.OperatorInfo;

@DynamicUpdate
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = DBKey.TABLE_AUDITING)
public class Auditing extends OperatorInfo {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBKey.COL_AUDITING_ID)
    private Long id;

    @NonNull
    @Column(name = DBKey.COL_AUDITING_ISSUER_BANK_ID)
    private Long issuerBankId;

    @NonNull
    @Column(name = DBKey.COL_AUDITING_FUNCTION_TYPE)
    private String functionType;

    @NonNull
    @Column(name = DBKey.COL_AUDITING_ACTION_TYPE)
    private String actionType;

    @NonNull
    @Column(name = DBKey.COL_AUDITING_AUDIT_STATUS)
    private String auditStatus;

    @NonNull
    @Column(name = DBKey.COL_AUDITING_DRAFT_CONTENT)
    private String draftContent;

    @Column(name = DBKey.COL_AUDITING_FILE_NAME)
    private String fileName;

    @Column(name = DBKey.COL_AUDITING_FILE_CONTENT)
    private byte[] fileContent;

    @Column(name = DBKey.COL_AUDITING_AUDIT_COMMENT)
    private String auditComment;

    public Auditing(Long id, Long issuerBankId, String functionType, String actionType,
            String auditStatus, String draftContent, String fileName, byte[] fileContent,
            String auditComment, String creator, Long createMillis, String updater,
            Long updateMillis, Boolean deleteFlag, String deleter, Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.issuerBankId = issuerBankId;
        this.functionType = functionType;
        this.actionType = actionType;
        this.auditStatus = auditStatus;
        this.draftContent = draftContent;
        this.fileName = fileName;
        this.fileContent = fileContent;
        this.auditComment = auditComment;
    }

    public Auditing(AuditFunctionType functionType, AuditActionType actionType,
            String draftContent) {
        this.functionType = functionType.getTypeSymbol();
        this.actionType = actionType.getTypeSymbol();
        this.draftContent = draftContent;
    }

    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.getBySymbol(functionType);
    }

    public static Auditing valueOf(AuditingDO e) {
        return new Auditing(e.getId(), e.getIssuerBankId(), e.getFunctionType().getTypeSymbol(),
                e.getActionType(), e.getAuditStatus(), e.getDraftContent(), e.getFileName(),
                e.getFileContent(), e.getAuditComment(), e.getCreator(), e.getCreateMillis(),
                e.getUpdater(), e.getUpdateMillis(), e.getDeleteFlag(), e.getDeleter(),
                e.getDeleteMillis());
    }

}
