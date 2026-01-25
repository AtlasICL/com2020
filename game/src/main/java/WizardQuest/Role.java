package WizardQuest;

/**
 * Enumerates the roles a user can have. A user can have only 1 of these roles.
 */
public enum Role {
    PLAYER("Player"),
    DESIGNER("Designer"),
    DEVELOPER("Developer");

    private final String JSONName;

    private Role(String JSONName) {
        this.JSONName = JSONName;
    }

    /**
     * Gets the name of the role according to the settings JSON specification
     * 
     * @return the name of the role.
     */
    public String getJSONName() {
        return this.JSONName;
    }
}
