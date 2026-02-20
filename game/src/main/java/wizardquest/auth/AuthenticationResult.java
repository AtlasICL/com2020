package wizardquest.auth;

public record AuthenticationResult(
        String name,
        String userID,
        RoleEnum role
) {}

