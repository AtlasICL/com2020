package WizardQuest;

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