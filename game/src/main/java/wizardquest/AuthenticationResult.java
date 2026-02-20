package wizardquest;

public record AuthenticationResult(
        String name,
        String userID,
        RoleEnum role
) {}

