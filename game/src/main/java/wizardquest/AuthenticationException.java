package wizardquest;

/**
 * Exception representing an issue relating to user authentication. Either an
 * authenticated user is required and none is available, or an issue arises with
 * authenticating a user.
 */
public class AuthenticationException extends Exception {
    /**
     * Throw the exception without a message or cause
     */
    public AuthenticationException() {
        super();
    }

    /**
     * Throws the exception with a message to be displayed to the developer.
     * 
     * @param message the message to display.
     */
    public AuthenticationException(String message) {
        super(message);
    }

    /**
     * Throws the exception with a message to be displayed to the developer and a
     * cause for the exception being thrown.
     * 
     * @param message the message to display.
     * @param cause   the cause for the exception.
     */
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
