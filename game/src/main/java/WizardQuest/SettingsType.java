package WizardQuest;

/**
 * Enumerates all settings types in the game.
 */

public enum SettingsType {
    TELEMETRY_ENABLED("Telemetry Enabled");

    private final String telemetryName;

    private SettingsType(String telemetryName) {
        this.telemetryName = telemetryName;
    }

    public String getTelemetryName() {
        return telemetryName;
    }
}


