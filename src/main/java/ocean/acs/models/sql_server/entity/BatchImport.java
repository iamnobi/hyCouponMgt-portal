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
import ocean.acs.models.data_object.entity.BatchImportDO;
import ocean.acs.models.entity.DBKey;
import ocean.acs.models.entity.OperatorInfo;

@DynamicUpdate
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = DBKey.TABLE_BLACK_LIST_PAN_BATCH)
public class BatchImport extends OperatorInfo {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBKey.COL_BLACK_LIST_PAN_BATCH_ID)
    private long id;

    @Column(name = DBKey.COL_BLACK_LIST_PAN_BATCH_ISSUER_BANK_ID)
    private Long issuerBankId;

    @Column(name = DBKey.COL_BLACK_LIST_PAN_BATCH_BATCH_NAME)
    private String batchName;

    @Column(name = DBKey.COL_BLACK_LIST_PAN_BATCH_FILE_NAME)
    private String fileName;

    @Column(name = DBKey.COL_BLACK_LIST_PAN_BATCH_FILE_CONTENT)
    private byte[] fileContent;

    @Column(name = DBKey.COL_BLACK_LIST_PAN_BATCH_TRANS_STATUS)
    private String transStatus;

    @Column(name = DBKey.COL_BLACK_LIST_PAN_BATCH_PAN_NUMBER)
    private int panNumber;

    @Column(name = DBKey.COL_AUDIT_STATUS)
    private String auditStatus;

    public BatchImport(long id, Long issuerBankId, String batchName, String fileName,
            byte[] fileContent, String transStatus, int panNumber, String auditStatus,
            String creator, Long createMillis, String updater, Long updateMillis,
            Boolean deleteFlag, String deleter, Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.issuerBankId = issuerBankId;
        this.batchName = batchName;
        this.fileName = fileName;
        this.fileContent = fileContent;
        this.transStatus = transStatus;
        this.panNumber = panNumber;
        this.auditStatus = auditStatus;
    }

    public static BatchImport valueOf(BatchImportDO d) {
        return new BatchImport(d.getId(), d.getIssuerBankId(), d.getBatchName(), d.getFileName(),
                d.getFileContent(), d.getTransStatus(), d.getPanNumber(), d.getAuditStatus(),
                d.getCreator(), d.getCreateMillis(), d.getUpdater(), d.getUpdateMillis(),
                d.getDeleteFlag(), d.getDeleter(), d.getDeleteMillis());
    }

}
