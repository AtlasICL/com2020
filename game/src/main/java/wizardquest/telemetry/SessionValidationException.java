package wizardquest.telemetry;

/**
 * Exception representing an issue relating to an incorrect userID for telemetry.
 * The userID does not match what is expected for the session.
 */
public class SessionValidationException extends Exception {
    /**
     * Throw the exception without a message or cause
     */
    public SessionValidationException() {
        super();
    }

    /**
     * Throws the exception with a message to be displayed to the developer.
     * 
     * @param message the message to display.
     */
    public SessionValidationException(String message) {
        super(message);
    }

    /**
     * Throws the exception with a message to be displayed to the developer and a
     * cause for the exception being thrown.
     * 
     * @param message the message to display.
     * @param cause   the cause for the exception.
     */
    public SessionValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}