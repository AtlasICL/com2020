package WizardQuest;

/**
 * Enumerates all of the game's settings.
 */
public enum SettingsType {
    TELEMETRY_ENABLED("TelemetryEnabled");

    private final String telemetryName;

    private SettingsType(String telemetryName) {
        this.telemetryName = telemetryName;
    }

    /**
     * Gets the name of the setting as specified by the telemetry schema.
     * 
     * @return the setting's telemetry schema name.
     */
    public String getTelemetryName() {
        return this.telemetryName;
    }
}
