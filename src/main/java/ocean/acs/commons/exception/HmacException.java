package ocean.acs.commons.exception;

@SuppressWarnings("serial")
public class HmacException extends Exception {

    public HmacException() {
        super();
    }

    public HmacException(String message) {
        super(message);
    }

    public HmacException(String message, Throwable cause) {
        super(message, cause);
    }

    public HmacException(Throwable cause) {
        super(cause);
    }

}
