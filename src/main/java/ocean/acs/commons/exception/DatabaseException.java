package ocean.acs.commons.exception;

import lombok.Getter;
import ocean.acs.commons.enumerator.ResultStatus;

public class DatabaseException extends Exception {

    private static final long serialVersionUID = 1L;

    @Getter
    private ResultStatus resultStatus;

    public DatabaseException(ResultStatus resultStatus, String message) {
        super(message);
        this.resultStatus = resultStatus;
    }

    public DatabaseException(ResultStatus resultStatus, String message, Throwable cause) {
        super(message, cause);
        this.resultStatus = resultStatus;
    }
}
