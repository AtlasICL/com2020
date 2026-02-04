package WizardQuest;

public class NormalEncounterStartEvent extends EncounterStartEvent {
    /**
     * Constructor for the normal encounter start telemetry event. Produces a telemetry event storing
     * common data.
     */
    public NormalEncounterStartEvent(Object source, int userID, int sessionID, String timeStamp, String telemetryName, EncounterType encounterName,
                                     Difficulty difficulty, int stageNumber) {
        super(source, userID, sessionID, timeStamp, telemetryName, encounterName, difficulty, stageNumber);
    }
}

