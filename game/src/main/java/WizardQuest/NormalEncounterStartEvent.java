package WizardQuest;

public class NormalEncounterStartEvent extends EncounterEvent{
    /**
     * Constructor for NormalEncounterStartEvent.
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
     * @param encounterName the name of the encounter a player is fighting.
     * @param difficulty    the difficulty used for the player's session.
     * @param stageNumber   the current stage player is attempting.
     */
    public NormalEncounterStartEvent(Object source, int userID, int sessionID, 
            String timeStamp, EncounterEnum encounterName, DifficultyEnum difficulty, int stageNumber){
        super(source, userID, sessionID, timeStamp, "NormalEncounterStart", 
            encounterName, difficulty, stageNumber);
    }
}
