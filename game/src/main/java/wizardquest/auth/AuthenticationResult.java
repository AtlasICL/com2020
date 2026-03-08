package wizardquest.auth;
/**
 * AuthenticationResult - result of auth. attempt
 */
public record AuthenticationResult(
        String name,
        String userID,
        RoleEnum role
) {}

