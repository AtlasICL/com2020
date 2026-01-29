package WizardQuest;

public class GainCoinEvent extends EncounterEvent{
    private final int coinsGained;
    /**
     * Constructor for GainCoinEvent.
     * 
     * @param source      the object that constructed the telemetry event.
     * @param userID      the ID of the user who is playing the game when the
     *                    event is
     *                    constructed.
     * @param sessionID   the ID of the session the user is currently playing. See
     *                    TelemetryListenerInterface for information about
     *                    sessions.
     * @param timeStamp   the time the event was constructed in the format
     *                    yyyy/mm/dd/hh/mm/ss
     * @param difficulty  the difficulty used for the players session.
     * @param stageNumber the stage player has completed.
     * @param coinsGained number of coins gained by the player.
     */
    public GainCoinEvent(Object source, int userID, int sessionID, 
        String timeStamp, Difficulty difficulty, EncounterType encounterName, int stageNumber, int coinsGained){
        super(source, userID, sessionID, timeStamp, "BossEncounterStart", encounterName, stageNumber, difficulty);
        this.coinsGained = coinsGained;
    }
    /**
     * Gets the stored number of coins gained.
     * 
     * @return number of coins gained.
     */
    public int GetCoinsGained(){
        return this.coinsGained;
    }
}
