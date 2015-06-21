package my.wf.samlib.exception;

public class SamlibException extends RuntimeException {

    public SamlibException(String message) {
        super(message);
    }

    public SamlibException(String message, Throwable cause) {
        super(message, cause);
    }

    public SamlibException(Throwable cause) {
        super(cause);
    }

    public SamlibException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
