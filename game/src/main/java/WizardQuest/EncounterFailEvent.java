package WizardQuest;

public abstract class EncounterFailEvent extends EncounterEvent{
    private final int livesLeft;

    /**
     * Constructor for EncounterFailEvent.
     * 
     * @param source        the object that constructed the telemetry event.
     * @param userID        the ID of the user who is playing the game when the
     *                      event is
     *                      constructed.
     * @param sessionID     the ID of the session the user is currently playing. See
     *                      TelemetryListenerInterface for information about
     *                      sessions.
     * @param timeStamp     the time the event was constructed in the format
     *                      yyyy/mm/dd/hh/mm/ss.
     * @param telemetryName name of the type of encounter event.
     * @param encounterName the name of the encounter a player is fighting.
     * @param difficulty    the difficulty used for the player's session.
     * @param stageNumber   the stage player has failed.
     * @param livesLeft     player lives remaining after completion.
     */
    public EncounterFailEvent(Object source, String userID, int sessionID,
            String timeStamp, String telemetryName, EncounterEnum encounterName, DifficultyEnum difficulty, int stageNumber, int livesLeft){
        super(source, userID, sessionID, timeStamp, telemetryName, encounterName, difficulty, stageNumber);
        this.livesLeft = livesLeft;
    }
    
    /**
     * Gets stored lives left.
     * 
     * @return lives left after encounter failure.
     */
    public int getLivesLeft(){
        return this.livesLeft;
    }
}
