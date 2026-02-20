package wizardquest.telemetry;

/**
 * Exception representing an issue relating to an incorrect time stamp for telemetry.
 * The time stamp is in the future, or is of invalid format.
 */
public class TimestampValidationException extends Exception {
    /**
     * Throw the exception without a message or cause
     */
    public TimestampValidationException() {
        super();
    }

    /**
     * Throws the exception with a message to be displayed to the developer.
     * 
     * @param message the message to display.
     */
    public TimestampValidationException(String message) {
        super(message);
    }

    /**
     * Throws the exception with a message to be displayed to the developer and a
     * cause for the exception being thrown.
     * 
     * @param message the message to display.
     * @param cause   the cause for the exception.
     */
    public TimestampValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}