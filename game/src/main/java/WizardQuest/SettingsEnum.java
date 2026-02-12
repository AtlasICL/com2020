package WizardQuest;

/**
 * Enumerates all settings types in the game.
 */
public enum SettingsEnum {
    TELEMETRY_ENABLED("TelemetryEnabled");

    private final String telemetryName;

    private SettingsEnum(String telemetryName) {
        this.telemetryName = telemetryName;
    }

    public String getTelemetryName() {
        return telemetryName;
    }
}