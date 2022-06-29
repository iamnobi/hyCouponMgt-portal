package ocean.acs.models.data_object.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class BatchImportDO extends OperatorInfoDO {

    private long id;
    private Long issuerBankId;
    private String batchName;
    private String fileName;
    private byte[] fileContent;
    private String transStatus;
    private int panNumber;
    private String auditStatus;

    public BatchImportDO(long id, Long issuerBankId, String batchName, String fileName,
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

    public static BatchImportDO valueOf(ocean.acs.models.oracle.entity.BatchImport e) {
        return new BatchImportDO(e.getId(), e.getIssuerBankId(), e.getBatchName(), e.getFileName(),
                e.getFileContent(), e.getTransStatus(), e.getPanNumber(), e.getAuditStatus(),
                e.getCreator(), e.getCreateMillis(), e.getUpdater(), e.getUpdateMillis(),
                e.getDeleteFlag(), e.getDeleter(), e.getDeleteMillis());
    }

    public static BatchImportDO valueOf(ocean.acs.models.sql_server.entity.BatchImport e) {
        return new BatchImportDO(e.getId(), e.getIssuerBankId(), e.getBatchName(), e.getFileName(),
                e.getFileContent(), e.getTransStatus(), e.getPanNumber(), e.getAuditStatus(),
                e.getCreator(), e.getCreateMillis(), e.getUpdater(), e.getUpdateMillis(),
                e.getDeleteFlag(), e.getDeleter(), e.getDeleteMillis());
    }

}
