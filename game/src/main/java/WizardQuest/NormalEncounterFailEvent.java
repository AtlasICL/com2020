package WizardQuest;

public class NormalEncounterFailEvent extends EncounterFailEvent {
    /**
     * Constructor for the normal encounter fail telemetry event. Produces a telemetry event storing
     * common data.
     */
    public NormalEncounterFailEvent(Object source, int userID, int sessionID, String timeStamp, String telemetryName, EncounterType encounterName,
                              int stageNumber, Difficulty difficulty, int livesLeft) {
        super(source, userID, sessionID, timeStamp, telemetryName, encounterName, stageNumber, difficulty, livesLeft);
    }
}

