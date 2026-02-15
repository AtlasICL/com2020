package WizardQuest;

public class BossEncounterCompleteEvent extends EncounterCompleteEvent{
    /**
     * Constructor for BossEncounterCompleteEvent.
     * 
     * @param source            the object that constructed the telemetry event.
     * @param userID            the ID of the user who is playing the game when the
     *                          event is
     *                          constructed.
     * @param sessionID         the ID of the session the user is currently playing. See
     *                          TelemetryListenerInterface for information about
     *                          sessions.
     * @param timeStamp         the time the event was constructed in the format
     *                          yyyy/mm/dd/hh/mm/ss.
     * @param encounterName     the name of the encounter a player is fighting.
     * @param difficulty        the difficulty used for the player's session.
     * @param stageNumber       the stage player has completed.
     * @param playerHPRemaining player HP remaining after completion.
     */
    public BossEncounterCompleteEvent(Object source, String userID, int sessionID,
            String timeStamp, EncounterEnum encounterName, DifficultyEnum difficulty, int stageNumber, int playerHPRemaining){
        super(source, userID, sessionID, timeStamp, "BossEncounterComplete", 
            encounterName, difficulty, stageNumber, playerHPRemaining);
    }
}
