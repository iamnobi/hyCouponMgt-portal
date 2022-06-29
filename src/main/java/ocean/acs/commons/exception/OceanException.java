package ocean.acs.commons.exception;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ocean.acs.commons.enumerator.MessageType;
import ocean.acs.commons.enumerator.ResultStatus;


@Data
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class OceanException extends RuntimeException {

    protected ResultStatus status = ResultStatus.UNKNOWN;
    protected String message;
    protected MessageType messageType;

    public OceanException() {
        super();
    }

    public OceanException(String message) {
        super(message);
        this.message = message;
    }

    public OceanException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public OceanException(ResultStatus status) {
        this.status = status;
        this.message = status.toString();
    }

    /**
     * 自定義錯誤訊息內容
     *
     * @param status
     * @param errorMessage
     */
    public OceanException(ResultStatus status, String errorMessage) {
        super(errorMessage);
        this.status = status;
        this.message = errorMessage;
    }

    public OceanException(ResultStatus status, String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.status = status;
        this.message = errorMessage;
    }

    public ResultStatus getResultStatus() {
        return status;
    }
}

