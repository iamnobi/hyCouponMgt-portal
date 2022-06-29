package com.cherri.acs_portal.dto.blackList.output;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ocean.acs.models.data_object.entity.BatchImportDO;

@Data
@ToString
@NoArgsConstructor
public class BlackListPanBatchDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long batchId;
    private String batchName;

    public static BlackListPanBatchDTO valueOf(BatchImportDO entity) {
        BlackListPanBatchDTO dto = new BlackListPanBatchDTO();
        dto.setBatchId(entity.getId());
        dto.setBatchName(entity.getBatchName());
        return dto;
    }
}
