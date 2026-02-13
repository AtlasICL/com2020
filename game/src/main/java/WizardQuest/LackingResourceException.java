package WizardQuest;

/**
 * Exception representing an issue relating to a lack of entity resources.
 * Either a player is
 * attempting to purchase an upgrade which they have an insufficient coin total
 * for, or they
 * are attempting to use an ability which they have an insufficient magic total
 * for.
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
