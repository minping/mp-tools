package dbfKit.exception;

public class DBFException extends RuntimeException {
    private static final long serialVersionUID = 1906727217048909819L;


    /**
     * Constructs an DBFException with the specified detail message.
     * @param message The detail message (which is saved for later retrieval by the Throwable.getMessage() method)
     */
    public DBFException(String message) {
        super(message);
    }
    /**
     * Constructs an DBFException with the specified detail message and cause.
     * @param message The detail message (which is saved for later retrieval by the Throwable.getMessage() method)
     * @param cause The cause (which is saved for later retrieval by the Throwable.getCause() method).
     */
    public DBFException(String message, Throwable cause) {
        super(message, cause);
    }
    /**
     * Constructs an DBFException with the specified cause.
     * @param cause The cause (which is saved for later retrieval by the Throwable.getCause() method).
     */
    public DBFException(Throwable cause) {
        super(cause);
    }
}
