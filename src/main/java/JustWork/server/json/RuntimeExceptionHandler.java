package JustWork.server.json;

public class RuntimeExceptionHandler extends Exception {

    private static final long serialVersionUID = -450470888919715864L;

    public RuntimeExceptionHandler(String message) {
        super(message);
    }

    public RuntimeExceptionHandler(Throwable cause) {
        super(cause);
    }

    public RuntimeExceptionHandler(String message, Throwable cause) {
        super(message, cause);
    }
}
