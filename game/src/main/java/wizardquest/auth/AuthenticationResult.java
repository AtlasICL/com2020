package wizardquest.auth;

import wizardquest.RoleEnum;

public record AuthenticationResult(
        String name,
        String userID,
        RoleEnum role
) {}

