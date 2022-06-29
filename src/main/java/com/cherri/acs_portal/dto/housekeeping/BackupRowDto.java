package com.cherri.acs_portal.dto.housekeeping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class BackupRowDto {

    private String tableName;
    private String id;
    private String insertStatement;
    private byte[] file;

}
