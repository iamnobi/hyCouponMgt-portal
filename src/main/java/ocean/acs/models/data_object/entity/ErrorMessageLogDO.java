package ocean.acs.models.data_object.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.MessageType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessageLogDO {

    private Long id;
    private String errorCode;
    private String errorComponent;
    private String errorDescription;
    private String errorDetail;
    private MessageType errorMessageType;
    private String messageType;
    private String messageVersion;
    private String sysCreator;
    @Builder.Default
    private Long createMillis = System.currentTimeMillis();
    private String updater;
    private Long updateMillis;

    public static ErrorMessageLogDO valueOf(ocean.acs.models.oracle.entity.ErrorMessageLog e) {
        return new ErrorMessageLogDO(e.getId(), e.getErrorCode(), e.getErrorComponent(),
                e.getErrorDescription(), e.getErrorDetail(), e.getErrorMessageType(),
                e.getMessageType(), e.getMessageVersion(), e.getSysCreator(), e.getCreateMillis(),
                e.getUpdater(), e.getUpdateMillis());
    }
    
    public static ErrorMessageLogDO valueOf(ocean.acs.models.sql_server.entity.ErrorMessageLog e) {
        return new ErrorMessageLogDO(e.getId(), e.getErrorCode(), e.getErrorComponent(),
                e.getErrorDescription(), e.getErrorDetail(), e.getErrorMessageType(),
                e.getMessageType(), e.getMessageVersion(), e.getSysCreator(), e.getCreateMillis(),
                e.getUpdater(), e.getUpdateMillis());
    }

}
