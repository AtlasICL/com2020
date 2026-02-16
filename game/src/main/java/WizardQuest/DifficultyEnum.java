package WizardQuest;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enumerates all difficulty levels in the game.
 */
public enum DifficultyEnum {
    EASY("Easy"),
    MEDIUM("Medium"),
    HARD("Hard");

    private final String telemetryName;

    private DifficultyEnum(String telemetryName) {
        this.telemetryName = telemetryName;
    }

    @JsonValue
    public String getTelemetryName() {
        return telemetryName;
    }
}
