package com.cherri.acs_portal.dto.housekeeping;

import lombok.*;

@Builder
@AllArgsConstructor
@Data
public class TabColumnDto {

    private String tableName;
    private String columnName;
    private String dataType;

}