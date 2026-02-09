package WizardQuest;

/**
 * Exception representing an issue related to not having enough resource to
 * perform an action, such as purchasing an upgrade or using an ability that
 * costs magic.
 */
public class LackingResourceException extends Exception {
    /**
     * Throw the exception without a message or cause
     */
    public LackingResourceException() {
        super();
    }

    /**
     * Throws the exception with a message to be displayed to the developer.
     * 
     * @param message the message to display.
     */
    public LackingResourceException(String message) {
        super(message);
    }

    /**
     * Throws the exception with a message to be displayed to the developer and a
     * cause for the exception being thrown.
     * 
     * @param message the message to display.
     * @param cause   the cause for the exception.
     */
    public LackingResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
