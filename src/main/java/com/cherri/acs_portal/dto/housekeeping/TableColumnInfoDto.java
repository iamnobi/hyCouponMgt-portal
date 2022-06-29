package com.cherri.acs_portal.dto.housekeeping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class TableColumnInfoDto {

    private String tableName;
    private Integer level;
    private List<ColumnInfoDto> columnInfoDtoList;
    private boolean hasBlob;

    @Data
    @AllArgsConstructor
    public static class ColumnInfoDto {
        private String columnName;
        private String dataType;
    }

}
