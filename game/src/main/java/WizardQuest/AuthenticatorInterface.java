package WizardQuest;

public interface AuthenticatorInterface {

    /**
     * Prompts the user to log in via Google, and returns the result.
     *
     * @return Returns an AuthenticationResult object, which contains the
     * authenticated user's name, unique userID (sub), and their Role.
     * @throws AuthenticationException if authentication fails.
     */
    AuthenticationResult login() throws AuthenticationException;
}
