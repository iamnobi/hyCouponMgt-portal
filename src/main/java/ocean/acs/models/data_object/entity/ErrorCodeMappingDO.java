package ocean.acs.models.data_object.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ErrorCodeMappingDO {

    private Long id;
    private Long errorGroupId;
    private String code;
    private String message;
    private String sysCreator;
    @Builder.Default
    private Long createMillis = System.currentTimeMillis();
    private String updater;
    private Long updateMillis;

    public static ErrorCodeMappingDO valueOf(ocean.acs.models.oracle.entity.ErrorCodeMapping e) {
        return new ErrorCodeMappingDO(
                e.getId(),
                e.getErrorGroupId(),
                e.getCode(),
                e.getMessage(),
                e.getSysCreator(),
                e.getCreateMillis(),
                e.getUpdater(),
                e.getUpdateMillis());
    }
    
    public static ErrorCodeMappingDO valueOf(ocean.acs.models.sql_server.entity.ErrorCodeMapping e) {
        return new ErrorCodeMappingDO(
                e.getId(),
                e.getErrorGroupId(),
                e.getCode(),
                e.getMessage(),
                e.getSysCreator(),
                e.getCreateMillis(),
                e.getUpdater(),
                e.getUpdateMillis());
    }

    public static ErrorCodeMappingDO newInstance(Long id, String errorCode, String errorMsg) {
        ErrorCodeMappingDO codeMapping = ErrorCodeMappingDO.builder().build();
        codeMapping.setId(id);
        codeMapping.setCode(errorCode);
        codeMapping.setMessage(errorMsg);

        return codeMapping;
    }
}
