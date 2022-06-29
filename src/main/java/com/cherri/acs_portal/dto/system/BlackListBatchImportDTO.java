package com.cherri.acs_portal.dto.system;

import com.cherri.acs_portal.dto.audit.AuditableDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.enumerator.TransStatus;
import org.springframework.web.multipart.MultipartFile;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class BlackListBatchImportDTO extends AuditableDTO {

    MultipartFile batchFile;

    String batchName;

    TransStatus transStatus;

    private AuditStatus auditStatus;

    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.BLACK_LIST_BATCH_IMPORT;
    }

    public BlackListBatchImportDTO(Long issuerBankId, MultipartFile panList, String batchName,
      TransStatus transStatus) {
        this.issuerBankId = issuerBankId;
        this.batchFile = panList;
        this.batchName = batchName;
        this.transStatus = transStatus;
    }
}
