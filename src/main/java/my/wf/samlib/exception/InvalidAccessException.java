package my.wf.samlib.exception;

public class InvalidAccessException extends SamlibException {
    public InvalidAccessException(String message) {
        super(message);
    }

    public InvalidAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidAccessException(Throwable cause) {
        super(cause);
    }

    public InvalidAccessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
