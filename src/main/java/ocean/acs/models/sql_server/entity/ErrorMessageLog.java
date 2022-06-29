package ocean.acs.models.sql_server.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ocean.acs.commons.enumerator.EmvErrorReason;
import ocean.acs.commons.enumerator.MessageType;
import ocean.acs.models.data_object.entity.ErrorMessageLogDO;
import ocean.acs.models.data_object.kernel.ErrorDO;
import ocean.acs.models.entity.DBKey;

@DynamicUpdate
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = DBKey.TABLE_ERROR_MESSAGE_LOG)
public class ErrorMessageLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBKey.COL_ERROR_MESSAGE_LOG_ID)
    private Long id;

    @Column(name = DBKey.COL_ERROR_MESSAGE_LOG_ERROR_CODE)
    private String errorCode;

    @Column(name = DBKey.COL_ERROR_MESSAGE_LOG_ERROR_COMPONENT)
    private String errorComponent;

    @Column(name = DBKey.COL_ERROR_MESSAGE_LOG_ERROR_DESCRIPTION)
    private String errorDescription;

    @Column(name = DBKey.COL_ERROR_MESSAGE_LOG_ERROR_DETAIL)
    private String errorDetail;

    @Enumerated(EnumType.STRING)
    @Column(name = DBKey.COL_ERROR_MESSAGE_LOG_ERROR_MESSAGE_TYPE)
    private MessageType errorMessageType;

    @Column(name = DBKey.COL_ERROR_MESSAGE_LOG_MESSAGE_TYPE)
    private String messageType;

    @Column(name = DBKey.COL_ERROR_MESSAGE_LOG_MESSAGE_VERSION)
    private String messageVersion;

    @NonNull
    @Column(name = DBKey.COL_ERROR_MESSAGE_LOG_SYS_CREATOR)
    private String sysCreator;

    @NonNull
    @Column(name = DBKey.COL_ERROR_MESSAGE_LOG_CREATE_MILLIS)
    @Builder.Default
    private Long createMillis = System.currentTimeMillis();

    @Column(name = DBKey.COL_ERROR_MESSAGE_LOG_UPDATER)
    private String updater;

    @Column(name = DBKey.COL_ERROR_MESSAGE_LOG_UPDATE_MILLIS)
    private Long updateMillis;

    public static ErrorMessageLog valueOf(ErrorMessageLogDO d) {
        return new ErrorMessageLog(d.getId(), d.getErrorCode(), d.getErrorComponent(),
                d.getErrorDescription(), d.getErrorDetail(), d.getErrorMessageType(),
                d.getMessageType(), d.getMessageVersion(), d.getSysCreator(), d.getCreateMillis(),
                d.getUpdater(), d.getUpdateMillis());
    }

    public static ErrorMessageLog valueOf(ErrorDO errorDO) {
        String creator = errorDO.getErrorMessageType() == null ? MessageType.Erro.name()
                : errorDO.getErrorMessageType();
        String errorCode =
                errorDO.getErrorCode() == null ? EmvErrorReason.TRANSIENT_SYSTEM_FAILURE.getCode()
                        : errorDO.getErrorCode();
        MessageType errorMessageType = MessageType.getByName(errorDO.getErrorMessageType());
        ErrorMessageLog entity = new ErrorMessageLog();
        entity.setErrorCode(errorDO.getErrorCode());
        entity.setErrorComponent(errorDO.getErrorComponent());
        entity.setErrorDescription(errorDO.getErrorDescription());
        entity.setErrorDetail(errorDO.getErrorDetail());
        entity.setErrorMessageType(MessageType.getByName(errorDO.getErrorMessageType()));
        entity.setMessageType(errorDO.getMessageType());
        entity.setMessageVersion(errorDO.getMessageVersion());
        entity.setErrorCode(errorCode);
        entity.setErrorMessageType(errorMessageType);
        entity.setSysCreator(creator);
        return entity;
    }

    public static ErrorMessageLog updateValueOf(ErrorDO errorDO, ErrorMessageLogDO entityDO) {
        return ErrorMessageLog.builder().errorCode(errorDO.getErrorCode())
                .errorComponent(errorDO.getErrorComponent())
                .errorDescription(errorDO.getErrorDescription())
                .errorDetail(errorDO.getErrorDetail())
                .errorMessageType(MessageType.getByName(errorDO.getErrorMessageType()))
                .messageType(errorDO.getMessageType()).messageVersion(errorDO.getMessageVersion())
                .updater(errorDO.getErrorMessageType() == null ? MessageType.Erro.name()
                        : errorDO.getErrorMessageType())
                .updateMillis(System.currentTimeMillis()).build();
    }

}
