package WizardQuest;

public abstract class EncounterCompleteEvent extends EncounterEvent{
    private final int playerHPRemaining;
    /**
     * Constructor for EncounterCompleteEvent.
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
     * @param telemetryName     name of the type of encounter event.
     * @param encounterName     the name of the encounter a player is fighting.
     * @param difficulty        the difficulty used for the players session.
     * @param stageNumber       the stage player has completed.
     * @param playerHPRemaining player HP remaining after completion.
     */
    public EncounterCompleteEvent(Object source, int userID, int sessionID, 
            String timeStamp, String telemetryName, EncounterEnum encounterName, Difficulty difficulty, int stageNumber, int playerHPRemaining) {
        super(source, userID, sessionID, timeStamp, telemetryName, encounterName, difficulty, stageNumber);
        this.playerHPRemaining = playerHPRemaining;
    }
    /**
     * Gets stored player HP.
     * 
     * @return player HP remaining after encounter complete.
     */
    public int getPlayerHPRemaining(){
        return this.playerHPRemaining;
    }
}
