package WizardQuest;

/**
 * Enumerates all difficulty levels in the game.
 */
public enum Difficulty {
    EASY(),
    MEDIUM(),
    HARD();

    private final String telemetryName;

    private Difficulty(String telemetryName) {
        this.telemetryName = telemetryName;
    }

    public String getTelemetryName() {
        return telemetryName;
    }
}