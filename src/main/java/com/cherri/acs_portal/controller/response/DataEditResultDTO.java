package com.cherri.acs_portal.controller.response;

import com.cherri.acs_portal.dto.audit.AuditableDTO;
import lombok.Data;
import ocean.acs.commons.enumerator.DataEditStatus;
import ocean.acs.models.data_object.entity.AuditingDO;
import ocean.acs.models.oracle.entity.Auditing;

@Data
public class DataEditResultDTO {

    private DataEditStatus status;

    private Long id;

    public DataEditResultDTO(Auditing auditing) {
        id = auditing.getId();
        status = DataEditStatus.AUDITING;
    }

    public DataEditResultDTO(AuditingDO auditing) {
        id = auditing.getId();
        status = DataEditStatus.AUDITING;
    }

    public DataEditResultDTO(AuditableDTO auditableDTO) {
        id = auditableDTO.getId();
        status = DataEditStatus.STORED;
    }

    public DataEditResultDTO() {
        id = 0L;
        status = DataEditStatus.STORED;
    }
}
