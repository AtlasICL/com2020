package WizardQuest;

public class NormalEncounterFailEvent extends EncounterEvent{
    final int livesLeft;
    /**
     * Constructor for NormalEncounterFailEvent.
     * 
     * @param source        the object that constructed the telemetry event.
     * @param userID        the ID of the user who is playing the game when the
     *                      event is
     *                      constructed.
     * @param sessionID     the ID of the session the user is currently playing. See
     *                      TelemetryListenerInterface for information about
     *                      sessions.
     * @param timeStamp     the time the event was constructed in the format
     *                      yyyy/mm/dd/hh/mm/ss
     * @param encounterName the name of the encounter a player is fighting.
     * @param difficulty    the difficulty used for the players session.
     * @param stageNumber   the stage player has completed.
     * @param livesLeft     player HP remaining after completion.
     */
    public NormalEncounterFailEvent(Object source, int userID, int sessionID, 
            String timeStamp, EncounterType encounterName, Difficulty difficulty, int stageNumber, int livesLeft){
        super(source, userID, sessionID, timeStamp, "NormalEncounterFail", 
            encounterName, stageNumber, difficulty);
        this.livesLeft = livesLeft;
    }
    public int GetLivesLeft(){
        return this.livesLeft;
    }
}
