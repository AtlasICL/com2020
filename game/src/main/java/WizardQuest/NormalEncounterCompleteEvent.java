package WizardQuest;

public class NormalEncounterCompleteEvent extends EncounterCompleteEvent {
    /**
     * Constructor for the normal encounter complete telemetry event. Produces a telemetry event storing
     * common data.
     */
    public NormalEncounterCompleteEvent(Object source, int userID, int sessionID, String timeStamp, String telemetryName, EncounterEnum encounterName,
                                  Difficulty difficulty, int stageNumber, int playerHPRemaining) {
        super(source, userID, sessionID, timeStamp, telemetryName, encounterName, difficulty, stageNumber, playerHPRemaining);
    }
}
