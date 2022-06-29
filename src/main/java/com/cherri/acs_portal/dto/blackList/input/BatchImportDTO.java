package com.cherri.acs_portal.dto.blackList.input;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.enumerator.TransStatus;
import ocean.acs.models.data_object.entity.BatchImportDO;
import ocean.acs.models.oracle.entity.BatchImport;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class BatchImportDTO extends AuditableDTO {

    private static final long serialVersionUID = 1L;

    private String batchName;
    private String authStatus;
    private TransStatus transStatus;
    private Integer panNumber;
    private Long createMillis;
    private AuditStatus auditStatus = AuditStatus.PUBLISHED;
    private String user;

    @JsonIgnore
    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.BLACK_LIST_BATCH_IMPORT;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }

    public BatchImportDTO(Long issuerBankId, String batchName, TransStatus transStatus,
      String fileName, byte[] fileContent) {
        this.issuerBankId = issuerBankId;
        this.batchName = batchName;
        this.fileName = fileName;
        this.transStatus = transStatus;
        this.fileContent = fileContent;
    }

    public static BatchImportDTO valueOf(BatchImport batchImport) {
        BatchImportDTO importDTO = new BatchImportDTO();
        importDTO.setPanNumber(batchImport.getPanNumber());
        importDTO.setFileName(batchImport.getFileName());
        importDTO.setIssuerBankId(batchImport.getIssuerBankId());
        importDTO.setId(batchImport.getId());
        importDTO.setTransStatus(TransStatus.codeOf(batchImport.getTransStatus()));
        importDTO.setBatchName(batchImport.getBatchName());
        importDTO.setAuthStatus(batchImport.getTransStatus());
        importDTO.setAuditStatus(AuditStatus.getStatusBySymbol(batchImport.getAuditStatus()));
        importDTO.setFileContent(batchImport.getFileContent());
        importDTO.setCreateMillis(batchImport.getCreateMillis());

        return importDTO;
    }

    public static BatchImportDTO valueOf(BatchImportDO batchImport) {
        BatchImportDTO importDTO = new BatchImportDTO();
        importDTO.setPanNumber(batchImport.getPanNumber());
        importDTO.setFileName(batchImport.getFileName());
        importDTO.setIssuerBankId(batchImport.getIssuerBankId());
        importDTO.setId(batchImport.getId());
        importDTO.setTransStatus(TransStatus.codeOf(batchImport.getTransStatus()));
        importDTO.setBatchName(batchImport.getBatchName());
        importDTO.setAuthStatus(batchImport.getTransStatus());
        importDTO.setAuditStatus(AuditStatus.getStatusBySymbol(batchImport.getAuditStatus()));
        importDTO.setFileContent(batchImport.getFileContent());
        importDTO.setCreateMillis(batchImport.getCreateMillis());

        return importDTO;
    }
}
