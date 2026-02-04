package WizardQuest;

public abstract class EncounterStartEvent extends EncounterEvent {
    /**
     * Constructor for the encounter start telemetry event. Produces a telemetry event storing
     * common data.
     */
    public EncounterStartEvent(Object source, int userID, int sessionID, String timeStamp, String telemetryName, EncounterType encounterName,
                          Difficulty difficulty, int stageNumber) {
        super(source, userID, sessionID, timeStamp, telemetryName, encounterName, difficulty, stageNumber);
    }
}