package wizardquest.auth;

/**
 * Enumerates the roles a user can have. A user can have only 1 of these roles.
 */
public enum RoleEnum {
    PLAYER("Player"),
    DESIGNER("Designer"),
    DEVELOPER("Developer");

    private final String JSONName;

    private RoleEnum(String JSONName) {
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

    /**
     * Converts the user's role from string format in the JSON file to a RoleEnum
     * object.
     *
     * @param jsonName the user role specified in the JSON file.
     * @return the user role as a RoleEnum object.
     */
    public static RoleEnum convertJSONToEnum(String jsonName) {
        for (RoleEnum role : RoleEnum.values()) {
            if (role.getJSONName().equals(jsonName)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Could not map JSON name " + jsonName + " to role.");
    }
}
